import { Injectable, signal, computed } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  // 1. Create a Signal to hold the cart array
  cartItems = signal<any[]>([]);

  // 2. Create a Computed Signal that automatically calculates the count
  cartCount = computed(() => this.cartItems().length);

  addToCart(product: any) {
    // 3. .update() instantly pushes the new data and tells the HTML to redraw
    this.cartItems.update(items => [...items, product]);
    alert(`${product.name} added to cart!`);
  }
}