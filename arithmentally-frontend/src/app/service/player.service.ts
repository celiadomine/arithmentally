import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Player } from '../models/player';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PlayerService {
  private backendUrl = environment.backendBaseUrl + 'players';

  constructor(private http: HttpClient) {}

  getOrCreatePlayer(): Observable<Player> {
    return this.http.get<Player>(`${this.backendUrl}/me`);
  }

  updatePlayer(player: Player): Observable<Player> {
    return this.http.put<Player>(`${this.backendUrl}/${player.id}`, player);
  }

  deletePlayer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/${id}`);
  }
}

