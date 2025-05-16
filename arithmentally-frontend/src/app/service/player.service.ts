import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Player } from '../data/player';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PlayerProfileService {
  private backendUrl = environment.backendBaseUrl + 'player';

  constructor(private http: HttpClient) {}

  getProfile(): Observable<Player> {
    return this.http.get<Player>(`${this.backendUrl}/me`);
  }

  updateProfile(player: Player): Observable<Player> {
    return this.http.put<Player>(`${this.backendUrl}/${player.id}`, player);
  }
}