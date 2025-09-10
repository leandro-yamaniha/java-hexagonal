import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { MenuService } from '../../services/menu.service';
import * as MenuActions from './menu.actions';

@Injectable()
export class MenuEffects {

  loadMenuItems$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.loadMenuItems),
      mergeMap(({ page, size }) =>
        this.menuService.getAllMenuItems(page, size).pipe(
          map(menuItems => MenuActions.loadMenuItemsSuccess({ menuItems })),
          catchError(error => of(MenuActions.loadMenuItemsFailure({ error: error.message })))
        )
      )
    )
  );

  loadMenuItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.loadMenuItem),
      mergeMap(({ id }) =>
        this.menuService.getMenuItemById(id).pipe(
          map(menuItem => MenuActions.loadMenuItemSuccess({ menuItem })),
          catchError(error => of(MenuActions.loadMenuItemFailure({ error: error.message })))
        )
      )
    )
  );

  loadMenuItemsByCategory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.loadMenuItemsByCategory),
      mergeMap(({ category }) =>
        this.menuService.getMenuItemsByCategory(category).pipe(
          map(menuItems => MenuActions.loadMenuItemsByCategorySuccess({ menuItems })),
          catchError(error => of(MenuActions.loadMenuItemsByCategoryFailure({ error: error.message })))
        )
      )
    )
  );

  loadAvailableMenuItems$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.loadAvailableMenuItems),
      mergeMap(() =>
        this.menuService.getAvailableMenuItems().pipe(
          map(menuItems => MenuActions.loadAvailableMenuItemsSuccess({ menuItems })),
          catchError(error => of(MenuActions.loadAvailableMenuItemsFailure({ error: error.message })))
        )
      )
    )
  );

  searchMenuItems$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.searchMenuItems),
      mergeMap(({ name }) =>
        this.menuService.searchMenuItemsByName(name).pipe(
          map(menuItems => MenuActions.searchMenuItemsSuccess({ menuItems })),
          catchError(error => of(MenuActions.searchMenuItemsFailure({ error: error.message })))
        )
      )
    )
  );

  createMenuItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.createMenuItem),
      mergeMap(({ menuItem }) =>
        this.menuService.createMenuItem(menuItem).pipe(
          map(createdMenuItem => MenuActions.createMenuItemSuccess({ menuItem: createdMenuItem })),
          catchError(error => of(MenuActions.createMenuItemFailure({ error: error.message })))
        )
      )
    )
  );

  updateMenuItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.updateMenuItem),
      mergeMap(({ id, menuItem }) =>
        this.menuService.updateMenuItem(id, menuItem).pipe(
          map(updatedMenuItem => MenuActions.updateMenuItemSuccess({ menuItem: updatedMenuItem })),
          catchError(error => of(MenuActions.updateMenuItemFailure({ error: error.message })))
        )
      )
    )
  );

  deleteMenuItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.deleteMenuItem),
      mergeMap(({ id }) =>
        this.menuService.deleteMenuItem(id).pipe(
          map(() => MenuActions.deleteMenuItemSuccess({ id })),
          catchError(error => of(MenuActions.deleteMenuItemFailure({ error: error.message })))
        )
      )
    )
  );

  toggleMenuItemAvailability$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MenuActions.toggleMenuItemAvailability),
      mergeMap(({ id, available }) =>
        this.menuService.setMenuItemAvailability(id, available).pipe(
          map(menuItem => MenuActions.toggleMenuItemAvailabilitySuccess({ menuItem })),
          catchError(error => of(MenuActions.toggleMenuItemAvailabilityFailure({ error: error.message })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private menuService: MenuService
  ) {}
}
