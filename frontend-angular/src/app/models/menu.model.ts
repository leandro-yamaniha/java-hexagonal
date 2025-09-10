export interface MenuItem {
  id: string;
  name: string;
  description?: string;
  price: Money;
  category: MenuCategory;
  imageUrl?: string;
  available: boolean;
  preparationTimeMinutes: number;
  createdAt: string;
  updatedAt: string;
}

export interface Money {
  amount: number;
  currency: string;
}

export enum MenuCategory {
  APPETIZER = 'APPETIZER',
  MAIN_COURSE = 'MAIN_COURSE',
  DESSERT = 'DESSERT',
  BEVERAGE = 'BEVERAGE',
  SALAD = 'SALAD',
  SOUP = 'SOUP',
  PASTA = 'PASTA',
  PIZZA = 'PIZZA',
  SEAFOOD = 'SEAFOOD',
  MEAT = 'MEAT',
  VEGETARIAN = 'VEGETARIAN',
  VEGAN = 'VEGAN',
  GLUTEN_FREE = 'GLUTEN_FREE',
  KIDS_MENU = 'KIDS_MENU',
  SPECIAL = 'SPECIAL'
}

export interface CreateMenuItemRequest {
  name: string;
  description?: string;
  price: number;
  currency: string;
  category: string;
  imageUrl?: string;
  preparationTimeMinutes: number;
}

export interface UpdateMenuItemRequest {
  name: string;
  description?: string;
  price: number;
  currency: string;
  category: string;
  imageUrl?: string;
  preparationTimeMinutes: number;
  available: boolean;
}
