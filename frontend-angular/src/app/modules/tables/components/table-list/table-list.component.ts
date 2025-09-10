import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-table-list',
  template: `
    <div class="table-list-container">
      <div class="header">
        <h1>Table Management</h1>
        <button mat-raised-button color="primary">
          <mat-icon>add</mat-icon>
          Add Table
        </button>
      </div>

      <mat-card class="filters-card">
        <mat-card-content>
          <div class="filters-row">
            <mat-form-field appearance="outline" class="search-field">
              <mat-label>Search tables</mat-label>
              <input matInput placeholder="Search by table number">
              <mat-icon matSuffix>search</mat-icon>
            </mat-form-field>

            <mat-form-field appearance="outline" class="status-field">
              <mat-label>Filter by Status</mat-label>
              <mat-select>
                <mat-option value="">All Tables</mat-option>
                <mat-option value="available">Available</mat-option>
                <mat-option value="occupied">Occupied</mat-option>
                <mat-option value="reserved">Reserved</mat-option>
                <mat-option value="cleaning">Cleaning</mat-option>
              </mat-select>
            </mat-form-field>
          </div>
        </mat-card-content>
      </mat-card>

      <mat-card class="grid-card">
        <mat-card-content>
          <div class="placeholder-content">
            <mat-icon class="placeholder-icon">table_restaurant</mat-icon>
            <h3>Table Management Coming Soon</h3>
            <p>This module will provide comprehensive table management including:</p>
            <ul>
              <li>Visual table layout</li>
              <li>Real-time table status</li>
              <li>Reservation management</li>
              <li>Table assignments</li>
              <li>Capacity tracking</li>
              <li>Cleaning schedules</li>
            </ul>
            
            <div class="sample-grid">
              <div class="sample-table available">
                <mat-icon>table_restaurant</mat-icon>
                <span>Table 1</span>
                <mat-chip color="primary" selected>Available</mat-chip>
              </div>
              <div class="sample-table occupied">
                <mat-icon>table_restaurant</mat-icon>
                <span>Table 2</span>
                <mat-chip color="warn" selected>Occupied</mat-chip>
              </div>
              <div class="sample-table reserved">
                <mat-icon>table_restaurant</mat-icon>
                <span>Table 3</span>
                <mat-chip color="accent" selected>Reserved</mat-chip>
              </div>
              <div class="sample-table cleaning">
                <mat-icon>table_restaurant</mat-icon>
                <span>Table 4</span>
                <mat-chip selected>Cleaning</mat-chip>
              </div>
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .table-list-container {
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

    .filters-card {
      margin-bottom: 20px;
    }

    .filters-row {
      display: grid;
      grid-template-columns: 2fr 1fr;
      gap: 20px;
      align-items: center;
    }

    .search-field, .status-field {
      width: 100%;
    }

    .grid-card {
      margin-bottom: 20px;
    }

    .placeholder-content {
      text-align: center;
      padding: 40px 20px;
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

    .sample-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-top: 40px;
      max-width: 800px;
      margin-left: auto;
      margin-right: auto;
    }

    .sample-table {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      background: white;
      transition: transform 0.2s;
    }

    .sample-table:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }

    .sample-table mat-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      margin-bottom: 8px;
      color: #666;
    }

    .sample-table span {
      font-weight: 600;
      margin-bottom: 8px;
      color: #333;
    }

    .sample-table.available {
      border-color: #4caf50;
    }

    .sample-table.occupied {
      border-color: #f44336;
    }

    .sample-table.reserved {
      border-color: #ff9800;
    }

    .sample-table.cleaning {
      border-color: #9e9e9e;
    }

    @media (max-width: 768px) {
      .filters-row {
        grid-template-columns: 1fr;
        gap: 16px;
      }
      
      .sample-grid {
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 15px;
      }
    }
  `]
})
export class TableListComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
