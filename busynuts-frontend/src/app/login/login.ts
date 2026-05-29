import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  credentials = { username: '', password: '' };
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.credentials).subscribe({
      next: (user) => {
        // The backend returned the user object. Let's check their role and route them!
        if (user.role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        } else if (user.role === 'ROLE_SELLER') {
          this.router.navigate(['/seller']);
        } else {
          this.router.navigate(['/consumer']);
        }
      },
      error: (err) => {
        // If Spring Boot returns a 401 Unauthorized, show this error
        this.errorMessage = 'Invalid username or password';
      }
    });
  }
}