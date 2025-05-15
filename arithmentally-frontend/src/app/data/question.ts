import { GameSession } from './game-session';

export class Question {
  id!: number;
  task: string = '';
  correctAnswer: string = '';
  userAnswer: string = '';
  correct: boolean = false;
  timeTaken: number = 0;
  gameSession?: GameSession;
}
