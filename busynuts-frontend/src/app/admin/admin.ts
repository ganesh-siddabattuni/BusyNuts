import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { ProductService } from '../services/product';
import { RawMaterialService } from '../services/raw-material';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin implements OnInit {
  userRole: string | null = '';
  activeTab: string = 'dashboard';
  
  // 1. Upgraded to Signals for instant reactivity!
  products = signal<any[]>([]);
  procurementQueue = signal<any[]>([]); 
  
  newProduct = { name: '', description: '', price: null, stockQuantity: null };

  constructor(
    private authService: AuthService, 
    private router: Router,
    private productService: ProductService,
    private rawMaterialService: RawMaterialService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    } else {
      this.userRole = this.authService.getUserRole();
      if (this.userRole !== 'ROLE_ADMIN') {
        this.authService.logout();
        this.router.navigate(['/login']);
      } else {
        this.loadProducts();
        this.loadProcurementQueue(); 
      }
    }
  }

  setTab(tabName: string): void {
    this.activeTab = tabName;
  }

  // --- PRODUCT INVENTORY METHODS ---
  loadProducts(): void {
    this.productService.getProducts().subscribe(data => {
      this.products.set(data); // 2. Update via Signal
    });
  }

  addProduct(): void {
    this.productService.createProduct(this.newProduct).subscribe({
      next: (savedProduct) => {
        // 3. Update the signal array instantly
        this.products.update(current => [...current, savedProduct]); 
        this.newProduct = { name: '', description: '', price: null, stockQuantity: null };
        alert('Product added successfully!');
      },
      error: (err) => alert('Failed to add product.')
    });
  }

  // --- PROCUREMENT QUEUE METHODS ---
  loadProcurementQueue(): void {
    this.rawMaterialService.getAllMaterials().subscribe(data => {
      this.procurementQueue.set(data); // 4. Update via Signal
    });
  }

  updateMaterialStatus(id: number, status: string): void {
    this.rawMaterialService.updateStatus(id, status).subscribe({
      next: () => {
        alert(`Batch marked as ${status}!`);
        this.loadProcurementQueue(); // This will now trigger an instant UI redraw
      },
      error: (err) => console.error('Failed to update status', err)
    });
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}