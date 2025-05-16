import { Component, OnInit } from '@angular/core';
import { ScoreboardService } from '../../service/scoreboard.service';
import { ScoreboardEntry } from '../../data/scoreboard-entry';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { AppRoles } from '../../app.roles';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-scoreboard',
  standalone: true,
  imports: [MatIcon, MatTableModule, CommonModule],
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.scss']
})
export class ScoreboardComponent implements OnInit {
  roles = AppRoles;
  displayedColumns = ['username', 'score', 'duration', 'date', 'actions'];
  entriesDataSource = new MatTableDataSource<ScoreboardEntry>();

  constructor(private scoreboardService: ScoreboardService) {}

  ngOnInit(): void {
    this.reloadData();
  }

  reloadData(): void {
    this.scoreboardService.getTop10().subscribe(entries => {
      this.entriesDataSource.data = entries;
    });
  }

  //public deleteEntry(id: number): Observable<HttpResponse<string>> {
  //  return this.http.delete<string>(environment.backendBaseUrl + 'scoreboard/' + id, {
  //    observe: 'response'
  //  });
  //}
  
}
