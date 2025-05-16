import { Player } from './player';
import { RoundConfiguration } from './round-configuration';
import { Question } from './question';

export class GameSession {
  id!: number;
  date!: string;
  score = 0;             
  duration = 0;        
  player!: Player;
  configuration!: RoundConfiguration;
  questions: Question[] = [];
}
