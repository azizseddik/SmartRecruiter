import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { Observable, throwError, map, catchError } from 'rxjs';
interface OffreRequest {
  titre: string;
  description: string;
  domaine: string;
  experienceMin: number;
}

interface Offre {
  id: number;
  titre: string;
  description: string;
  domaine: string;
  experienceMin: number;
}

@Component({
  selector: 'app-ajouter-offre',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './ajouter-offre.component.html',
  styleUrl: './ajouter-offre.component.css'
})
export class AjouterOffreComponent {
  private  apiUrl = 'http://localhost:8082/api/offres';

  offre: OffreRequest = {
    titre: '',
    description: '',
    domaine: '',
    experienceMin: 0
  };

  enCours = false;
  message = '';
  erreur = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

creerOffre(form: NgForm): Observable<string> {
  this.erreur = '';
  this.message = '';
   console.log('1. creerOffre() appelée');
  console.log('2. Formulaire valide :', form.valid);
  console.log('3. Données offre :', this.offre);
  console.log('4. URL API :', this.apiUrl);

  const offre: OffreRequest = {
    titre: this.offre.titre?.trim() ?? '',
    description: this.offre.description?.trim() ?? '',
    domaine: this.offre.domaine?.trim() ?? '',
    experienceMin: Number(this.offre.experienceMin)
  };

  // 1. Validation du formulaire
  if (
    form.invalid ||
    !offre.titre ||
    !offre.description ||
    !offre.domaine ||
    !Number.isInteger(offre.experienceMin) ||
    offre.experienceMin < 0
  ) {
    form.control.markAllAsTouched();

    this.erreur =
      'Vérifiez les champs obligatoires et l’expérience minimale.';

    return throwError(
      () => new Error(this.erreur)
    );
  }

  // 2. Indiquer que la requête est en cours
  this.enCours = true;

  console.log('Envoi de la nouvelle offre :', offre);
  console.log('URL API :', this.apiUrl);

  // 3. Envoyer la requête HTTP
  return this.http.post<Offre>(this.apiUrl, offre).pipe(
    map(() => {
      this.message = 'Offre créée avec succès.';
      this.erreur = '';

      this.offre = {
        titre: '',
        description: '',
        domaine: '',
        experienceMin: 0
      };

      return this.message;
    }),

    catchError((error: HttpErrorResponse) => {
      console.error('Erreur création offre :', error);

      this.erreur =
        error.error?.message ||
        `Erreur HTTP ${error.status} : impossible de créer l’offre.`;

      return throwError(() => error);
    }),

    finalize(() => {
      this.enCours = false;
    })
  );
}


  private getErreurCreation(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Impossible de joindre l’API. Vérifiez que le backend est démarré et accessible.';
    }

    const details = error.error;
    if (typeof details === 'string' && details.trim()) {
      return details;
    }

    if (details && typeof details === 'object') {
      const message = details.message ?? details.detail ?? details.error;
      if (typeof message === 'string' && message.trim()) {
        return message;
      }
    }

    return `La création de l’offre a échoué (HTTP ${error.status}).`;
  }
  
}
