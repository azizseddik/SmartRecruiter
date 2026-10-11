import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Candidat, CandidatService } from '../../services/candidat.service';

@Component({
  selector: 'app-candidats',
  imports: [FormsModule],
  templateUrl: './candidats.component.html',
  styleUrl: './candidats.component.css'
})
export class CandidatsComponent  implements OnInit{   
  candidats: Candidat[] = [];
  recherche = '';
  chargement = false;
  erreur = '';

  constructor(private candidatService: CandidatService) {}

  ngOnInit(): void {
    this.chargerCandidats();
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
          'Impossible de charger les candidats. Vérifiez que le backend Spring Boot fonctionne sur le port 8082.';
        this.chargement = false;
      }
    });
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
        this.erreur = 'La recherche a échoué. Vérifiez la route de recherche du backend.';
        this.chargement = false;
      }
    });
  }

  reinitialiserRecherche(): void {
    this.recherche = '';
    this.chargerCandidats();
  }

}
