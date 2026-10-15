
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ResultatMatching {
  candidatId: number;
  offreId: number;
  score: number;
  resultat: string;
  competencesSatisfaites: string[];
  competencesManquantes: string[];
}

@Injectable({
  providedIn: 'root'
})
export class MatchingService {
  private readonly apiUrl = 'http://localhost:8082/api/matching';

  constructor(private http: HttpClient) {}

  classerCandidats(offreId: number): Observable<ResultatMatching[]> {
    return this.http.get<ResultatMatching[]>(
      `${this.apiUrl}/offre/${offreId}/classement`
    );
  }
}
