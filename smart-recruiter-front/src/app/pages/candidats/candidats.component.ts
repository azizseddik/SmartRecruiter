
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  Candidat, CandidatService, Candidature,
  StatutCandidature, RoleUtilisateur
} from '../../services/candidat.service';

import { Offre, OffreService } from '../../services/offre.service';
import { CvService } from '../../services/cv.service';

import {
  MatchingService,
  ResultatMatching
} from '../../services/matching.service';

@Component({
  selector: 'app-candidats',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './candidats.component.html',
  styleUrl: './candidats.component.css'
})
export class CandidatsComponent implements OnInit {

  candidats: Candidat[] = [];
  offres: Offre[] = [];
  candidatures: Candidature[] = [];
  classement: ResultatMatching[] = [];

  recherche = '';
  offreIdSelectionnee: number | null = null;
  fichierCV: File | null = null;

  chargement = false;
  chargementOffres = false;
  chargementClassement = false;
  importEnCours = false;

  erreur = '';
  erreurOffres = '';
  erreurImport = '';
  erreurClassement = '';
  messageImport = '';

  statutEnCours: number | null = null;
role: RoleUtilisateur = 'RH';
  constructor(
    private candidatService: CandidatService,
    private offreService: OffreService,
    private cvService: CvService,
    private matchingService: MatchingService
  ) {}

  ngOnInit(): void {
    this.chargerCandidats();
    this.chargerOffres();
  }

  // ==========================================
  // CHARGER LES CANDIDATS
  // ==========================================

  chargerCandidats(): void {
    this.chargement = true;
    this.erreur = '';

    this.candidatService.getCandidats().subscribe({
      next: (data) => {
        this.candidats = data;
        this.chargement = false;
      },
      error: (error) => {
        console.error('Erreur chargement candidats :', error);

        this.erreur =
          'Impossible de charger les candidats. Vérifiez le backend Spring Boot.';

        this.chargement = false;
      }
    });
  }

  // ==========================================
  // RECHERCHER UN CANDIDAT
  // ==========================================

  rechercher(): void {
    const terme = this.recherche.trim();

    if (!terme) {
      this.chargerCandidats();
      return;
    }

    this.chargement = true;
    this.erreur = '';

    this.candidatService.rechercherCandidats(terme).subscribe({
      next: (data) => {
        this.candidats = data;
        this.chargement = false;
      },
      error: (error) => {
        console.error('Erreur recherche candidats :', error);

        this.erreur = 'La recherche des candidats a échoué.';
        this.chargement = false;
      }
    });
  }

  reinitialiserRecherche(): void {
    this.recherche = '';
    this.chargerCandidats();
  }

  // ==========================================
  // CHARGER LES OFFRES
  // ==========================================

  chargerOffres(): void {
    this.chargementOffres = true;
    this.erreurOffres = '';

    this.offreService.getOffres().subscribe({
      next: (data) => {
        this.offres = data;
        this.chargementOffres = false;
      },
      error: (error) => {
        console.error('Erreur chargement offres :', error);

        this.erreurOffres =
          'Impossible de charger les offres. Vérifiez GET /api/offres.';

        this.chargementOffres = false;
      }
    });
  }

  // ==========================================
  // CHARGER LES CANDIDATURES D'UNE OFFRE
  // ==========================================

chargerCandidatures(): void {
  if (this.offreIdSelectionnee === null) {
    this.candidatures = [];
    return;
  }

  const offreId = this.offreIdSelectionnee;

  this.candidatService
    .getCandidaturesParOffre(offreId)
    .subscribe({
      next: (data) => {
        if (this.offreIdSelectionnee !== offreId) {
          return;
        }

        this.candidatures = data.filter(
          candidature => !!candidature.statut
        );
      },
      error: (error) => {
        console.error(
          'Erreur chargement candidatures :',
          error
        );

        this.candidatures = [];
      }
    });
}
  // ==========================================
  // SÉLECTIONNER UNE OFFRE
  // ==========================================

  selectionnerOffre(offreId: number | null): void {
    this.offreIdSelectionnee = offreId;

    this.candidatures = [];
    this.classement = [];
    this.erreurClassement = '';

    if (offreId === null) {
      return;
    }

    this.chargerClassement();
  }

  // ==========================================
  // TROUVER LA CANDIDATURE D'UN CANDIDAT
  // ==========================================

  getCandidature(
    candidatId: number
  ): Candidature | undefined {
    return this.candidatures.find(
      candidature => candidature.candidatId === candidatId
    );
  }

  // ==========================================
  // CHARGER LE MATCHING
  // ==========================================

  chargerClassement(): void {
    if (this.offreIdSelectionnee === null) {
      this.classement = [];
      this.candidatures = [];
      this.erreurClassement = '';
      return;
    }

    this.chargementClassement = true;
    this.erreurClassement = '';

    // Charger les candidatures de l'offre sélectionnée
    this.chargerCandidatures();

    const offreId = this.offreIdSelectionnee;

    this.matchingService
      .classerCandidats(offreId)
      .subscribe({
        next: (data) => {
          if (this.offreIdSelectionnee !== offreId) {
            return;
          }

          this.classement = data;
          this.chargementClassement = false;
        },
        error: (error) => {
          console.error('Erreur classement :', error);

          this.classement = [];
          this.erreurClassement =
            'Impossible de charger le classement de cette offre.';

          this.chargementClassement = false;
        }
      });
  }

  // ==========================================
  // NOM DU CANDIDAT
  // ==========================================

  getNomCandidat(candidatId: number): string {
    const candidat = this.candidats.find(
      c => c.id === candidatId
    );

    return candidat?.nom || `Candidat #${candidatId}`;
  }

  getNomPourCandidature(
    candidature: Candidature
  ): string {
    return this.getNomCandidat(candidature.candidatId);
  }

  // ==========================================
  // SCORE DE MATCHING
  // ==========================================

  getResultatMatching(
    candidatId: number
  ): ResultatMatching | undefined {
    return this.classement.find(
      resultat => resultat.candidatId === candidatId
    );
  }

  getScoreCandidat(candidatId: number): number | null {
    const resultat = this.getResultatMatching(candidatId);

    return resultat ? resultat.score : null;
  }

  getClasseScore(score: number): string {
    if (score >= 90) {
      return 'score-excellent';
    }

    if (score >= 75) {
      return 'score-tres-bon';
    }

    if (score >= 60) {
      return 'score-moyen';
    }

    return 'score-faible';
  }

  // ==========================================
  // ÉTUDES ET ÉTABLISSEMENTS
  // ==========================================

  afficherEtudes(candidature: Candidature): string {
    return candidature.formations
      ?.map(f => f.diplome)
      .filter(
        (d): d is string => !!d?.trim()
      )
      .join(', ') || 'Non renseigné';
  }

  afficherEtablissements(
    candidature: Candidature
  ): string {
    return candidature.formations
      ?.map(f => f.etablissement)
      .filter(
        (e): e is string => !!e?.trim()
      )
      .join(', ') || 'Non renseigné';
  }

  // ==========================================
  // COMPÉTENCES
  // ==========================================

  afficherCompetences(
    candidature: Candidature
  ): string {
    return candidature.competences?.join(', ')
      || 'Non renseignées';
  }

  // ==========================================
  // STATUT DE LA CANDIDATURE
  // ==========================================
  private readonly ROLE_PAR_ETAPE: Partial<Record<StatutCandidature, RoleUtilisateur>> = {
  EN_ATTENTE: 'RH',
  ENTRETIEN: 'MANAGER',
  VALIDATION_RH: 'RH'
};

private readonly STATUTS_FINAUX: StatutCandidature[] =
  ['ACCEPTEE', 'REJETEE', 'REFUSE_ENTRETIEN'];

peutDecider(c: Candidature): boolean {
  return this.ROLE_PAR_ETAPE[c.statut] === this.role;
}

estFinal(c: Candidature): boolean {
  return this.STATUTS_FINAUX.includes(c.statut);
}

cibleAccepter(c: Candidature): StatutCandidature {
  switch (c.statut) {
    case 'EN_ATTENTE': return 'ENTRETIEN';
    case 'ENTRETIEN':  return 'VALIDATION_RH';
    default:           return 'ACCEPTEE';
  }
}

cibleRefuser(c: Candidature): StatutCandidature {
  return c.statut === 'EN_ATTENTE' ? 'REJETEE' : 'REFUSE_ENTRETIEN';
}

libelleBoutonAccepter(c: Candidature): string {
  switch (c.statut) {
    case 'EN_ATTENTE': return 'Transmettre au manager';
    case 'ENTRETIEN':  return 'Valider (technique)';
    default:           return 'Accepter';
  }
}

getLibelleStatut(statut: StatutCandidature): string {
  switch (statut) {
    case 'EN_ATTENTE':       return 'En attente RH';
    case 'ENTRETIEN':        return 'En attente du manager';
    case 'VALIDATION_RH':    return 'Validation RH finale';
    case 'REFUSE_ENTRETIEN': return 'Refusé après entretien';
    case 'ACCEPTEE':         return 'Acceptée';
    case 'REJETEE':          return 'Rejetée';
    default:                 return statut;
  }
}
modifierStatut(candidatureId: number, accepter: boolean): void {
  const candidature = this.candidatures.find(c => c.id === candidatureId);

  if (!candidature || !this.peutDecider(candidature) || this.statutEnCours !== null) {
    return;
  }

  const cible = accepter
    ? this.cibleAccepter(candidature)
    : this.cibleRefuser(candidature);

  const definitif = this.STATUTS_FINAUX.includes(cible);
  const verbe = accepter ? 'valider' : 'refuser';

  const message = definitif
    ? `Voulez-vous ${verbe} cette candidature ? Cette décision est définitive.`
    : `Voulez-vous ${verbe} cette candidature et la transmettre à l'étape suivante ?`;

  if (!confirm(message)) {
    return;
  }

  this.statutEnCours = candidatureId;

  this.candidatService
    .modifierStatut(candidatureId, cible, this.role)
    .subscribe({
      next: (response) => {
        this.candidatures = this.candidatures.map(c =>
          c.id === candidatureId
            ? { ...c, ...response, statut: response?.statut ?? cible }
            : c
        );
        this.statutEnCours = null;
      },
      error: (error) => {
        console.error('Erreur modification statut :', error);
        this.statutEnCours = null;
        alert(
          error?.error?.message ||
          'Impossible de modifier le statut. Vérifiez le backend.'
        );
      }
    });
}

  // ==========================================
  // URL DU CV
  // ==========================================

  getCvUrl(
    candidature: Candidature
  ): string | null {
    if (!candidature.cheminCV) {
      return null;
    }

    return `http://localhost:8082/api/cv/fichiers/${encodeURIComponent(
      candidature.cheminCV
    )}`;
  }

  // ==========================================
  // SÉLECTION DU FICHIER PDF
  // ==========================================

  selectionnerFichier(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;

    this.fichierCV = null;
    this.erreurImport = '';
    this.messageImport = '';

    if (!file) {
      return;
    }

    if (!file.name.toLowerCase().endsWith('.pdf')) {
      this.erreurImport =
        'Veuillez sélectionner un fichier PDF.';

      input.value = '';
      return;
    }

    if (file.size === 0) {
      this.erreurImport =
        'Le fichier sélectionné est vide.';

      input.value = '';
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      this.erreurImport =
        'Le PDF ne doit pas dépasser 10 Mo.';

      input.value = '';
      return;
    }

    this.fichierCV = file;
  }

  // ==========================================
  // IMPORTER UN CV POUR UNE OFFRE
  // ==========================================

  importerCV(): void {
    this.erreurImport = '';
    this.messageImport = '';

    if (this.offreIdSelectionnee === null) {
      this.erreurImport =
        'Sélectionnez une offre avant d’importer le CV.';

      return;
    }

    if (!this.fichierCV) {
      this.erreurImport =
        'Sélectionnez un fichier PDF.';

      return;
    }

    this.importEnCours = true;

    const offreId = this.offreIdSelectionnee;

    this.cvService
      .importerCVPourOffre(this.fichierCV, offreId)
      .subscribe({
        next: (response) => {
          console.log(
            'Résultat importation CV :',
            response
          );

          this.messageImport =
            'CV importé et candidature créée avec succès.';

          this.fichierCV = null;
          this.importEnCours = false;

          // Actualiser les données après l'importation
          this.chargerCandidats();
          this.chargerClassement();

          const input = document.getElementById(
            'fichierCV'
          ) as HTMLInputElement | null;

          if (input) {
            input.value = '';
          }
        },
        error: (error) => {
          console.error(
            'Erreur importation CV :',
            error
          );

          const messageBackend =
            error?.error?.message ??
            error?.error?.error ??
            error?.message;

          this.erreurImport =
            typeof messageBackend === 'string'
              ? `Échec de l’importation : ${messageBackend}`
              : 'Échec de l’importation. Vérifiez le PDF et les logs du backend.';

          this.importEnCours = false;
        }
      });
  }
}
