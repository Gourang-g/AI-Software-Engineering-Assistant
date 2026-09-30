from fastapi import APIRouter, Body
from pydantic import BaseModel, Field

router = APIRouter(
    prefix="/api/project",
    tags=["Project"]
)

class ProjectAnalysisRequest(BaseModel):
     project_name: str = Field(validation_alias="projectName")
     
@router.post("/analyze")
def analyze_project(request_data: ProjectAnalysisRequest): # 👈 Accept the model here
    return {
        "project": request_data.project_name,
        "status": "received",
        "message": "Project received by AI service"
    }