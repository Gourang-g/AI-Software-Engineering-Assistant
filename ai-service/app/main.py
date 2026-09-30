from fastapi import FastAPI
from app.api.project import router as project_router

app = FastAPI(
title="AI Software Engineering Assistant",
description="AI service for codebase analysis and RAG",
version="1.0.0"
)

app.include_router(project_router)

@app.get("/")
def root():
    return {"message": "Welcome to the AI Software Engineering Assistant!"}

@app.get("/health")
def health():
    return {"status": "healthy"}
