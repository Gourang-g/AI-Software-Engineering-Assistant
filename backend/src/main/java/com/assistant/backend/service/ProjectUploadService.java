
package com.assistant.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ProjectUploadService {

    private final ProjectScannerService scanner;
    private final ProjectPersistenceService persistenceService;
    private final CodeChunkingService codeChunkingService;

    @Value("${app.projects.upload-dir:uploads/projects}")
    private String uploadDir;

    public ProjectUploadService(
        ProjectScannerService scanner,
        ProjectPersistenceService persistenceService,
        CodeChunkingService codeChunkingService) {

    this.scanner = scanner;
    this.persistenceService = persistenceService;
    this.codeChunkingService = codeChunkingService;
}

    public Map<String, Object> uploadAndScan(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String filename = Objects.requireNonNullElse(
                file.getOriginalFilename(), "");

        if (!filename.toLowerCase(Locale.ROOT).endsWith(".zip")) {
            throw new IllegalArgumentException(
                    "Only ZIP project uploads are supported");
        }

        Path base = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        Files.createDirectories(base);

        // Use a random ID to avoid path traversal and name collisions.
        String projectId = UUID.randomUUID().toString();
        Path destination = base.resolve(projectId).normalize();

        Files.createDirectories(destination);

        try {
            extractZip(file, destination);
            Map<String, Object> result = scanner.scanProject(destination);

            result.put("projectId", projectId);
            result.put("originalFilename", filename);
            result.put("status", "SCANNED");

            persistenceService.saveScanResult(result);

            int chunkCount = codeChunkingService.ingestProject(projectId, destination, result.get("files"));

            return result;

        } catch (IOException | RuntimeException ex) {
            deleteDirectory(destination);
            throw ex;
        }
    }

    private void extractZip(MultipartFile file, Path destination)
            throws IOException {

        long maxExtractedBytes = 300L * 1024 * 1024;
        long maxEntries = 10_000;
        long totalBytes = 0;
        long entries = 0;

        Path root = destination.toRealPath();

        try (ZipInputStream zip = new ZipInputStream(
                new BufferedInputStream(file.getInputStream()))) {

            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                if (++entries > maxEntries) {
                    throw new IOException("ZIP contains too many entries");
                }

                Path target = destination.resolve(entry.getName())
                        .normalize();

                if (!target.startsWith(destination.normalize())) {
                    throw new IOException("Unsafe ZIP entry path");
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(target);
                } else {
                    Files.createDirectories(target.getParent());

                    Path parent = target.getParent().toRealPath();
                    if (!parent.startsWith(root)) {
                        throw new IOException("Unsafe extraction directory");
                    }

                    try (OutputStream out = Files.newOutputStream(
                            target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {

                        byte[] buffer = new byte[8192];
                        int read;

                        while ((read = zip.read(buffer)) != -1) {
                            totalBytes += read;

                            if (totalBytes > maxExtractedBytes) {
                                throw new IOException(
                                        "Extracted project exceeds size limit");
                            }

                            out.write(buffer, 0, read);
                        }
                    }
                }

                zip.closeEntry();
            }
        }
    }

    private void deleteDirectory(Path directory) {
        if (!Files.exists(directory)) {
            return;
        }

        try (var paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                            // Best-effort cleanup.
                        }
                    });
        } catch (IOException ignored) {
            // Best-effort cleanup.
        }
    }
}
