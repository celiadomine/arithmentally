import { Component, OnInit } from '@angular/core';
import { GameSession } from '../../data/game-session';
import { HistoryService } from '../../service/history.service'
import { MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-history',
  imports: [MatIcon, MatTableModule, CommonModule],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent implements OnInit {
  games: GameSession[] = [];
  displayedColumns = ['date', 'score', 'duration', 'actions'];

  constructor(private historyService: HistoryService) {}

  ngOnInit(): void {
    this.historyService.getPlayerGames(1).subscribe(g => this.games = g);
  }

  deleteGame(id: number): void {
    this.historyService.deleteGame(id).subscribe(() => {
      this.games = this.games.filter(g => g.id !== id);
    });
  }
}