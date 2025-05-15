import { Player } from './player';
import { RoundConfiguration } from './round-configuration';
import { Question } from './question';

export class GameSession {
  id!: number;
  date!: string;
  score: number = 0;
  duration: number = 0;
  player!: Player;
  configuration!: RoundConfiguration;
  questions: Question[] = [];
}
