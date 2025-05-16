import { Component, OnInit } from '@angular/core';
import { GameSession } from '../../data/game-session';
import { GameService } from '../../service/game.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Question } from '../../data/question';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss'
})
export class GameComponent implements OnInit {
  
  userAnswer = new FormControl('');
  game!: GameSession;
  currentQuestion!: Question;
  
  totalQuestions = 0;

  constructor(
    public gameService: GameService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.game = history.state.game;
    this.totalQuestions = this.game.questions.length;
    this.gameService.game = this.game;
    this.gameService.startTime = Date.now();
    this.currentQuestion = this.gameService.getCurrentQuestion();
  }

  submitAnswer(): void {
    const answer: string = (this.userAnswer.value ?? '').trim();
    this.gameService.submitAnswer(answer);
    this.userAnswer.reset();

    if (this.gameService.isGameFinished()) {
      this.finishGame();
    } else {
      this.currentQuestion = this.gameService.getCurrentQuestion();
    }
  }

  finishGame(): void {
    const score = this.gameService.getScore();
    const duration = this.gameService.getDuration();

    this.dialog.open(ConfirmDialogComponent, {
      data: {
        score,
        total: this.totalQuestions,
        time: duration
      }
    }).afterClosed().subscribe(() => {
      this.router.navigate(['/history']);
    });
  }
}
