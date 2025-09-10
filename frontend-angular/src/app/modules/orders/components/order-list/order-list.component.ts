import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-order-list',
  template: `
    <div class="order-list-container">
      <div class="header">
        <h1>Order Management</h1>
        <button mat-raised-button color="primary">
          <mat-icon>add</mat-icon>
          New Order
        </button>
      </div>

      <mat-card class="search-card">
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Search orders</mat-label>
            <input matInput placeholder="Search by order ID or customer">
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card class="table-card">
        <mat-card-content>
          <div class="placeholder-content">
            <mat-icon class="placeholder-icon">receipt</mat-icon>
            <h3>Order Management Coming Soon</h3>
            <p>This module will handle order creation, tracking, and management.</p>
            <p>Features will include:</p>
            <ul>
              <li>Create new orders</li>
              <li>Track order status</li>
              <li>Manage order items</li>
              <li>Customer order history</li>
              <li>Kitchen integration</li>
            </ul>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .order-list-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .header h1 {
      margin: 0;
      color: #333;
    }

    .header button mat-icon {
      margin-right: 8px;
    }

    .search-card {
      margin-bottom: 20px;
    }

    .search-field {
      width: 100%;
      max-width: 400px;
    }

    .table-card {
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
      margin-top: 20px;
    }

    .placeholder-content li {
      margin-bottom: 8px;
    }
  `]
})
export class OrderListComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
