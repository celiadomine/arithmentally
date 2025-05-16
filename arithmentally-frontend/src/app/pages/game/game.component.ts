import { Component, OnInit } from '@angular/core';
import { GameSession } from '../../data/game-session';
import { Question } from '../../data/question';
import { GameService } from '../../service/game.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-game',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss'
})


export class GameComponent implements OnInit {
  currentQuestion!: Question;
  currentIndex = 0;
  totalQuestions = 0;
  userAnswer = new FormControl('');
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
    const answer: string = (this.userAnswer.value ?? '').trim();

  
    this.currentQuestion.userAnswer = answer;
    this.currentQuestion.correct = this.currentQuestion.correctAnswer === answer;
  
    this.userAnswer.reset(); 
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
