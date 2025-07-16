import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private tokenKey = 'token';

  constructor() { }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  decodePayload(): any {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload = token.split('.')[1];
    const decodePayload = atob(payload);
    return JSON.parse(decodePayload);
  }

  getUserId(): number | null {
    const payload = this.decodePayload();
    return payload ? payload.id : null;
  }

  getUserRoles(): string[] | null {
    const payload = this.decodePayload();
    return payload ? payload.roles : null;
  }

  celarToken(): void {
    localStorage.removeItem(this.tokenKey);
  }


}
