import { GameSession } from './game-session';

export class ScoreboardEntry {
  id!: number;
  username = '';
  email = '';
  score = 0;
  duration = 0;
  date!: string;
  gameSession!: GameSession;
}
