import { Routes } from '@angular/router';
import { GameComponent } from './pages/game/game.component';
import { GameSetupComponent } from './pages/game-setup/game-setup.component';
import { HistoryComponent } from './pages/history/history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ScoreboardComponent } from './pages/scoreboard/scoreboard.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'game-setup',
    pathMatch: 'full'
  },
  {
    path: 'game-setup',
    component: GameSetupComponent
  },
  {
    path: 'game',
    component: GameComponent
  },
  {
    path: 'history',
    component: HistoryComponent,
    data: {
      pagetitle: 'Spielverlauf'
    }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    data: {
      pagetitle: 'Profil'
    }
  },
  {
    path: 'scoreboard',
    component: ScoreboardComponent,
    data: {
      pagetitle: 'Rangliste'
    }
  }
];
