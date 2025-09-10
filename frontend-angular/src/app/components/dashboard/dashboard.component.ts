import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Customer } from '../../models/customer.model';
import { MenuItem } from '../../models/menu.model';
import * as CustomerActions from '../../store/customer/customer.actions';
import * as MenuActions from '../../store/menu/menu.actions';

@Component({
  selector: 'app-dashboard',
  template: `
    <div class="dashboard-container">
      <h1>Restaurant Dashboard</h1>
      
      <div class="stats-grid">
        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>people</mat-icon>
              Total Customers
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="stat-number">{{ (customers$ | async)?.length || 0 }}</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>restaurant_menu</mat-icon>
              Menu Items
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="stat-number">{{ (menuItems$ | async)?.length || 0 }}</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>check_circle</mat-icon>
              Available Items
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="stat-number">{{ getAvailableItemsCount() }}</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>person_add</mat-icon>
              Active Customers
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="stat-number">{{ getActiveCustomersCount() }}</div>
          </mat-card-content>
        </mat-card>
      </div>

      <div class="quick-actions">
        <h2>Quick Actions</h2>
        <div class="action-buttons">
          <button mat-raised-button color="primary" routerLink="/customers">
            <mat-icon>people</mat-icon>
            Manage Customers
          </button>
          <button mat-raised-button color="accent" routerLink="/menu">
            <mat-icon>restaurant_menu</mat-icon>
            Manage Menu
          </button>
          <button mat-raised-button color="warn" routerLink="/orders">
            <mat-icon>receipt</mat-icon>
            View Orders
          </button>
          <button mat-raised-button routerLink="/tables">
            <mat-icon>table_restaurant</mat-icon>
            Manage Tables
          </button>
        </div>
      </div>

      <div class="recent-activity">
        <h2>Recent Activity</h2>
        <mat-card>
          <mat-card-content>
            <p>Recent customers and menu updates will appear here...</p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }

    h1 {
      color: #333;
      margin-bottom: 30px;
      text-align: center;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin-bottom: 40px;
    }

    .stat-card {
      text-align: center;
      transition: transform 0.2s;
    }

    .stat-card:hover {
      transform: translateY(-2px);
    }

    .stat-card mat-card-title {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      font-size: 16px;
    }

    .stat-number {
      font-size: 48px;
      font-weight: bold;
      color: #3f51b5;
      margin-top: 10px;
    }

    .quick-actions {
      margin-bottom: 40px;
    }

    .quick-actions h2 {
      color: #333;
      margin-bottom: 20px;
    }

    .action-buttons {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 15px;
    }

    .action-buttons button {
      height: 60px;
      font-size: 16px;
    }

    .action-buttons button mat-icon {
      margin-right: 8px;
    }

    .recent-activity h2 {
      color: #333;
      margin-bottom: 20px;
    }

    .recent-activity mat-card {
      min-height: 200px;
    }
  `]
})
export class DashboardComponent implements OnInit {
  customers$: Observable<Customer[]>;
  menuItems$: Observable<MenuItem[]>;

  constructor(private store: Store) {
    this.customers$ = this.store.select(state => (state as any).customers.customers);
    this.menuItems$ = this.store.select(state => (state as any).menu.menuItems);
  }

  ngOnInit(): void {
    this.store.dispatch(CustomerActions.loadCustomers({ page: 0, size: 100 }));
    this.store.dispatch(MenuActions.loadMenuItems({ page: 0, size: 100 }));
  }

  getAvailableItemsCount(): number {
    let count = 0;
    this.menuItems$.subscribe(items => {
      count = items?.filter(item => item.available).length || 0;
    }).unsubscribe();
    return count;
  }

  getActiveCustomersCount(): number {
    let count = 0;
    this.customers$.subscribe(customers => {
      count = customers?.filter(customer => customer.active).length || 0;
    }).unsubscribe();
    return count;
  }
}
