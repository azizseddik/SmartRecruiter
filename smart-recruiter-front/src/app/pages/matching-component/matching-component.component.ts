import { CommonModule } from '@angular/common';
import { Component ,OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
interface MatchingResponse {
  candidatId: number;
  offreId: number;
  score: number;
  resultat: string;
  competencesSatisfaites: string[];
  competencesManquantes: string[];
}
interface Candidat {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
}
@Component({
  selector: 'app-matching-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './matching-component.component.html',
  styleUrl: './matching-component.component.css'
})
export class MatchingComponentComponent implements OnInit {
 private readonly apiUrl = 'http://localhost:8082/api/matching';
 private readonly candidatsUrl = 'http://localhost:8082/api/candidats';
   candidatsParId = new Map<number, Candidat>();

  offreId: number | null = null;

  resultats: MatchingResponse[] = [];

  chargement = false;
  erreur = '';
  rechercheEffectuee = false;

  constructor(private http: HttpClient) {}
  ngOnInit(): void {
    this.http.get<Candidat[]>(this.candidatsUrl).subscribe({
      next: (liste) => {
        this.candidatsParId = new Map(liste.map((c) => [c.id, c]));
      },
      error: (err) => console.error('Chargement des candidats impossible :', err)
    });
  }
   getCandidat(id: number): Candidat | undefined {
    return this.candidatsParId.get(id);
  }

  chargerClassement(): void {
    this.erreur = '';
    this.rechercheEffectuee = false;
    this.resultats = [];

    if (
      this.offreId === null ||
      !Number.isInteger(Number(this.offreId)) ||
      Number(this.offreId) <= 0
    ) {
      this.erreur = 'Veuillez saisir un identifiant d’offre valide.';
      return;
    }

    const id = Number(this.offreId);

    this.chargement = true;

    this.http
      .get<MatchingResponse[]>(
        `${this.apiUrl}/offre/${id}/classement`
      )
      .pipe(
        finalize(() => {
          this.chargement = false;
        })
      )
      .subscribe({
        next: (data) => {
          this.resultats = [...data].sort(
            (a, b) => b.score - a.score
          );

          this.rechercheEffectuee = true;

          console.log('Classement reçu :', this.resultats);
        },

        error: (error: HttpErrorResponse) => {
          console.error('Erreur de matching :', error);

          if (error.status === 404) {
            this.erreur =
              'Offre introuvable. Vérifiez son identifiant.';
          } else if (error.status === 0) {
            this.erreur =
              'Impossible de contacter le backend. Vérifiez Spring Boot et la connexion.';
          } else {
            this.erreur =
              error.error?.message ??
              `Erreur lors du classement (HTTP ${error.status}).`;
          }
        }
      });
  }

  getClasseScore(score: number): string {
    if (score >= 90) return 'excellent';
    if (score >= 75) return 'tres-bon';
    if (score >= 60) return 'moyen';
    return 'faible';
  }

  trackByCandidat(
    index: number,
    resultat: MatchingResponse
  ): number {
    return resultat.candidatId;
  }
}
