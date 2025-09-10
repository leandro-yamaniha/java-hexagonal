import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <mat-toolbar color="primary">
      <mat-toolbar-row>
        <span>Restaurant Management System</span>
        <span class="spacer"></span>
        <button mat-button routerLink="/dashboard">Dashboard</button>
        <button mat-button routerLink="/customers">Customers</button>
        <button mat-button routerLink="/menu">Menu</button>
        <button mat-button routerLink="/orders">Orders</button>
        <button mat-button routerLink="/tables">Tables</button>
      </mat-toolbar-row>
    </mat-toolbar>
    
    <main class="main-content">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .spacer {
      flex: 1 1 auto;
    }
    
    .main-content {
      padding: 20px;
      min-height: calc(100vh - 64px);
      background-color: #f5f5f5;
    }
  `]
})
export class AppComponent {
  title = 'Restaurant Management System';
}
