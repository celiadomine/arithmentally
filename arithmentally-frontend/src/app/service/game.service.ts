import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { GameSession } from '../models/game-session';
import { RoundConfiguration } from '../models/round-configuration';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GameService {
  private backendUrl = environment.backendBaseUrl + 'games';

  constructor(private http: HttpClient) {}

  startNewGame(config: RoundConfiguration): Observable<GameSession> {
    return this.http.post<GameSession>(`${this.backendUrl}/start`, config);
  }

  getPlayerGames(playerId: number): Observable<GameSession[]> {
    return this.http.get<GameSession[]>(`${this.backendUrl}/player/${playerId}`);
  }

  updateScore(gameId: number, score: number, duration: number): Observable<GameSession> {
    return this.http.put<GameSession>(`${this.backendUrl}/${gameId}/score`, { score, duration });
  }

  deleteGame(id: number): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/${id}`);
  }
}
