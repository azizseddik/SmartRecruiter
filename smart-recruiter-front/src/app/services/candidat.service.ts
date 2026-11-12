
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Formation {
  diplome: string | null;
  etablissement: string | null;
  anneeDebut: string | null;
  anneeFin: string | null;
}
export type StatutCandidature =
  'EN_ATTENTE' | 'ENTRETIEN' | 'VALIDATION_RH' |
  'REFUSE_ENTRETIEN' | 'ACCEPTEE' | 'REJETEE';
export type RoleUtilisateur = 'RH' | 'MANAGER'
export interface Candidat {
  id: number;
  nom: string;
  statut: StatutCandidature;
  email?: string;
  telephone?: string;
  domaine?: string;
  experience?: number;
}

export interface Candidature {
id: number;
  candidatId: number;
  nomCandidat: string;
  emailCandidat: string;
  offreId: number;
  titreOffre: string;
  formations: Formation[];
  competences: string[];
  scoreMatching: number | null;
  statut: StatutCandidature;
  cheminCV: string | null;
  dateCandidature: string;
}

@Injectable({
  providedIn: 'root'
})
export class CandidatService {

  private readonly apiUrl = 'http://localhost:8082/api/candidats';
  private readonly candidatureUrl = 'http://localhost:8082/api/candidatures';

  constructor(private http: HttpClient) {}

  getCandidats(): Observable<Candidat[]> {
    return this.http.get<Candidat[]>(this.apiUrl);
  }

  rechercherCandidats(terme: string): Observable<Candidat[]> {
    const params = new HttpParams().set('q', terme);

    return this.http.get<Candidat[]>(
      `${this.apiUrl}/recherche`,
      { params }
    );
  }

  getCandidaturesParOffre(offreId: number): Observable<Candidature[]> {
    return this.http.get<Candidature[]>(
      `${this.candidatureUrl}/offre/${offreId}`
    );
  }

  supprimerCandidat(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

modifierStatut(
  id: number,
  statut: StatutCandidature,
  role: RoleUtilisateur
): Observable<Candidature> {
  return this.http.patch<Candidature>(
    `${this.apiUrl}/candidatures/${id}/statut`,
    { statut },
    { headers: { 'X-Role': role } }
  );

} }
