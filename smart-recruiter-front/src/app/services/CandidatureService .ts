
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type StatutCandidature =
  | 'EN_ATTENTE'
  | 'ACCEPTEE'
  | 'REJETEE';

@Injectable({
  providedIn: 'root'
})
export class CandidatureService {

  private apiUrl = 'http://localhost:8082/api/candidatures';

  constructor(private http: HttpClient) {}

  modifierStatut(
    id: number,
    statut: StatutCandidature
  ): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/${id}/statut`,
      { statut }
    );
  }
}
