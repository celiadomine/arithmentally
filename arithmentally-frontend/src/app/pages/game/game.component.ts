import { Component } from '@angular/core';
import { GameSession } from '../models/game-session';
import { Question } from '../models/question';
import { GameService } from '../services/game.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-game',
  imports: [],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss'
})

export class GameComponent implements OnInit {
  currentQuestion!: Question;
  currentIndex = 0;
  totalQuestions = 0;
  userAnswer = '';
  game!: GameSession;
  startTime!: number;

  constructor(
    private gameService: GameService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.game = history.state.game;
    this.totalQuestions = this.game.questions.length;
    this.currentQuestion = this.game.questions[this.currentIndex];
    this.startTime = Date.now();
  }

  submitAnswer(): void {
    this.currentQuestion.userAnswer = this.userAnswer;
    this.currentQuestion.correct = this.currentQuestion.correctAnswer === this.userAnswer.trim();
    this.userAnswer = '';
    this.currentIndex++;

    if (this.currentIndex < this.totalQuestions) {
      this.currentQuestion = this.game.questions[this.currentIndex];
    } else {
      this.finishGame();
    }
  }

  finishGame(): void {
    const correctAnswers = this.game.questions.filter(q => q.correct).length;
    const duration = Math.floor((Date.now() - this.startTime) / 1000);

    this.dialog.open(ConfirmDialogComponent, {
      data: {
        score: correctAnswers,
        total: this.totalQuestions,
        time: duration
      }
    }).afterClosed().subscribe(() => {
      this.router.navigate(['/history']);
    });
  }
}
