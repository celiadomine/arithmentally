import { Component } from '@angular/core';
import { AppAuthService } from '../service/app.auth.service';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})

export class ProfileComponent implements OnInit {
  username = '';
  email = '';
  nickname = '';

constructor(private authService: AppAuthService, private playerService: PlayerService) {}

  ngOnInit(): void {
    this.authService.usernameObservable.subscribe(name => this.username = name);
    this.authService.useraliasObservable.subscribe(email => this.email = email);
    this.playerService.getOrCreatePlayer().subscribe(p => this.nickname = p.name);
  }

  updateProfile(): void {
    this.playerService.getOrCreatePlayer().subscribe(player => {
      player.name = this.nickname;
      this.playerService.updatePlayer(player).subscribe(() => {
        alert('Nickname updated!');
      });
    });
  }
}
