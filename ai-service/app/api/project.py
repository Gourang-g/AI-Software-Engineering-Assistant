from fastapi import APIRouter

router = APIRouter(
    prefix="/api/project",
    tags=["Project"]
)

@router.post("/analyze")
def analyze_project(project_name: str):
    return {
        "project": project_name,
        "status": "received",
        "message": "Project received by AI service"
    }