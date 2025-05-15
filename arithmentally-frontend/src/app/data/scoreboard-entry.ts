import { GameSession } from './game-session';

export class ScoreboardEntry {
  id!: number;
  username: string = '';
  email: string = '';
  score: number = 0;
  duration: number = 0;
  date!: string;
  gameSession!: GameSession;
}
