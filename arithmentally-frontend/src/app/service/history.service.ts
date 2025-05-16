import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { GameSession } from '../data/game-session';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class HistoryService {
  private backendUrl = environment.backendBaseUrl + 'games';

  constructor(private http: HttpClient) {}

  getPlayerGames(playerId: number): Observable<GameSession[]> {
    return this.http.get<GameSession[]>(`${this.backendUrl}/player/${playerId}`);
  }

  deleteGame(id: number): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/${id}`);
  }
}
