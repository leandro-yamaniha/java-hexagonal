import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-order-detail',
  template: `
    <div class="order-detail-container">
      <div class="header">
        <button mat-icon-button (click)="goBack()">
          <mat-icon>arrow_back</mat-icon>
        </button>
        <h1>Order Details</h1>
      </div>

      <mat-card class="detail-card">
        <mat-card-content>
          <div class="placeholder-content">
            <mat-icon class="placeholder-icon">receipt_long</mat-icon>
            <h3>Order Detail View Coming Soon</h3>
            <p>This component will display detailed order information including:</p>
            <ul>
              <li>Order items and quantities</li>
              <li>Customer information</li>
              <li>Order status and timeline</li>
              <li>Payment details</li>
              <li>Kitchen notes</li>
            </ul>
            <p><strong>Order ID:</strong> {{ orderId }}</p>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .order-detail-container {
      max-width: 800px;
      margin: 0 auto;
      padding: 20px;
    }

    .header {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
      gap: 16px;
    }

    .header h1 {
      margin: 0;
      color: #333;
    }

    .detail-card {
      margin-bottom: 20px;
    }

    .placeholder-content {
      text-align: center;
      padding: 60px 20px;
      color: #666;
    }

    .placeholder-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      color: #ccc;
      margin-bottom: 20px;
    }

    .placeholder-content h3 {
      margin-bottom: 16px;
      color: #333;
    }

    .placeholder-content ul {
      text-align: left;
      display: inline-block;
      margin: 20px 0;
    }

    .placeholder-content li {
      margin-bottom: 8px;
    }
  `]
})
export class OrderDetailComponent implements OnInit {
  orderId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.orderId = this.route.snapshot.paramMap.get('id');
  }

  goBack(): void {
    this.router.navigate(['/orders']);
  }
}
