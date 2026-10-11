import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
interface Offre {
  id?: number;
  titre: string;
  description: string;
  domaine: string;
  experienceMin: number;
}
@Component({
  selector: 'app-offres',
  imports: [CommonModule, FormsModule,],
  templateUrl: './offres.component.html',
  styleUrl: './offres.component.css'
})
export class OffresComponent {
 private readonly apiUrl = 'http://localhost:8082/api/offres';
  
  offres: Offre[] = [];
  recherche = '';
  chargement = false;
  erreur = '';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.chargerOffres();
  }

  get offresFiltrees(): Offre[] {
    const terme = this.recherche.trim().toLowerCase();

    if (!terme) {
      return this.offres;
    }

    return this.offres.filter(offre =>
      offre.titre.toLowerCase().includes(terme) ||
      offre.domaine.toLowerCase().includes(terme)
    );
  }

  chargerOffres(): void {
    this.chargement = true;
    this.erreur = '';

    this.http.get<Offre[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.offres = data;
        this.chargement = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des offres :', err);
        this.erreur =
          'Impossible de charger les offres. Vérifiez que le backend est démarré et que l’API est accessible.';
        this.chargement = false;
      }
    });
  }

  supprimerOffre(offre: Offre): void {
    if (offre.id == null) {
      return;
    }

    const confirmer = window.confirm(
      `Voulez-vous supprimer l'offre « ${offre.titre} » ?`
    );

    if (!confirmer) {
      return;
    }

    this.http.delete(`${this.apiUrl}/${offre.id}`).subscribe({
      next: () => this.chargerOffres(),
      error: (err) => {
        console.error('Erreur lors de la suppression :', err);
        this.erreur = 'Impossible de supprimer cette offre.';
      }
    });
  }

}
