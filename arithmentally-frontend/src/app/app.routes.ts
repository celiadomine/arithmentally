import { Routes } from '@angular/router';
import { GameComponent } from './pages/game/game.component';
import { GameSetupComponent } from './pages/game-setup/game-setup.component';
import { HistoryComponent } from './pages/history/history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ScoreboardComponent } from './pages/scoreboard/scoreboard.component';
import { NoAccessComponent } from './pages/no-access/no-access.component';
import { appCanActivate } from './guard/app.auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'game-setup',
    pathMatch: 'full'
  },
  {
    path: 'game-setup',
    component: GameSetupComponent,
    data: {
        pagetitle: 'Game-Setup'
    }
  },
  {
    path: 'game',
    component: GameComponent,
    data: {
        pagetitle: 'Game'
    }
  },
  {
    path: 'history',
    component: HistoryComponent,
    data: {
      pagetitle: 'History'
    }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    data: {
      pagetitle: 'Profile'
    }
  },
  {
    path: 'scoreboard',
    component: ScoreboardComponent,
    data: {
      pagetitle: 'Scoreboard'
    }
  },
  {
    path: 'noaccess',
    component: NoAccessComponent
},
];
