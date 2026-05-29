import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RawMaterialService {
  private apiUrl = 'http://localhost:8080/api/raw-materials';

  constructor(private http: HttpClient) { }

  // Admin uses this to see the whole queue
  getAllMaterials(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Seller uses this to see ONLY their own submissions
  getMaterialsBySeller(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/seller/${username}`);
  }

  // Seller uses this to submit a new batch
  submitMaterial(material: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, material);
  }

  // Admin uses this to Approve/Reject a batch
  updateStatus(id: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/status`, status, { responseType: 'text' });
  }
}