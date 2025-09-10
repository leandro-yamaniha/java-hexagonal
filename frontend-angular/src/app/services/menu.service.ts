import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MenuItem, CreateMenuItemRequest, UpdateMenuItemRequest, MenuCategory } from '../models/menu.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private readonly baseUrl = `${environment.apiUrl}/menu-items`;

  constructor(private http: HttpClient) {}

  getAllMenuItems(page: number = 0, size: number = 20): Observable<MenuItem[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<MenuItem[]>(this.baseUrl, { params });
  }

  getMenuItemById(id: string): Observable<MenuItem> {
    return this.http.get<MenuItem>(`${this.baseUrl}/${id}`);
  }

  getMenuItemsByCategory(category: MenuCategory): Observable<MenuItem[]> {
    const params = new HttpParams().set('category', category);
    return this.http.get<MenuItem[]>(`${this.baseUrl}/by-category`, { params });
  }

  getAvailableMenuItems(): Observable<MenuItem[]> {
    return this.http.get<MenuItem[]>(`${this.baseUrl}/available`);
  }

  searchMenuItemsByName(name: string): Observable<MenuItem[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<MenuItem[]>(`${this.baseUrl}/search`, { params });
  }

  createMenuItem(menuItem: CreateMenuItemRequest): Observable<MenuItem> {
    return this.http.post<MenuItem>(this.baseUrl, menuItem);
  }

  updateMenuItem(id: string, menuItem: UpdateMenuItemRequest): Observable<MenuItem> {
    return this.http.put<MenuItem>(`${this.baseUrl}/${id}`, menuItem);
  }

  deleteMenuItem(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  setMenuItemAvailability(id: string, available: boolean): Observable<MenuItem> {
    const endpoint = available ? 'make-available' : 'make-unavailable';
    return this.http.patch<MenuItem>(`${this.baseUrl}/${id}/${endpoint}`, {});
  }

  getMenuCategories(): MenuCategory[] {
    return Object.values(MenuCategory);
  }
}
