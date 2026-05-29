import { Component, OnInit, signal } from '@angular/core';
import { ProductService } from '../services/product';
import { CartService } from '../services/cart';

@Component({
  selector: 'app-consumer',
  standalone: true,
  templateUrl: './consumer.html',
  styleUrl: './consumer.css'
})
export class Consumer implements OnInit {
  // 1. Convert the products array into a Signal
  products = signal<any[]>([]);

  constructor(
    private productService: ProductService,
    public cartService: CartService // 2. Keep this public so the HTML can read the Cart Signal directly
  ) {}

  ngOnInit(): void {
    // Fetch products from Spring Boot
    this.productService.getProducts().subscribe(data => {
      // 3. .set() updates the Signal, which instantly updates the UI!
      this.products.set(data); 
    });
  }

  onAddToCart(product: any) {
    this.cartService.addToCart(product);
  }
}