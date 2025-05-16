import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ScoreboardEntry } from '../data/scoreboard-entry';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ScoreboardService {
  private backendUrl = environment.backendBaseUrl + 'scoreboard';

  constructor(private http: HttpClient) {}

  getTop10(): Observable<ScoreboardEntry[]> {
    return this.http.get<ScoreboardEntry[]>(`${this.backendUrl}/top10`);
  }

  saveEntry(entry: ScoreboardEntry): Observable<ScoreboardEntry> {
    return this.http.post<ScoreboardEntry>(this.backendUrl, entry);
  }
}