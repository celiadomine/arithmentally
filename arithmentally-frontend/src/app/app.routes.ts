import { Routes } from '@angular/router';
import { GameComponent } from './pages/game/game.component';
import { GameSetupComponent } from './pages/game-setup/game-setup.component';
import { HistoryComponent } from './pages/history/history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ScoreboardComponent } from './pages/scoreboard/scoreboard.component';
import { NoAccessComponent } from './pages/no-access/no-access.component';
import { appCanActivate } from './guard/app.auth.guard';
import { AppRoles } from './app.roles';

export const routes: Routes = [
  {
    path: '',
    component: GameSetupComponent,
  },
  {
    path: 'game-setup',
    component: GameSetupComponent,
    canActivate: [appCanActivate], 
    data: {
        roles: [AppRoles.Read],
        pagetitle: 'Game-Setup'
    }
  },
  {
    path: 'game',
    component: GameComponent,
    canActivate: [appCanActivate], 
    data: {
      roles: [AppRoles.Read],
        pagetitle: 'Game'
    }
  },
  {
    path: 'history',
    component: HistoryComponent,
    canActivate: [appCanActivate], 
    data: {
      roles: [AppRoles.Read],
      pagetitle: 'History'
    }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [appCanActivate], 
    data: {
      roles: [AppRoles.Update],
      pagetitle: 'Profile'
    }
  },
  {
    path: 'scoreboard',
    component: ScoreboardComponent,
    canActivate: [appCanActivate], 
    data: {
      roles: [AppRoles.Admin],
      pagetitle: 'Scoreboard'
    }
  },
  {
    path: 'noaccess',
    component: NoAccessComponent
},
];
