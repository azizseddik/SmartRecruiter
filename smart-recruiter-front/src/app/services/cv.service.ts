
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CvService {
  private readonly apiUrl =
    'http://localhost:8082/api/cv/importer-candidature';

  constructor(private http: HttpClient) {}

  importerCVPourOffre(file: File, offreId: number): Observable<unknown> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('offreId', String(offreId));

    return this.http.post<unknown>(this.apiUrl, formData);
  }
}
