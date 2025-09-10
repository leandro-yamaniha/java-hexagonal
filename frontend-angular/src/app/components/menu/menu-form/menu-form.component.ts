import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MenuItem, CreateMenuItemRequest, UpdateMenuItemRequest, MenuCategory } from '../../../models/menu.model';

export interface MenuDialogData {
  menuItem: MenuItem | null;
  isEdit: boolean;
}

@Component({
  selector: 'app-menu-form',
  template: `
    <h2 mat-dialog-title>{{ data.isEdit ? 'Edit Menu Item' : 'Add New Menu Item' }}</h2>
    
    <mat-dialog-content>
      <form [formGroup]="menuForm" class="menu-form">
        <div class="form-row">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Name *</mat-label>
            <input matInput formControlName="name" placeholder="Enter item name">
            <mat-error *ngIf="menuForm.get('name')?.hasError('required')">
              Name is required
            </mat-error>
            <mat-error *ngIf="menuForm.get('name')?.hasError('minlength')">
              Name must be at least 2 characters long
            </mat-error>
          </mat-form-field>
        </div>

        <div class="form-row">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Description</mat-label>
            <textarea matInput formControlName="description" rows="3" placeholder="Enter item description"></textarea>
          </mat-form-field>
        </div>

        <div class="form-row two-columns">
          <mat-form-field appearance="outline">
            <mat-label>Price *</mat-label>
            <input matInput formControlName="price" type="number" step="0.01" min="0" placeholder="0.00">
            <span matPrefix>$&nbsp;</span>
            <mat-error *ngIf="menuForm.get('price')?.hasError('required')">
              Price is required
            </mat-error>
            <mat-error *ngIf="menuForm.get('price')?.hasError('min')">
              Price must be greater than 0
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Currency *</mat-label>
            <mat-select formControlName="currency">
              <mat-option value="USD">USD ($)</mat-option>
              <mat-option value="EUR">EUR (€)</mat-option>
              <mat-option value="GBP">GBP (£)</mat-option>
              <mat-option value="CAD">CAD (C$)</mat-option>
            </mat-select>
            <mat-error *ngIf="menuForm.get('currency')?.hasError('required')">
              Currency is required
            </mat-error>
          </mat-form-field>
        </div>

        <div class="form-row two-columns">
          <mat-form-field appearance="outline">
            <mat-label>Category *</mat-label>
            <mat-select formControlName="category">
              <mat-option *ngFor="let category of categories" [value]="category">
                {{ formatCategoryName(category) }}
              </mat-option>
            </mat-select>
            <mat-error *ngIf="menuForm.get('category')?.hasError('required')">
              Category is required
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline">
            <mat-label>Preparation Time *</mat-label>
            <input matInput formControlName="preparationTimeMinutes" type="number" min="1" max="300" placeholder="15">
            <span matSuffix>&nbsp;minutes</span>
            <mat-error *ngIf="menuForm.get('preparationTimeMinutes')?.hasError('required')">
              Preparation time is required
            </mat-error>
            <mat-error *ngIf="menuForm.get('preparationTimeMinutes')?.hasError('min')">
              Preparation time must be at least 1 minute
            </mat-error>
            <mat-error *ngIf="menuForm.get('preparationTimeMinutes')?.hasError('max')">
              Preparation time cannot exceed 300 minutes
            </mat-error>
          </mat-form-field>
        </div>

        <div class="form-row">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Image URL</mat-label>
            <input matInput formControlName="imageUrl" placeholder="https://example.com/image.jpg">
            <mat-error *ngIf="menuForm.get('imageUrl')?.hasError('pattern')">
              Please enter a valid URL
            </mat-error>
          </mat-form-field>
        </div>

        <div class="form-row" *ngIf="data.isEdit">
          <mat-checkbox formControlName="available">
            Item is available for ordering
          </mat-checkbox>
        </div>
      </form>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button 
              color="primary" 
              (click)="onSave()" 
              [disabled]="menuForm.invalid">
        {{ data.isEdit ? 'Update' : 'Create' }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .menu-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
      min-width: 500px;
      padding: 20px 0;
    }

    .form-row {
      display: flex;
      gap: 16px;
    }

    .two-columns {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
    }

    .full-width {
      width: 100%;
    }

    mat-dialog-content {
      max-height: 600px;
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

    mat-checkbox {
      margin-top: 8px;
    }

    @media (max-width: 600px) {
      .two-columns {
        grid-template-columns: 1fr;
      }
      
      .menu-form {
        min-width: 300px;
      }
    }
  `]
})
export class MenuFormComponent implements OnInit {
  menuForm: FormGroup;
  categories = Object.values(MenuCategory);

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<MenuFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MenuDialogData
  ) {
    this.menuForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.data.isEdit && this.data.menuItem) {
      this.populateForm(this.data.menuItem);
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: [''],
      price: ['', [Validators.required, Validators.min(0.01)]],
      currency: ['USD', [Validators.required]],
      category: ['', [Validators.required]],
      preparationTimeMinutes: ['', [Validators.required, Validators.min(1), Validators.max(300)]],
      imageUrl: ['', [Validators.pattern(/^https?:\/\/.+/)]],
      available: [true]
    });
  }

  private populateForm(menuItem: MenuItem): void {
    this.menuForm.patchValue({
      name: menuItem.name,
      description: menuItem.description || '',
      price: menuItem.price.amount,
      currency: menuItem.price.currency,
      category: menuItem.category,
      preparationTimeMinutes: menuItem.preparationTimeMinutes,
      imageUrl: menuItem.imageUrl || '',
      available: menuItem.available
    });
  }

  formatCategoryName(category: string): string {
    return category.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  onSave(): void {
    if (this.menuForm.valid) {
      const formValue = this.menuForm.value;
      
      const menuItemData: CreateMenuItemRequest | UpdateMenuItemRequest = {
        name: formValue.name.trim(),
        description: formValue.description?.trim() || undefined,
        price: parseFloat(formValue.price),
        currency: formValue.currency,
        category: formValue.category,
        preparationTimeMinutes: parseInt(formValue.preparationTimeMinutes),
        imageUrl: formValue.imageUrl?.trim() || undefined,
        ...(this.data.isEdit && { available: formValue.available })
      };

      this.dialogRef.close(menuItemData);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
