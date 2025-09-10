import { createAction, props } from '@ngrx/store';
import { Customer, CreateCustomerRequest, UpdateCustomerRequest } from '../../models/customer.model';

// Load Customers
export const loadCustomers = createAction(
  '[Customer] Load Customers',
  props<{ page?: number; size?: number }>()
);

export const loadCustomersSuccess = createAction(
  '[Customer] Load Customers Success',
  props<{ customers: Customer[] }>()
);

export const loadCustomersFailure = createAction(
  '[Customer] Load Customers Failure',
  props<{ error: string }>()
);

// Load Customer by ID
export const loadCustomer = createAction(
  '[Customer] Load Customer',
  props<{ id: string }>()
);

export const loadCustomerSuccess = createAction(
  '[Customer] Load Customer Success',
  props<{ customer: Customer }>()
);

export const loadCustomerFailure = createAction(
  '[Customer] Load Customer Failure',
  props<{ error: string }>()
);

// Search Customers
export const searchCustomers = createAction(
  '[Customer] Search Customers',
  props<{ name: string }>()
);

export const searchCustomersSuccess = createAction(
  '[Customer] Search Customers Success',
  props<{ customers: Customer[] }>()
);

export const searchCustomersFailure = createAction(
  '[Customer] Search Customers Failure',
  props<{ error: string }>()
);

// Create Customer
export const createCustomer = createAction(
  '[Customer] Create Customer',
  props<{ customer: CreateCustomerRequest }>()
);

export const createCustomerSuccess = createAction(
  '[Customer] Create Customer Success',
  props<{ customer: Customer }>()
);

export const createCustomerFailure = createAction(
  '[Customer] Create Customer Failure',
  props<{ error: string }>()
);

// Update Customer
export const updateCustomer = createAction(
  '[Customer] Update Customer',
  props<{ id: string; customer: UpdateCustomerRequest }>()
);

export const updateCustomerSuccess = createAction(
  '[Customer] Update Customer Success',
  props<{ customer: Customer }>()
);

export const updateCustomerFailure = createAction(
  '[Customer] Update Customer Failure',
  props<{ error: string }>()
);

// Delete Customer
export const deleteCustomer = createAction(
  '[Customer] Delete Customer',
  props<{ id: string }>()
);

export const deleteCustomerSuccess = createAction(
  '[Customer] Delete Customer Success',
  props<{ id: string }>()
);

export const deleteCustomerFailure = createAction(
  '[Customer] Delete Customer Failure',
  props<{ error: string }>()
);

// Toggle Customer Status
export const toggleCustomerStatus = createAction(
  '[Customer] Toggle Customer Status',
  props<{ id: string; activate: boolean }>()
);

export const toggleCustomerStatusSuccess = createAction(
  '[Customer] Toggle Customer Status Success',
  props<{ customer: Customer }>()
);

export const toggleCustomerStatusFailure = createAction(
  '[Customer] Toggle Customer Status Failure',
  props<{ error: string }>()
);

// Clear Errors
export const clearCustomerErrors = createAction(
  '[Customer] Clear Errors'
);

// Set Selected Customer
export const setSelectedCustomer = createAction(
  '[Customer] Set Selected Customer',
  props<{ customer: Customer | null }>()
);
