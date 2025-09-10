import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { CustomerService } from '../../services/customer.service';
import * as CustomerActions from './customer.actions';

@Injectable()
export class CustomerEffects {

  loadCustomers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.loadCustomers),
      mergeMap(({ page, size }) =>
        this.customerService.getAllCustomers(page, size).pipe(
          map(customers => CustomerActions.loadCustomersSuccess({ customers })),
          catchError(error => of(CustomerActions.loadCustomersFailure({ error: error.message })))
        )
      )
    )
  );

  loadCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.loadCustomer),
      mergeMap(({ id }) =>
        this.customerService.getCustomerById(id).pipe(
          map(customer => CustomerActions.loadCustomerSuccess({ customer })),
          catchError(error => of(CustomerActions.loadCustomerFailure({ error: error.message })))
        )
      )
    )
  );

  searchCustomers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.searchCustomers),
      mergeMap(({ name }) =>
        this.customerService.searchCustomersByName(name).pipe(
          map(customers => CustomerActions.searchCustomersSuccess({ customers })),
          catchError(error => of(CustomerActions.searchCustomersFailure({ error: error.message })))
        )
      )
    )
  );

  createCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.createCustomer),
      mergeMap(({ customer }) =>
        this.customerService.createCustomer(customer).pipe(
          map(createdCustomer => CustomerActions.createCustomerSuccess({ customer: createdCustomer })),
          catchError(error => of(CustomerActions.createCustomerFailure({ error: error.message })))
        )
      )
    )
  );

  updateCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.updateCustomer),
      mergeMap(({ id, customer }) =>
        this.customerService.updateCustomer(id, customer).pipe(
          map(updatedCustomer => CustomerActions.updateCustomerSuccess({ customer: updatedCustomer })),
          catchError(error => of(CustomerActions.updateCustomerFailure({ error: error.message })))
        )
      )
    )
  );

  deleteCustomer$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.deleteCustomer),
      mergeMap(({ id }) =>
        this.customerService.deleteCustomer(id).pipe(
          map(() => CustomerActions.deleteCustomerSuccess({ id })),
          catchError(error => of(CustomerActions.deleteCustomerFailure({ error: error.message })))
        )
      )
    )
  );

  toggleCustomerStatus$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CustomerActions.toggleCustomerStatus),
      mergeMap(({ id, activate }) => {
        const request = activate 
          ? this.customerService.activateCustomer(id)
          : this.customerService.deactivateCustomer(id);
        
        return request.pipe(
          map(customer => CustomerActions.toggleCustomerStatusSuccess({ customer })),
          catchError(error => of(CustomerActions.toggleCustomerStatusFailure({ error: error.message })))
        );
      })
    )
  );

  constructor(
    private actions$: Actions,
    private customerService: CustomerService
  ) {}
}
