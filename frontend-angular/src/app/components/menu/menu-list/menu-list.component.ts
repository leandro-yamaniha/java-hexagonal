import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { MenuItem, MenuCategory } from '../../../models/menu.model';
import { MenuFormComponent } from '../menu-form/menu-form.component';
import * as MenuActions from '../../../store/menu/menu.actions';

@Component({
  selector: 'app-menu-list',
  template: `
    <div class="menu-list-container">
      <div class="header">
        <h1>Menu Management</h1>
        <button mat-raised-button color="primary" (click)="openCreateDialog()">
          <mat-icon>add</mat-icon>
          Add Menu Item
        </button>
      </div>

      <mat-card class="filters-card">
        <mat-card-content>
          <div class="filters-row">
            <mat-form-field appearance="outline" class="search-field">
              <mat-label>Search menu items</mat-label>
              <input matInput (keyup)="applyFilter($event)" placeholder="Search by name">
              <mat-icon matSuffix>search</mat-icon>
            </mat-form-field>

            <mat-form-field appearance="outline" class="category-field">
              <mat-label>Filter by Category</mat-label>
              <mat-select (selectionChange)="filterByCategory($event.value)">
                <mat-option value="">All Categories</mat-option>
                <mat-option *ngFor="let category of categories" [value]="category">
                  {{ formatCategoryName(category) }}
                </mat-option>
              </mat-select>
            </mat-form-field>

            <mat-form-field appearance="outline" class="availability-field">
              <mat-label>Availability</mat-label>
              <mat-select (selectionChange)="filterByAvailability($event.value)">
                <mat-option value="">All Items</mat-option>
                <mat-option value="available">Available Only</mat-option>
                <mat-option value="unavailable">Unavailable Only</mat-option>
              </mat-select>
            </mat-form-field>
          </div>
        </mat-card-content>
      </mat-card>

      <mat-card class="table-card">
        <mat-card-content>
          <div class="table-container">
            <table mat-table [dataSource]="dataSource" matSort class="menu-table">
              
              <ng-container matColumnDef="name">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Name</th>
                <td mat-cell *matCellDef="let item">
                  <div class="item-name">
                    <strong>{{ item.name }}</strong>
                    <div class="item-description" *ngIf="item.description">
                      {{ item.description }}
                    </div>
                  </div>
                </td>
              </ng-container>

              <ng-container matColumnDef="category">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Category</th>
                <td mat-cell *matCellDef="let item">
                  <mat-chip color="accent" selected>
                    {{ formatCategoryName(item.category) }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="price">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Price</th>
                <td mat-cell *matCellDef="let item">
                  <span class="price">{{ item.price.amount | currency:item.price.currency }}</span>
                </td>
              </ng-container>

              <ng-container matColumnDef="preparationTime">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Prep Time</th>
                <td mat-cell *matCellDef="let item">{{ item.preparationTimeMinutes }} min</td>
              </ng-container>

              <ng-container matColumnDef="available">
                <th mat-header-cell *matHeaderCellDef>Status</th>
                <td mat-cell *matCellDef="let item">
                  <mat-chip [color]="item.available ? 'primary' : 'warn'" selected>
                    {{ item.available ? 'Available' : 'Unavailable' }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="actions">
                <th mat-header-cell *matHeaderCellDef>Actions</th>
                <td mat-cell *matCellDef="let item">
                  <button mat-icon-button color="primary" (click)="openEditDialog(item)" matTooltip="Edit">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button 
                          [color]="item.available ? 'warn' : 'primary'"
                          (click)="toggleAvailability(item)"
                          [matTooltip]="item.available ? 'Make Unavailable' : 'Make Available'">
                    <mat-icon>{{ item.available ? 'visibility_off' : 'visibility' }}</mat-icon>
                  </button>
                  <button mat-icon-button color="warn" (click)="deleteMenuItem(item)" matTooltip="Delete">
                    <mat-icon>delete</mat-icon>
                  </button>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
            </table>

            <mat-paginator [pageSizeOptions]="[5, 10, 20]" showFirstLastButtons></mat-paginator>
          </div>
        </mat-card-content>
      </mat-card>

      <div *ngIf="loading$ | async" class="loading-spinner">
        <mat-spinner></mat-spinner>
      </div>
    </div>
  `,
  styles: [`
    .menu-list-container {
      max-width: 1400px;
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
      grid-template-columns: 2fr 1fr 1fr;
      gap: 20px;
      align-items: center;
    }

    .search-field, .category-field, .availability-field {
      width: 100%;
    }

    .table-card {
      margin-bottom: 20px;
    }

    .table-container {
      overflow-x: auto;
    }

    .menu-table {
      width: 100%;
      min-width: 1000px;
    }

    .menu-table th {
      font-weight: 600;
      color: #333;
    }

    .menu-table td {
      padding: 12px 8px;
    }

    .item-name strong {
      display: block;
      font-size: 16px;
      margin-bottom: 4px;
    }

    .item-description {
      font-size: 12px;
      color: #666;
      max-width: 200px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .price {
      font-weight: 600;
      font-size: 16px;
      color: #2e7d32;
    }

    .loading-spinner {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 200px;
    }

    mat-chip {
      font-size: 12px;
    }

    .mat-column-actions {
      width: 120px;
      text-align: center;
    }

    .mat-column-available {
      width: 120px;
      text-align: center;
    }

    .mat-column-category {
      width: 150px;
    }

    .mat-column-price {
      width: 100px;
      text-align: right;
    }

    .mat-column-preparationTime {
      width: 100px;
      text-align: center;
    }

    @media (max-width: 768px) {
      .filters-row {
        grid-template-columns: 1fr;
        gap: 16px;
      }
    }
  `]
})
export class MenuListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'category', 'price', 'preparationTime', 'available', 'actions'];
  dataSource = new MatTableDataSource<MenuItem>();
  categories = Object.values(MenuCategory);
  
  menuItems$: Observable<MenuItem[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  private originalData: MenuItem[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private store: Store,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.menuItems$ = this.store.select(state => (state as any).menu.menuItems);
    this.loading$ = this.store.select(state => (state as any).menu.loading);
    this.error$ = this.store.select(state => (state as any).menu.error);
  }

  ngOnInit(): void {
    this.loadMenuItems();
    
    this.menuItems$.subscribe(menuItems => {
      this.originalData = menuItems || [];
      this.dataSource.data = this.originalData;
    });

    this.error$.subscribe(error => {
      if (error) {
        this.snackBar.open(error, 'Close', { duration: 5000 });
        this.store.dispatch(MenuActions.clearMenuErrors());
      }
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadMenuItems(): void {
    this.store.dispatch(MenuActions.loadMenuItems({ page: 0, size: 100 }));
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  filterByCategory(category: string): void {
    if (!category) {
      this.dataSource.data = this.originalData;
    } else {
      this.dataSource.data = this.originalData.filter(item => item.category === category);
    }
  }

  filterByAvailability(availability: string): void {
    if (!availability) {
      this.dataSource.data = this.originalData;
    } else if (availability === 'available') {
      this.dataSource.data = this.originalData.filter(item => item.available);
    } else if (availability === 'unavailable') {
      this.dataSource.data = this.originalData.filter(item => !item.available);
    }
  }

  formatCategoryName(category: string): string {
    return category.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(MenuFormComponent, {
      width: '700px',
      data: { menuItem: null, isEdit: false }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.store.dispatch(MenuActions.createMenuItem({ menuItem: result }));
      }
    });
  }

  openEditDialog(menuItem: MenuItem): void {
    const dialogRef = this.dialog.open(MenuFormComponent, {
      width: '700px',
      data: { menuItem, isEdit: true }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.store.dispatch(MenuActions.updateMenuItem({ 
          id: menuItem.id, 
          menuItem: result 
        }));
      }
    });
  }

  toggleAvailability(menuItem: MenuItem): void {
    this.store.dispatch(MenuActions.toggleMenuItemAvailability({
      id: menuItem.id,
      available: !menuItem.available
    }));
  }

  deleteMenuItem(menuItem: MenuItem): void {
    if (confirm(`Are you sure you want to delete "${menuItem.name}"?`)) {
      this.store.dispatch(MenuActions.deleteMenuItem({ id: menuItem.id }));
    }
  }
}
