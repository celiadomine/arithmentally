import { Component } from '@angular/core';
import { ScoreboardService } from '../services/scoreboard.service';
import { ScoreboardEntry } from '../models/scoreboard-entry';

@Component({
  selector: 'app-scoreboard',
  imports: [],
  templateUrl: './scoreboard.component.html',
  styleUrl: './scoreboard.component.scss'
})
export class ScoreboardComponent implements OnInit {
  entries: ScoreboardEntry[] = [];
  displayedColumns = ['username', 'score', 'duration', 'date', 'actions'];

  constructor(private scoreboardService: ScoreboardService) {}

  ngOnInit(): void {
    this.scoreboardService.getTop10().subscribe(e => this.entries = e);
  }

  deleteEntry(id: number): void {
    this.scoreboardService.deleteEntry(id).subscribe(() => {
      this.entries = this.entries.filter(e => e.id !== id);
    });
  }
}
