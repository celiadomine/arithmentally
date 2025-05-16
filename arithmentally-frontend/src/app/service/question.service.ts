import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Question } from '../../app/data/question';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class QuestionService {
  private backendUrl = environment.backendBaseUrl + 'questions';

  constructor(private http: HttpClient) {}

  processAnswer(question: Question, userAnswer: string): Observable<Question> {
    return this.http.post<Question>(`${this.backendUrl}/${question.id}/answer`, { userAnswer });
  }
}
