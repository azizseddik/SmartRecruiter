
from typing import Optional
from pydantic import BaseModel, EmailStr, Field


class ExperienceCV(BaseModel):
    poste: Optional[str] = None
    entreprise: Optional[str] = None
    date_debut: Optional[str] = None
    date_fin: Optional[str] = None
    en_cours: Optional[bool] = None
    description: Optional[str] = None


class FormationCV(BaseModel):
    diplome: Optional[str] = None
    etablissement: Optional[str] = None
    annee_debut: Optional[str] = None
    annee_fin: Optional[str] = None


class InformationsCV(BaseModel):
    nom: Optional[str] = None
    email: Optional[str] = None
    telephone: Optional[str] = None
    localisation: Optional[str] = None
    competences: list[str] = Field(default_factory=list)
    langues: list[str] = Field(default_factory=list)
    experiences: list[ExperienceCV] = Field(default_factory=list)
    formations: list[FormationCV] = Field(default_factory=list)


class AnalyseCVResponse(BaseModel):
    nom_fichier: str
    nombre_pages: int
    informations: InformationsCV