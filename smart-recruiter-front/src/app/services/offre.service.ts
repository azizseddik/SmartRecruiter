
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Offre {
  id: number;
  titre: string;
  description?: string;
  domaine?: string;
  experienceMin?: number;
}

@Injectable({
  providedIn: 'root'
})
export class OffreService {
  private readonly apiUrl = 'http://localhost:8082/api/offres';

  constructor(private http: HttpClient) {}

  getOffres(): Observable<Offre[]> {
    return this.http.get<Offre[]>(this.apiUrl);
  }
}
