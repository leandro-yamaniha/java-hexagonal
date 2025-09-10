import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-table-detail',
  template: `
    <div class="table-detail-container">
      <div class="header">
        <button mat-icon-button (click)="goBack()">
          <mat-icon>arrow_back</mat-icon>
        </button>
        <h1>Table Details</h1>
      </div>

      <mat-card class="detail-card">
        <mat-card-content>
          <div class="placeholder-content">
            <mat-icon class="placeholder-icon">table_restaurant</mat-icon>
            <h3>Table Detail View Coming Soon</h3>
            <p>This component will display comprehensive table information including:</p>
            <ul>
              <li>Table status and availability</li>
              <li>Current reservations</li>
              <li>Seating capacity</li>
              <li>Location in restaurant</li>
              <li>Service history</li>
              <li>Maintenance notes</li>
            </ul>
            <p><strong>Table ID:</strong> {{ tableId }}</p>
            
            <div class="sample-info">
              <div class="info-item">
                <mat-icon>people</mat-icon>
                <span>Capacity: 4 people</span>
              </div>
              <div class="info-item">
                <mat-icon>location_on</mat-icon>
                <span>Section: Main Dining</span>
              </div>
              <div class="info-item">
                <mat-icon>check_circle</mat-icon>
                <span>Status: Available</span>
              </div>
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .table-detail-container {
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

    .sample-info {
      display: flex;
      flex-direction: column;
      gap: 16px;
      margin-top: 30px;
      max-width: 300px;
      margin-left: auto;
      margin-right: auto;
    }

    .info-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px;
      background: #f5f5f5;
      border-radius: 8px;
      text-align: left;
    }

    .info-item mat-icon {
      color: #666;
    }

    .info-item span {
      font-weight: 500;
      color: #333;
    }
  `]
})
export class TableDetailComponent implements OnInit {
  tableId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.tableId = this.route.snapshot.paramMap.get('id');
  }

  goBack(): void {
    this.router.navigate(['/tables']);
  }
}
