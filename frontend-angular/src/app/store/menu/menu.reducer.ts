import { createReducer, on } from '@ngrx/store';
import { MenuItem, MenuCategory } from '../../models/menu.model';
import * as MenuActions from './menu.actions';

export interface MenuState {
  menuItems: MenuItem[];
  selectedMenuItem: MenuItem | null;
  selectedCategory: MenuCategory | null;
  loading: boolean;
  error: string | null;
}

export const initialState: MenuState = {
  menuItems: [],
  selectedMenuItem: null,
  selectedCategory: null,
  loading: false,
  error: null
};

export const menuReducer = createReducer(
  initialState,

  // Load Menu Items
  on(MenuActions.loadMenuItems, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.loadMenuItemsSuccess, (state, { menuItems }) => ({
    ...state,
    menuItems,
    loading: false,
    error: null
  })),

  on(MenuActions.loadMenuItemsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Load Menu Item
  on(MenuActions.loadMenuItem, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.loadMenuItemSuccess, (state, { menuItem }) => ({
    ...state,
    selectedMenuItem: menuItem,
    loading: false,
    error: null
  })),

  on(MenuActions.loadMenuItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Load Menu Items by Category
  on(MenuActions.loadMenuItemsByCategory, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.loadMenuItemsByCategorySuccess, (state, { menuItems }) => ({
    ...state,
    menuItems,
    loading: false,
    error: null
  })),

  on(MenuActions.loadMenuItemsByCategoryFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Load Available Menu Items
  on(MenuActions.loadAvailableMenuItems, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.loadAvailableMenuItemsSuccess, (state, { menuItems }) => ({
    ...state,
    menuItems,
    loading: false,
    error: null
  })),

  on(MenuActions.loadAvailableMenuItemsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Search Menu Items
  on(MenuActions.searchMenuItems, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.searchMenuItemsSuccess, (state, { menuItems }) => ({
    ...state,
    menuItems,
    loading: false,
    error: null
  })),

  on(MenuActions.searchMenuItemsFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Create Menu Item
  on(MenuActions.createMenuItem, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.createMenuItemSuccess, (state, { menuItem }) => ({
    ...state,
    menuItems: [...state.menuItems, menuItem],
    loading: false,
    error: null
  })),

  on(MenuActions.createMenuItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Update Menu Item
  on(MenuActions.updateMenuItem, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.updateMenuItemSuccess, (state, { menuItem }) => ({
    ...state,
    menuItems: state.menuItems.map(item => item.id === menuItem.id ? menuItem : item),
    selectedMenuItem: state.selectedMenuItem?.id === menuItem.id ? menuItem : state.selectedMenuItem,
    loading: false,
    error: null
  })),

  on(MenuActions.updateMenuItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Delete Menu Item
  on(MenuActions.deleteMenuItem, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.deleteMenuItemSuccess, (state, { id }) => ({
    ...state,
    menuItems: state.menuItems.filter(item => item.id !== id),
    selectedMenuItem: state.selectedMenuItem?.id === id ? null : state.selectedMenuItem,
    loading: false,
    error: null
  })),

  on(MenuActions.deleteMenuItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Toggle Menu Item Availability
  on(MenuActions.toggleMenuItemAvailability, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(MenuActions.toggleMenuItemAvailabilitySuccess, (state, { menuItem }) => ({
    ...state,
    menuItems: state.menuItems.map(item => item.id === menuItem.id ? menuItem : item),
    selectedMenuItem: state.selectedMenuItem?.id === menuItem.id ? menuItem : state.selectedMenuItem,
    loading: false,
    error: null
  })),

  on(MenuActions.toggleMenuItemAvailabilityFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Clear Errors
  on(MenuActions.clearMenuErrors, (state) => ({
    ...state,
    error: null
  })),

  // Set Selected Menu Item
  on(MenuActions.setSelectedMenuItem, (state, { menuItem }) => ({
    ...state,
    selectedMenuItem: menuItem
  })),

  // Set Selected Category
  on(MenuActions.setSelectedCategory, (state, { category }) => ({
    ...state,
    selectedCategory: category
  }))
);
