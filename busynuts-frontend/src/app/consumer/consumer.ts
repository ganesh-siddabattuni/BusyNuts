import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../services/product';
import { CartService } from '../services/cart';
import { OrderService } from '../services/order';

@Component({
  selector: 'app-consumer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consumer.html',
  styleUrl: './consumer.css'
})
export class Consumer implements OnInit {
  products = signal<any[]>([]);
  
  // Cart UI State
  isCartOpen = signal<boolean>(false);
  customer = { name: '', address: '' };

  constructor(
    private productService: ProductService,
    public cartService: CartService,
    private orderService: OrderService // <-- Inject here
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe(data => {
      this.products.set(data);
    });
  }

  onAddToCart(product: any): void {
    this.cartService.addToCart(product);
  }

  toggleCart(): void {
    this.isCartOpen.set(!this.isCartOpen());
  }

  getCartTotal(): number {
    return this.cartService.cartItems().reduce((total, item) => total + item.price, 0);
  }

  checkout(): void {
    const items = this.cartService.cartItems();
    if (items.length === 0) return;

    // 1. Group items to calculate quantities (turns [Item A, Item A] into {productId: A, quantity: 2})
    const groupedItems = items.reduce((acc, current) => {
      const existing = acc.find((item: any) => item.productId === current.id);
      if (existing) {
        existing.quantity += 1;
      } else {
        acc.push({ productId: current.id, quantity: 1 });
      }
      return acc;
    }, []);

    // 2. Build the payload exactly as the Spring Boot DTO expects
    const payload = {
      customerName: this.customer.name,
      shippingAddress: this.customer.address,
      cartItems: groupedItems
    };

    // 3. Send it to the backend
    this.orderService.placeOrder(payload).subscribe({
      next: (response) => {
        alert(`Success! Order #${response.id} has been placed.`);
        this.cartService.cartItems.set([]); // Empty the cart
        this.customer = { name: '', address: '' }; // Clear the form
        this.toggleCart(); // Close the cart panel
        this.loadProducts(); // Refresh products to show the updated (reduced) stock!
      },
      error: (err) => {
        alert(err.error || 'Checkout failed due to insufficient stock.');
      }
    });
  }
}