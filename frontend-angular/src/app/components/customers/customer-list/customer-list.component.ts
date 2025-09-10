import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Customer } from '../../../models/customer.model';
import { CustomerFormComponent } from '../customer-form/customer-form.component';
import * as CustomerActions from '../../../store/customer/customer.actions';

@Component({
  selector: 'app-customer-list',
  template: `
    <div class="customer-list-container">
      <div class="header">
        <h1>Customer Management</h1>
        <button mat-raised-button color="primary" (click)="openCreateDialog()">
          <mat-icon>person_add</mat-icon>
          Add Customer
        </button>
      </div>

      <mat-card class="search-card">
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Search customers</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Search by name or email">
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>
        </mat-card-content>
      </mat-card>

      <mat-card class="table-card">
        <mat-card-content>
          <div class="table-container">
            <table mat-table [dataSource]="dataSource" matSort class="customer-table">
              
              <ng-container matColumnDef="name">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Name</th>
                <td mat-cell *matCellDef="let customer">{{ customer.name }}</td>
              </ng-container>

              <ng-container matColumnDef="email">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Email</th>
                <td mat-cell *matCellDef="let customer">{{ customer.email }}</td>
              </ng-container>

              <ng-container matColumnDef="phone">
                <th mat-header-cell *matHeaderCellDef>Phone</th>
                <td mat-cell *matCellDef="let customer">{{ customer.phone || 'N/A' }}</td>
              </ng-container>

              <ng-container matColumnDef="status">
                <th mat-header-cell *matHeaderCellDef>Status</th>
                <td mat-cell *matCellDef="let customer">
                  <mat-chip [color]="customer.active ? 'primary' : 'warn'" selected>
                    {{ customer.active ? 'Active' : 'Inactive' }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="createdAt">
                <th mat-header-cell *matHeaderCellDef mat-sort-header>Created</th>
                <td mat-cell *matCellDef="let customer">{{ customer.createdAt | date:'short' }}</td>
              </ng-container>

              <ng-container matColumnDef="actions">
                <th mat-header-cell *matHeaderCellDef>Actions</th>
                <td mat-cell *matCellDef="let customer">
                  <button mat-icon-button color="primary" (click)="openEditDialog(customer)" matTooltip="Edit">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button 
                          [color]="customer.active ? 'warn' : 'primary'"
                          (click)="toggleCustomerStatus(customer)"
                          [matTooltip]="customer.active ? 'Deactivate' : 'Activate'">
                    <mat-icon>{{ customer.active ? 'block' : 'check_circle' }}</mat-icon>
                  </button>
                  <button mat-icon-button color="warn" (click)="deleteCustomer(customer)" matTooltip="Delete">
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
    .customer-list-container {
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

    .table-container {
      overflow-x: auto;
    }

    .customer-table {
      width: 100%;
      min-width: 800px;
    }

    .customer-table th {
      font-weight: 600;
      color: #333;
    }

    .customer-table td {
      padding: 12px 8px;
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

    .mat-column-status {
      width: 100px;
      text-align: center;
    }

    .mat-column-phone {
      width: 150px;
    }

    .mat-column-createdAt {
      width: 150px;
    }
  `]
})
export class CustomerListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'email', 'phone', 'status', 'createdAt', 'actions'];
  dataSource = new MatTableDataSource<Customer>();
  
  customers$: Observable<Customer[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private store: Store,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.customers$ = this.store.select(state => (state as any).customers.customers);
    this.loading$ = this.store.select(state => (state as any).customers.loading);
    this.error$ = this.store.select(state => (state as any).customers.error);
  }

  ngOnInit(): void {
    this.loadCustomers();
    
    this.customers$.subscribe(customers => {
      this.dataSource.data = customers || [];
    });

    this.error$.subscribe(error => {
      if (error) {
        this.snackBar.open(error, 'Close', { duration: 5000 });
        this.store.dispatch(CustomerActions.clearCustomerErrors());
      }
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadCustomers(): void {
    this.store.dispatch(CustomerActions.loadCustomers({ page: 0, size: 100 }));
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CustomerFormComponent, {
      width: '600px',
      data: { customer: null, isEdit: false }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.store.dispatch(CustomerActions.createCustomer({ customer: result }));
      }
    });
  }

  openEditDialog(customer: Customer): void {
    const dialogRef = this.dialog.open(CustomerFormComponent, {
      width: '600px',
      data: { customer, isEdit: true }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.store.dispatch(CustomerActions.updateCustomer({ 
          id: customer.id, 
          customer: result 
        }));
      }
    });
  }

  toggleCustomerStatus(customer: Customer): void {
    this.store.dispatch(CustomerActions.toggleCustomerStatus({
      id: customer.id,
      activate: !customer.active
    }));
  }

  deleteCustomer(customer: Customer): void {
    if (confirm(`Are you sure you want to delete customer "${customer.name}"?`)) {
      this.store.dispatch(CustomerActions.deleteCustomer({ id: customer.id }));
    }
  }
}
