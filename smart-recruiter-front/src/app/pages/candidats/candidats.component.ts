
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import {
  Candidat,
  CandidatService,
  Candidature,
  StatutCandidature
} from '../../services/candidat.service';
import { Offre, OffreService } from '../../services/offre.service';
import { CvService } from '../../services/cv.service';
import {
  MatchingService,
  ResultatMatching
} from '../../services/matching.service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-candidats',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './candidats.component.html',
  styleUrl: './candidats.component.css'
})
export class CandidatsComponent implements OnInit {
  candidats: Candidat[] = [];
  offres: Offre[] = [];
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

  constructor(
    private candidatService: CandidatService,
    private offreService: OffreService,
    private cvService: CvService,
    private matchingService: MatchingService,

  ) {}

  ngOnInit(): void {
    this.chargerCandidats();
    this.chargerOffres();
  }

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
 candidatures: Candidature[] = [];

chargerCandidatures(): void {
  if (this.offreIdSelectionnee === null) {
    this.candidatures = [];
    return;
  }
  this.candidatService
    .getCandidaturesParOffre(this.offreIdSelectionnee)
    .subscribe({
      next: (data) => (this.candidatures = data),
      error: (e) => console.error('Erreur candidatures :', e)
    });
}

getCandidature(candidatId: number): Candidature | undefined {
  return this.candidatures.find((c) => c.candidatId === candidatId);
}

modifierStatut(
  candidatureId: number,
  statut: Exclude<StatutCandidature, 'EN_ATTENTE'>
): void {
  this.candidatService
    .modifierStatut(candidatureId, statut)
    .subscribe({
      next: () => {
        this.candidatures = this.candidatures.map((c) =>
          c.id === candidatureId ? { ...c, statut } : c
        );
      },
      error: (erreur) => {
        console.error('Erreur de modification du statut :', erreur);
        alert('Impossible de modifier le statut de la candidature.');
      }
    });
}
getLibelleStatut(statut: StatutCandidature): string {
  switch (statut) {
    case 'ACCEPTEE': return 'Acceptée';
    case 'REJETEE':  return 'Rejetée';
    default:         return 'En attente';
  }
}


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
      this.erreurImport = 'Veuillez sélectionner un fichier PDF.';
      input.value = '';
      return;
    }

    if (file.size === 0) {
      this.erreurImport = 'Le fichier sélectionné est vide.';
      input.value = '';
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      this.erreurImport = 'Le PDF ne doit pas dépasser 10 Mo.';
      input.value = '';
      return;
    }

    this.fichierCV = file;
  }

  importerCV(): void {
    this.erreurImport = '';
    this.messageImport = '';

    if (this.offreIdSelectionnee === null) {
      this.erreurImport = 'Sélectionnez une offre avant d’importer le CV.';
      return;
    }

    if (!this.fichierCV) {
      this.erreurImport = 'Sélectionnez un fichier PDF.';
      return;
    }

    this.importEnCours = true;

    this.cvService
      .importerCVPourOffre(this.fichierCV, this.offreIdSelectionnee)
      .subscribe({
        next: (response) => {
          console.log('Résultat importation CV :', response);

          this.messageImport =
            'CV importé et candidature créée avec succès.';

          this.fichierCV = null;
          this.importEnCours = false;

          this.chargerCandidats();
          this.chargerClassement();
        },
        error: (error) => {
          console.error('Erreur importation CV :', error);

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

  chargerClassement(): void {
    if (this.offreIdSelectionnee === null) {
      this.classement = [];
      this.erreurClassement = '';
      return;
    }

    this.chargementClassement = true;
    this.erreurClassement = '';

    this.matchingService
      .classerCandidats(this.offreIdSelectionnee)
      .subscribe({
        next: (data) => {
          this.classement = data;
          this.chargementClassement = false;
           this.chargerCandidatures();
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

  getNomCandidat(candidatId: number): string {
    const candidat = this.candidats.find((c) => c.id === candidatId);
    return candidat?.nom || `Candidat #${candidatId}`;
  }

  getClasseScore(score: number): string {
    if (score >= 90) return 'score-excellent';
    if (score >= 75) return 'score-tres-bon';
    if (score >= 60) return 'score-moyen';
    return 'score-faible';
  }
}
