import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { GameSession } from '../data/game-session';
import { RoundConfiguration } from '../data/round-configuration';
import { Observable } from 'rxjs';
import { Question } from '../data/question';

@Injectable({ providedIn: 'root' })
export class GameService {
  private backendUrl = environment.backendBaseUrl + 'games';
  public game!: GameSession;
  public currentIndex = 0;
  public startTime!: number;

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

  getCurrentQuestion(): Question {
    return this.game.questions[this.currentIndex];
  }

  submitAnswer(answer: string): boolean {
    const currentQuestion = this.getCurrentQuestion();
    currentQuestion.userAnswer = answer;
    currentQuestion.correct = currentQuestion.correctAnswer === answer.trim();
    this.currentIndex++;
    return currentQuestion.correct;
  }

  isGameFinished(): boolean {
    return this.currentIndex >= this.game.questions.length;
  }

  getScore(): number {
    return this.game.questions.filter(q => q.correct).length;
  }

  getDuration(): number {
    return Math.floor((Date.now() - this.startTime) / 1000);
  }
} 