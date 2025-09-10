import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Customer, CreateCustomerRequest, UpdateCustomerRequest } from '../../../models/customer.model';

export interface CustomerDialogData {
  customer: Customer | null;
  isEdit: boolean;
}

@Component({
  selector: 'app-customer-form',
  template: `
    <h2 mat-dialog-title>{{ data.isEdit ? 'Edit Customer' : 'Add New Customer' }}</h2>
    
    <mat-dialog-content>
      <form [formGroup]="customerForm" class="customer-form">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Name *</mat-label>
          <input matInput formControlName="name" placeholder="Enter customer name">
          <mat-error *ngIf="customerForm.get('name')?.hasError('required')">
            Name is required
          </mat-error>
          <mat-error *ngIf="customerForm.get('name')?.hasError('minlength')">
            Name must be at least 2 characters long
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Email *</mat-label>
          <input matInput formControlName="email" type="email" placeholder="Enter email address">
          <mat-error *ngIf="customerForm.get('email')?.hasError('required')">
            Email is required
          </mat-error>
          <mat-error *ngIf="customerForm.get('email')?.hasError('email')">
            Please enter a valid email address
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Phone</mat-label>
          <input matInput formControlName="phone" placeholder="Enter phone number">
          <mat-error *ngIf="customerForm.get('phone')?.hasError('pattern')">
            Please enter a valid phone number
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Address</mat-label>
          <textarea matInput formControlName="address" rows="3" placeholder="Enter address"></textarea>
        </mat-form-field>
      </form>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button 
              color="primary" 
              (click)="onSave()" 
              [disabled]="customerForm.invalid">
        {{ data.isEdit ? 'Update' : 'Create' }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .customer-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
      min-width: 400px;
      padding: 20px 0;
    }

    .full-width {
      width: 100%;
    }

    mat-dialog-content {
      max-height: 500px;
      overflow-y: auto;
    }

    mat-dialog-actions {
      padding: 16px 0;
      gap: 8px;
    }

    textarea {
      resize: vertical;
      min-height: 80px;
    }
  `]
})
export class CustomerFormComponent implements OnInit {
  customerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CustomerFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CustomerDialogData
  ) {
    this.customerForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.data.isEdit && this.data.customer) {
      this.populateForm(this.data.customer);
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.pattern(/^[\+]?[1-9][\d]{0,15}$/)]],
      address: ['']
    });
  }

  private populateForm(customer: Customer): void {
    this.customerForm.patchValue({
      name: customer.name,
      email: customer.email,
      phone: customer.phone || '',
      address: customer.address || ''
    });
  }

  onSave(): void {
    if (this.customerForm.valid) {
      const formValue = this.customerForm.value;
      
      const customerData: CreateCustomerRequest | UpdateCustomerRequest = {
        name: formValue.name.trim(),
        email: formValue.email.trim(),
        phone: formValue.phone?.trim() || undefined,
        address: formValue.address?.trim() || undefined
      };

      this.dialogRef.close(customerData);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
