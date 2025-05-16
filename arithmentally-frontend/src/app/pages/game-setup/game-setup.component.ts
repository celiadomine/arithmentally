import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInput} from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatButton } from '@angular/material/button';
import { MatError } from '@angular/material/form-field';
import { MatLabel } from '@angular/material/form-field';
import { MatOption } from '@angular/material/select';
import { environment } from '../../../environments/environment';

@Component({
  standalone: true,
  selector: 'app-game-setup',
  templateUrl: './game-setup.component.html',
  styleUrl: './game-setup.component.scss',
  imports: [ CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInput, MatSelect, MatButton, MatError, MatLabel, MatOption ],
})

export class GameSetupComponent {
  gameForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.gameForm = this.fb.group({
      min: [],
      max: [],
      operations: [],
      amount: []
    });
  }

  startGame() {
    if (this.gameForm.valid) {
      const formValues = this.gameForm.value;

      const payload = {
        operations: formValues.operations.join(','), // Convert to string
        minRange: formValues.min,
        maxRange: formValues.max,
        numberOfQuestions: formValues.amount
      };

      this.http.post(environment.backendBaseUrl + 'games/start', payload).subscribe({
        next: res => console.log('Game started!', res),
        error: err => console.error('Game start failed', err)
      });
    }
  }
}
