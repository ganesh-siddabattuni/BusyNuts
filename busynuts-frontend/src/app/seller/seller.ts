import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { RawMaterialService } from '../services/raw-material';

@Component({
  selector: 'app-seller',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seller.html',
  styleUrl: './seller.css'
})
export class Seller implements OnInit {
  sellerUsername: string = '';
  
  // Signal to hold the seller's historical lots
  myLots = signal<any[]>([]);
  
  // Form object
  newLot = { materialType: 'Peanut Cake', weightInKg: null, askingPrice: null };

  constructor(
    private authService: AuthService,
    private rawMaterialService: RawMaterialService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn() || this.authService.getUserRole() !== 'ROLE_SELLER') {
      this.authService.logout();
      this.router.navigate(['/login']);
      return;
    }

    // Extract the username from local storage
    const userString = localStorage.getItem('currentUser');
    if (userString) {
      this.sellerUsername = JSON.parse(userString).username;
      this.loadMyLots();
    }
  }

  loadMyLots(): void {
    this.rawMaterialService.getMaterialsBySeller(this.sellerUsername).subscribe(data => {
      this.myLots.set(data);
    });
  }

  submitLot(): void {
    // Attach the username to the payload before sending to Spring Boot
    const payload = { ...this.newLot, sellerUsername: this.sellerUsername };
    
    this.rawMaterialService.submitMaterial(payload).subscribe({
      next: () => {
        alert('Batch submitted successfully! Waiting for Admin approval.');
        this.newLot = { materialType: 'Peanut Cake', weightInKg: null, askingPrice: null }; // Reset form
        this.loadMyLots(); // Refresh the table
      },
      error: (err) => console.error(err)
    });
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}