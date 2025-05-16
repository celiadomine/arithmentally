import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { GameSession } from '../data/game-session';
import { Observable } from 'rxjs';

export interface GameSetupPayload {
  operations: string;
  minRange: number;
  maxRange: number;
  numberOfQuestions: number;
}

@Injectable({ providedIn: 'root' })
export class GameSetupService {
  private backendUrl = environment.backendBaseUrl + 'games';

  constructor(private http: HttpClient) {}

  startGame(payload: GameSetupPayload): Observable<GameSession> {
    return this.http.post<GameSession>(`${this.backendUrl}/start`, payload);
  }
}