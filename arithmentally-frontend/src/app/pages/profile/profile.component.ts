import { Component, OnInit } from '@angular/core';
import { AppAuthService } from '../../service/app.auth.service';
import { PlayerService } from '../../service/player.service';
import { MatFormField } from '@angular/material/form-field';
import { MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  imports: [MatFormField, MatLabel, MatInput, FormsModule],
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
