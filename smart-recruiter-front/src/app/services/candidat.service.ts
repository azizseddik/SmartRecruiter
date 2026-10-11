import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Candidat {
  id: number;
  nom: string;
  email?: string;
  telephone?: string;
  domaine?: string;
  experience?: number;
}

@Injectable({
  providedIn: 'root'
})

export class CandidatService {
  

  private readonly apiUrl = 'http://localhost:8082/api/candidats';

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

  supprimerCandidat(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
