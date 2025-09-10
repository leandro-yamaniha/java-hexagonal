import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer, CreateCustomerRequest, UpdateCustomerRequest } from '../models/customer.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly baseUrl = `${environment.apiUrl}/customers`;

  constructor(private http: HttpClient) {}

  getAllCustomers(page: number = 0, size: number = 20): Observable<Customer[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Customer[]>(this.baseUrl, { params });
  }

  getCustomerById(id: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/${id}`);
  }

  getCustomerByEmail(email: string): Observable<Customer> {
    const params = new HttpParams().set('email', email);
    return this.http.get<Customer>(`${this.baseUrl}/by-email`, { params });
  }

  searchCustomersByName(name: string): Observable<Customer[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<Customer[]>(`${this.baseUrl}/search`, { params });
  }

  createCustomer(customer: CreateCustomerRequest): Observable<Customer> {
    return this.http.post<Customer>(this.baseUrl, customer);
  }

  updateCustomer(id: string, customer: UpdateCustomerRequest): Observable<Customer> {
    return this.http.put<Customer>(`${this.baseUrl}/${id}`, customer);
  }

  deleteCustomer(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  activateCustomer(id: string): Observable<Customer> {
    return this.http.patch<Customer>(`${this.baseUrl}/${id}/activate`, {});
  }

  deactivateCustomer(id: string): Observable<Customer> {
    return this.http.patch<Customer>(`${this.baseUrl}/${id}/deactivate`, {});
  }
}
