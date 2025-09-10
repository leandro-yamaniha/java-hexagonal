import { createAction, props } from '@ngrx/store';
import { MenuItem, CreateMenuItemRequest, UpdateMenuItemRequest, MenuCategory } from '../../models/menu.model';

// Load Menu Items
export const loadMenuItems = createAction(
  '[Menu] Load Menu Items',
  props<{ page?: number; size?: number }>()
);

export const loadMenuItemsSuccess = createAction(
  '[Menu] Load Menu Items Success',
  props<{ menuItems: MenuItem[] }>()
);

export const loadMenuItemsFailure = createAction(
  '[Menu] Load Menu Items Failure',
  props<{ error: string }>()
);

// Load Menu Item by ID
export const loadMenuItem = createAction(
  '[Menu] Load Menu Item',
  props<{ id: string }>()
);

export const loadMenuItemSuccess = createAction(
  '[Menu] Load Menu Item Success',
  props<{ menuItem: MenuItem }>()
);

export const loadMenuItemFailure = createAction(
  '[Menu] Load Menu Item Failure',
  props<{ error: string }>()
);

// Load Menu Items by Category
export const loadMenuItemsByCategory = createAction(
  '[Menu] Load Menu Items by Category',
  props<{ category: MenuCategory }>()
);

export const loadMenuItemsByCategorySuccess = createAction(
  '[Menu] Load Menu Items by Category Success',
  props<{ menuItems: MenuItem[] }>()
);

export const loadMenuItemsByCategoryFailure = createAction(
  '[Menu] Load Menu Items by Category Failure',
  props<{ error: string }>()
);

// Load Available Menu Items
export const loadAvailableMenuItems = createAction(
  '[Menu] Load Available Menu Items'
);

export const loadAvailableMenuItemsSuccess = createAction(
  '[Menu] Load Available Menu Items Success',
  props<{ menuItems: MenuItem[] }>()
);

export const loadAvailableMenuItemsFailure = createAction(
  '[Menu] Load Available Menu Items Failure',
  props<{ error: string }>()
);

// Search Menu Items
export const searchMenuItems = createAction(
  '[Menu] Search Menu Items',
  props<{ name: string }>()
);

export const searchMenuItemsSuccess = createAction(
  '[Menu] Search Menu Items Success',
  props<{ menuItems: MenuItem[] }>()
);

export const searchMenuItemsFailure = createAction(
  '[Menu] Search Menu Items Failure',
  props<{ error: string }>()
);

// Create Menu Item
export const createMenuItem = createAction(
  '[Menu] Create Menu Item',
  props<{ menuItem: CreateMenuItemRequest }>()
);

export const createMenuItemSuccess = createAction(
  '[Menu] Create Menu Item Success',
  props<{ menuItem: MenuItem }>()
);

export const createMenuItemFailure = createAction(
  '[Menu] Create Menu Item Failure',
  props<{ error: string }>()
);

// Update Menu Item
export const updateMenuItem = createAction(
  '[Menu] Update Menu Item',
  props<{ id: string; menuItem: UpdateMenuItemRequest }>()
);

export const updateMenuItemSuccess = createAction(
  '[Menu] Update Menu Item Success',
  props<{ menuItem: MenuItem }>()
);

export const updateMenuItemFailure = createAction(
  '[Menu] Update Menu Item Failure',
  props<{ error: string }>()
);

// Delete Menu Item
export const deleteMenuItem = createAction(
  '[Menu] Delete Menu Item',
  props<{ id: string }>()
);

export const deleteMenuItemSuccess = createAction(
  '[Menu] Delete Menu Item Success',
  props<{ id: string }>()
);

export const deleteMenuItemFailure = createAction(
  '[Menu] Delete Menu Item Failure',
  props<{ error: string }>()
);

// Toggle Menu Item Availability
export const toggleMenuItemAvailability = createAction(
  '[Menu] Toggle Menu Item Availability',
  props<{ id: string; available: boolean }>()
);

export const toggleMenuItemAvailabilitySuccess = createAction(
  '[Menu] Toggle Menu Item Availability Success',
  props<{ menuItem: MenuItem }>()
);

export const toggleMenuItemAvailabilityFailure = createAction(
  '[Menu] Toggle Menu Item Availability Failure',
  props<{ error: string }>()
);

// Clear Errors
export const clearMenuErrors = createAction(
  '[Menu] Clear Errors'
);

// Set Selected Menu Item
export const setSelectedMenuItem = createAction(
  '[Menu] Set Selected Menu Item',
  props<{ menuItem: MenuItem | null }>()
);

// Set Selected Category
export const setSelectedCategory = createAction(
  '[Menu] Set Selected Category',
  props<{ category: MenuCategory | null }>()
);
