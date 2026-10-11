from fastapi import FastAPI, UploadFile, File, HTTPException
from pypdf import PdfReader
from io import BytesIO
from cv_analyzer import analyser_cv
from cv_schemas import AnalyseCVResponse
app = FastAPI(
    title="SmartRecruiter AI",
    description="Service NLP pour analyser les CV",
    version="1.0.0"
)


@app.get("/")
def accueil():
    return {
        "message": "SmartRecruiter AI fonctionne",
        "status": "OK"
    }


@app.get("/health")
def verifier_service():
    return {
        "service": "NLP",
        "status": "actif"
    }


@app.post("/api/cv/analyze", response_model=AnalyseCVResponse)
async def analyser_cv_endpoint(fichier: UploadFile = File(...)):
    if not fichier.filename or not fichier.filename.lower().endswith(".pdf"):
        raise HTTPException(
            status_code=400,
            detail="Veuillez envoyer un fichier PDF."
        )

    try:
        contenu = await fichier.read()
        lecteur = PdfReader(BytesIO(contenu))

        textes = [page.extract_text() or "" for page in lecteur.pages]
        texte_complet = "\n".join(textes).strip()

        if not texte_complet:
            raise HTTPException(
                status_code=422,
                detail="Aucun texte détecté. Le PDF est peut-être scanné."
            )

        informations = analyser_cv(texte_complet)

        return {
            "nom_fichier": fichier.filename,
            "nombre_pages": len(lecteur.pages),
            "informations": informations
        }

    except HTTPException:
        raise
    except Exception as erreur:
        raise HTTPException(
            status_code=400,
            detail=f"Impossible d'analyser le PDF : {str(erreur)}"
        )

@app.post("/api/cv/extract-text")
async def extraire_texte_cv(
    fichier: UploadFile = File(...)
):
    if not fichier.filename or not fichier.filename.lower().endswith(".pdf"):
        raise HTTPException(
            status_code=400,
            detail="Veuillez envoyer un fichier PDF."
        )

    try:
        contenu = await fichier.read()
        lecteur = PdfReader(BytesIO(contenu))

        textes = [
            page.extract_text() or ""
            for page in lecteur.pages
        ]

        texte_complet = "\n".join(textes).strip()

        if not texte_complet:
            raise HTTPException(
                status_code=422,
                detail="Aucun texte détecté. Le PDF est peut-être scanné."
            )

        return {
            "nom_fichier": fichier.filename,
            "nombre_pages": len(lecteur.pages),
            "texte_extrait": texte_complet
        }

    except HTTPException:
        raise

    except Exception as erreur:
        raise HTTPException(
            status_code=400,
            detail=f"Impossible de lire le PDF : {str(erreur)}"
        )


@app.post("/api/cv/analyze")
async def analyser_fichier_cv(
    fichier: UploadFile = File(...)
):
    if not fichier.filename or not fichier.filename.lower().endswith(".pdf"):
        raise HTTPException(
            status_code=400,
            detail="Veuillez envoyer un fichier PDF."
        )

    try:
        contenu = await fichier.read()
        lecteur = PdfReader(BytesIO(contenu))

        texte = "\n".join(
            page.extract_text() or ""
            for page in lecteur.pages
        )

        if not texte.strip():
            raise HTTPException(
                status_code=422,
                detail="Aucun texte détecté dans le PDF."
            )

        resultat = analyser_cv(texte)

        return {
            "nom_fichier": fichier.filename,
            "nombre_pages": len(lecteur.pages),
            "informations": resultat
        }

    except HTTPException:
        raise

    except Exception as erreur:
        raise HTTPException(
            status_code=400,
            detail=f"Erreur pendant l'analyse : {str(erreur)}"
        )