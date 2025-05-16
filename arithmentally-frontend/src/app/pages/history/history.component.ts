import { Component, OnInit } from '@angular/core';
import { GameService } from '../../service/game.service';
import { GameSession } from '../../data/game-session';
import { MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';

import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-history',
  imports: [MatIcon, MatTableModule,  CommonModule],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})

export class HistoryComponent implements OnInit {
  games: GameSession[] = [];
  displayedColumns = ['date', 'score', 'duration', 'actions'];

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    // replace with actual player ID logic
    this.gameService.getPlayerGames(1).subscribe(g => this.games = g);
  }

  deleteGame(id: number): void {
    this.gameService.deleteGame(id).subscribe(() => {
      this.games = this.games.filter(g => g.id !== id);
    });
  }
}