import { GameSession } from './game-session';

export class Question {
  id!: number;
  task = '';
  correctAnswer = '';
  userAnswer = '';
  correct = false;
  timeTaken = 0;
  gameSession?: GameSession;
}
