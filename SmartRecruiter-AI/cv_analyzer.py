import re
import unicodedata


# ============================================================
# COMPETENCES
# ============================================================

COMPETENCES_CONNues = [
    "Pack Office",
    "Airtable",
    "Tableaux de bord",
    "BI",
    "Google Sheets",
    "Zendesk",
    "Slack",
    "Google Hangouts",
    "CRM",
    "Java",
    "Spring Boot",
    "Python",
    "Docker",
    "Linux",
    "MySQL",
    "Angular",
    "Cisco",
    "SQL",
]


# ============================================================
# LANGUES
# ============================================================

LANGUES_CONNues = [
    "Français",
    "Anglais",
    "Arabe",
    "Allemand",
    "Espagnol",
    "Italien",
]


# ============================================================
# NETTOYAGE
# ============================================================

def nettoyer_texte(texte: str) -> str:
    """Nettoie le texte extrait du PDF."""

    texte = unicodedata.normalize("NFKC", texte)

    texte = texte.replace("\u00a0", " ")

    # Plusieurs espaces -> un seul
    texte = re.sub(r"[ \t]+", " ", texte)

    return texte.strip()


# ============================================================
# EMAIL
# ============================================================

def extraire_email(texte: str):
    """Extrait l'adresse email."""

    resultat = re.search(
        r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}\b",
        texte
    )

    if resultat:
        return resultat.group(0)

    return None


# ============================================================
# TELEPHONE
# ============================================================

def extraire_telephone(texte: str):
    """Extrait un numéro de téléphone tunisien."""

    resultat = re.search(
        r"(?<!\d)(?:\+?216[\s.-]*)?(?:\d[\s.-]*){8}(?!\d)",
        texte
    )

    if not resultat:
        return None

    chiffres = re.sub(r"\D", "", resultat.group(0))

    if len(chiffres) == 11 and chiffres.startswith("216"):
        return "+216 " + chiffres[3:]

    if len(chiffres) == 8:
        return "+216 " + chiffres

    return None


# ============================================================
# NOM
# ============================================================

def extraire_nom(texte: str):
    """Extrait approximativement le nom du candidat."""

    lignes = [
        ligne.strip()
        for ligne in texte.splitlines()
        if ligne.strip()
    ]

    if not lignes:
        return None

    return lignes[0]


# ============================================================
# LOCALISATION
# ============================================================

def extraire_localisation(texte: str):
    """Recherche une ville tunisienne connue."""

    localisations = [
        "Ben Arous",
        "Tunis",
        "Ariana",
        "Mégrine",
        "Sfax",
        "Sousse",
        "Nabeul",
        "Monastir",
        "Bizerte",
        "Manouba",
        "Mourouj",
        "La Marsa",
        "Ezzahra",
        "Hammamet",
    ]

    for ville in localisations:

        motif = (
            r"(?<!\w)"
            + re.escape(ville)
            + r"(?!\w)"
        )

        if re.search(
            motif,
            texte,
            flags=re.IGNORECASE
        ):
            return ville

    return None


# ============================================================
# COMPETENCES
# ============================================================

def extraire_competences(texte: str):
    """Extrait les compétences connues."""

    competences_trouvees = []

    for competence in COMPETENCES_CONNues:

        motif = (
            r"(?<!\w)"
            + re.escape(competence)
            + r"(?!\w)"
        )

        if re.search(
            motif,
            texte,
            flags=re.IGNORECASE
        ):
            competences_trouvees.append(competence)

    return competences_trouvees


# ============================================================
# LANGUES
# ============================================================

def extraire_langues(texte: str):
    """Extrait les langues."""

    langues_trouvees = []

    for langue in LANGUES_CONNues:

        motif = (
            r"(?<!\w)"
            + re.escape(langue)
            + r"(?!\w)"
        )

        if re.search(
            motif,
            texte,
            flags=re.IGNORECASE
        ):
            langues_trouvees.append(langue)

    return langues_trouvees


# ============================================================
# OUTILS DATES
# ============================================================

def extraire_periode(texte: str):
    """
    Recherche une période dans un texte.

    Exemples acceptés :

    2019 - 2022
    2019 – 2022
    2019 à 2022
    2019 - aujourd'hui
    2019 à présent
    Mars 2020 - Juin 2022
    """

    motifs = [

        # 2019 - 2022
        r"\b((?:19|20)\d{2})\s*[-–—]\s*((?:19|20)\d{2})\b",

        # 2019 à 2022
        r"\b((?:19|20)\d{2})\s+à\s+((?:19|20)\d{2})\b",

        # 2019 - aujourd'hui
        r"\b((?:19|20)\d{2})\s*[-–—]\s*"
        r"(aujourd'hui|aujourd’hui|présent|present|actuel)\b",

        # 2019 à aujourd'hui
        r"\b((?:19|20)\d{2})\s+à\s+"
        r"(aujourd'hui|aujourd’hui|présent|present|actuel)\b",

        # mois année - mois année
        r"\b("
        r"janvier|février|fevrier|mars|avril|mai|juin|juillet|"
        r"août|aout|septembre|octobre|novembre|décembre|decembre"
        r")\s+((?:19|20)\d{2})"
        r"\s*[-–—]\s*"
        r"("
        r"janvier|février|fevrier|mars|avril|mai|juin|juillet|"
        r"août|aout|septembre|octobre|novembre|décembre|decembre"
        r")\s+((?:19|20)\d{2})",

        # 2019 seul
        r"\b((?:19|20)\d{2})\b",
    ]

    for motif in motifs:

        resultat = re.search(
            motif,
            texte,
            flags=re.IGNORECASE
        )

        if not resultat:
            continue

        groupes = resultat.groups()

        # ----------------------------------------------------
        # Année - année
        # ----------------------------------------------------

        if len(groupes) == 2:

            debut = groupes[0]
            fin = groupes[1]

            if fin.casefold() in [
                "aujourd'hui",
                "aujourd’hui",
                "présent",
                "present",
                "actuel",
            ]:
                return {
                    "date_debut": debut,
                    "date_fin": None,
                    "en_cours": True,
                }

            return {
                "date_debut": debut,
                "date_fin": fin,
                "en_cours": False,
            }

        # ----------------------------------------------------
        # Mois année - mois année
        # ----------------------------------------------------

        if len(groupes) == 4:

            debut = groupes[0] + " " + groupes[1]
            fin = groupes[2] + " " + groupes[3]

            return {
                "date_debut": debut,
                "date_fin": fin,
                "en_cours": False,
            }

        # ----------------------------------------------------
        # Année seule
        # ----------------------------------------------------

        if len(groupes) == 1:

            return {
                "date_debut": groupes[0],
                "date_fin": None,
                "en_cours": False,
            }

    return {
        "date_debut": None,
        "date_fin": None,
        "en_cours": False,
    }


# ============================================================
# DETECTION SECTION
# ============================================================

def est_section_experience(ligne: str):
    """Détecte une section d'expérience professionnelle."""

    ligne = ligne.casefold().strip()

    mots = [
        "expérience professionnelle",
        "experiences professionnelles",
        "expériences professionnelles",
        "expérience",
        "experiences",
        "expériences",
        "experience professionnelle",
        "professional experience",
        "professional experiences",
        "work experience",
        "work experiences",
        "employment history",
        "career history",
        "career",
        "parcours professionnel",
        "expérience de travail",
        "expériences de travail",
    ]

    return any(
        mot in ligne
        for mot in mots
    )

def est_section_formation(ligne: str):
    """Détecte une section de formation."""

    ligne = ligne.casefold().strip()

    mots = [
        "formation",
        "formations",
        "education",
        "education & formation",
        "études",
        "etudes",
        "parcours académique",
        "parcours academique",
    ]

    return any(
        mot in ligne
        for mot in mots
    )


def est_section(ligne: str):
    """Détecte une section importante du CV."""

    return (
        est_section_experience(ligne)
        or est_section_formation(ligne)
        or ligne.casefold() in [
            "compétences",
            "competences",
            "skills",
            "langues",
            "languages",
            "profil",
            "profile",
            "contact",
            "centres d'intérêt",
            "centres d'interet",
        ]
    )


# ============================================================
# EXPERIENCES
# ============================================================

def extraire_experiences(texte: str):
    """
    Extrait les expériences professionnelles
    sans dépendre d'un nom d'entreprise ou d'un poste précis.
    """

    lignes = [
        ligne.strip()
        for ligne in texte.splitlines()
        if ligne.strip()
    ]

    # --------------------------------------------------------
    # 1. Trouver la section EXPERIENCE
    # --------------------------------------------------------

    debut = None
    fin = len(lignes)

    for i, ligne in enumerate(lignes):

        if est_section_experience(ligne):

            debut = i + 1
            break

    # Aucune section expérience
    if debut is None:
        return []

    # --------------------------------------------------------
    # 2. Trouver la fin de la section
    # --------------------------------------------------------

    for i in range(debut, len(lignes)):

        if est_section_formation(lignes[i]):

            fin = i
            break

        ligne_min = lignes[i].casefold()

        if ligne_min in [
            "compétences",
            "competences",
            "skills",
            "langues",
            "languages",
            "formation",
            "formations",
            "education",
            "profil",
            "profile",
        ]:

            fin = i
            break

    bloc = lignes[debut:fin]

    if not bloc:
        return []

    # --------------------------------------------------------
    # 3. Trouver les lignes contenant des dates
    # --------------------------------------------------------

    candidats = []

    for i, ligne in enumerate(bloc):

        periode = extraire_periode(ligne)

        # On considère une ligne comme début
        # d'expérience si elle contient une vraie période.
        if (
            periode["date_debut"] is not None
            and (
                periode["date_fin"] is not None
                or periode["en_cours"]
                or re.search(
                    r"\b(?:19|20)\d{2}\b",
                    ligne
                )
            )
        ):
            candidats.append(
                (i, periode)
            )

    # --------------------------------------------------------
    # 4. Construire les expériences
    # --------------------------------------------------------

    experiences = []

    for position, (index_date, periode) in enumerate(candidats):

        # ----------------------------------------------------
        # Chercher les lignes autour de la date
        # ----------------------------------------------------

        ligne_date = bloc[index_date]

        # Lignes précédentes
        precedentes = bloc[
            max(0, index_date - 3):
            index_date
        ]

        # ----------------------------------------------------
        # Déterminer le poste
        # ----------------------------------------------------

        poste = None

        if precedentes:

            # La ligne immédiatement précédente
            # est généralement le poste.
            poste = precedentes[-1]

        # ----------------------------------------------------
        # Déterminer l'entreprise
        # ----------------------------------------------------

        entreprise = None

        # Cas classique :
        #
        # Poste
        # Entreprise — 2019 - 2022
        #
        # L'entreprise est dans la ligne de date.

        ligne_sans_date = re.sub(
            r"\b(?:19|20)\d{2}\b",
            "",
            ligne_date
        )

        ligne_sans_periode = re.sub(
            r"\s*[-–—]\s*",
            " ",
            ligne_sans_date
        )

        ligne_sans_periode = re.sub(
            r"\s+à\s+",
            " ",
            ligne_sans_periode,
            flags=re.IGNORECASE
        )

        ligne_sans_periode = re.sub(
            r"aujourd'hui|aujourd’hui|présent|present|actuel",
            "",
            ligne_sans_periode,
            flags=re.IGNORECASE
        )

        ligne_sans_periode = ligne_sans_periode.strip(
            " ,|-–—"
        )

        if ligne_sans_periode:
            entreprise = ligne_sans_periode

        # ----------------------------------------------------
        # Cas :
        #
        # Poste — Direction
        # Entreprise
        # 2019 - 2022
        # ----------------------------------------------------

        if not entreprise:

            if index_date > 0:

                ligne_precedente = bloc[index_date - 1]

                if (
                    ligne_precedente != poste
                    and not re.search(
                        r"\b(?:19|20)\d{2}\b",
                        ligne_precedente
                    )
                ):

                    entreprise = ligne_precedente

        # ----------------------------------------------------
        # Description
        # ----------------------------------------------------

        debut_description = index_date + 1

        # Si plusieurs expériences existent,
        # on s'arrête avant la prochaine date.

        if position + 1 < len(candidats):

            prochaine_date = candidats[
                position + 1
            ][0]

            fin_description = prochaine_date

        else:

            fin_description = len(bloc)

        description_lignes = bloc[
            debut_description:
            fin_description
        ]

        # Supprimer les lignes qui ressemblent
        # à une nouvelle expérience potentielle.
        description_lignes = [
            ligne
            for ligne in description_lignes
            if not est_section(ligne)
        ]

        description = " ".join(
            description_lignes
        ).strip()

        # ----------------------------------------------------
        # Nettoyage du poste
        # ----------------------------------------------------

        if poste:

            poste = re.sub(
                r"^\s*[•\-–—]\s*",
                "",
                poste
            ).strip()

        # ----------------------------------------------------
        # Ajouter expérience
        # ----------------------------------------------------

        experiences.append({

            "poste": poste,

            "entreprise": entreprise,

            "date_debut": periode["date_debut"],

            "date_fin": periode["date_fin"],

            "en_cours": periode["en_cours"],

            "description": description
            if description
            else None,

        })

    return experiences


# ============================================================
# FORMATIONS
# ============================================================

def extraire_formations(texte: str):

    lignes = [
        re.sub(r"\s+", " ", ligne).strip()
        for ligne in texte.splitlines()
        if ligne.strip()
    ]

    formations = []

    # --------------------------------------------------------
    # Trouver section formation
    # --------------------------------------------------------

    debut = None
    fin = len(lignes)

    for i, ligne in enumerate(lignes):

        if est_section_formation(ligne):

            debut = i + 1
            break

    if debut is None:
        return []

    # --------------------------------------------------------
    # Fin formation
    # --------------------------------------------------------

    for i in range(debut, len(lignes)):

        ligne_min = lignes[i].casefold()

        if ligne_min in [
            "expérience",
            "experience",
            "expérience professionnelle",
            "experience professionnelle",
            "compétences",
            "competences",
            "skills",
            "langues",
            "languages",
        ]:

            fin = i
            break

    bloc = lignes[debut:fin]

    # --------------------------------------------------------
    # Analyse du bloc
    # --------------------------------------------------------

    i = 0

    while i < len(bloc):

        ligne = bloc[i]

        periode = extraire_periode(ligne)

        # Une ligne contenant une date peut être
        # la ligne établissement.
        if periode["date_debut"] is not None:

            # Chercher diplôme avant cette ligne
            diplome = None

            if i > 0:
                diplome = bloc[i - 1]

            # Retirer les dates de la ligne établissement
            etablissement = re.sub(
                r"\b(?:19|20)\d{2}\b",
                "",
                ligne
            )

            etablissement = re.sub(
                r"\s*[-–—]\s*",
                " ",
                etablissement
            ).strip(" ,")

            # Si l'établissement est vide,
            # utiliser la ligne actuelle comme diplôme.
            if not etablissement:
                etablissement = None

            formations.append({

                "diplome": diplome,

                "etablissement": etablissement,

                "annee_debut": periode["date_debut"],

                "annee_fin": periode["date_fin"],

            })

        i += 1

    return formations


# ============================================================
# ANALYSE COMPLETE
# ============================================================

def analyser_cv(texte: str) -> dict:
    """Analyse complète du CV."""

    texte = nettoyer_texte(texte)

    return {

        "nom": extraire_nom(texte),

        "email": extraire_email(texte),

        "telephone": extraire_telephone(texte),

        "localisation": extraire_localisation(texte),

        "competences": extraire_competences(texte),

        "langues": extraire_langues(texte),

        "experiences": extraire_experiences(texte),

        "formations": extraire_formations(texte),

    }