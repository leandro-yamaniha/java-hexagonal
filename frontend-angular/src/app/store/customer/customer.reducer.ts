import { createReducer, on } from '@ngrx/store';
import { Customer } from '../../models/customer.model';
import * as CustomerActions from './customer.actions';

export interface CustomerState {
  customers: Customer[];
  selectedCustomer: Customer | null;
  loading: boolean;
  error: string | null;
}

export const initialState: CustomerState = {
  customers: [],
  selectedCustomer: null,
  loading: false,
  error: null
};

export const customerReducer = createReducer(
  initialState,

  // Load Customers
  on(CustomerActions.loadCustomers, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.loadCustomersSuccess, (state, { customers }) => ({
    ...state,
    customers,
    loading: false,
    error: null
  })),

  on(CustomerActions.loadCustomersFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Load Customer
  on(CustomerActions.loadCustomer, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.loadCustomerSuccess, (state, { customer }) => ({
    ...state,
    selectedCustomer: customer,
    loading: false,
    error: null
  })),

  on(CustomerActions.loadCustomerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Search Customers
  on(CustomerActions.searchCustomers, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.searchCustomersSuccess, (state, { customers }) => ({
    ...state,
    customers,
    loading: false,
    error: null
  })),

  on(CustomerActions.searchCustomersFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Create Customer
  on(CustomerActions.createCustomer, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.createCustomerSuccess, (state, { customer }) => ({
    ...state,
    customers: [...state.customers, customer],
    loading: false,
    error: null
  })),

  on(CustomerActions.createCustomerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Update Customer
  on(CustomerActions.updateCustomer, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.updateCustomerSuccess, (state, { customer }) => ({
    ...state,
    customers: state.customers.map(c => c.id === customer.id ? customer : c),
    selectedCustomer: state.selectedCustomer?.id === customer.id ? customer : state.selectedCustomer,
    loading: false,
    error: null
  })),

  on(CustomerActions.updateCustomerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Delete Customer
  on(CustomerActions.deleteCustomer, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.deleteCustomerSuccess, (state, { id }) => ({
    ...state,
    customers: state.customers.filter(c => c.id !== id),
    selectedCustomer: state.selectedCustomer?.id === id ? null : state.selectedCustomer,
    loading: false,
    error: null
  })),

  on(CustomerActions.deleteCustomerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Toggle Customer Status
  on(CustomerActions.toggleCustomerStatus, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(CustomerActions.toggleCustomerStatusSuccess, (state, { customer }) => ({
    ...state,
    customers: state.customers.map(c => c.id === customer.id ? customer : c),
    selectedCustomer: state.selectedCustomer?.id === customer.id ? customer : state.selectedCustomer,
    loading: false,
    error: null
  })),

  on(CustomerActions.toggleCustomerStatusFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Clear Errors
  on(CustomerActions.clearCustomerErrors, (state) => ({
    ...state,
    error: null
  })),

  // Set Selected Customer
  on(CustomerActions.setSelectedCustomer, (state, { customer }) => ({
    ...state,
    selectedCustomer: customer
  }))
);
