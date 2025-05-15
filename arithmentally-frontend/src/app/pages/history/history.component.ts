import { Component } from '@angular/core';
import { GameService } from '../services/game.service';
import { GameSession } from '../models/game-session';

@Component({
  selector: 'app-history',
  imports: [],
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