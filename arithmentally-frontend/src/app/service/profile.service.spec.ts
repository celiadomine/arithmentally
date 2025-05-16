import { TestBed } from '@angular/core/testing';
import { PlayerProfileService } from './player.service';
import { provideHttpClient, HttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Player } from '../data/player';

describe('PlayerProfileService', () => {
  let service: PlayerProfileService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PlayerProfileService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(PlayerProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should send PUT request to update nickname', () => {
    const player: Player = {
      id: 1,
      keycloakId: 'abc',
      name: 'hihihi',
      email: 'test@example.com',
      role: 'USER'
    };

    service.updateProfile(player).subscribe(updated => {
      expect(updated.name).toBe('hihihi');
    });

    const req = httpMock.expectOne(`http://localhost:9090/api/player/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body.name).toBe('hihihi');
    req.flush(player); // simulate server response
  });
});
