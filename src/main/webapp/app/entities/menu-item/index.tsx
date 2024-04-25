import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MenuItem from './menu-item';
import MenuItemDetail from './menu-item-detail';
import MenuItemUpdate from './menu-item-update';
import MenuItemDeleteDialog from './menu-item-delete-dialog';

const MenuItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MenuItem />} />
    <Route path="new" element={<MenuItemUpdate />} />
    <Route path=":id">
      <Route index element={<MenuItemDetail />} />
      <Route path="edit" element={<MenuItemUpdate />} />
      <Route path="delete" element={<MenuItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MenuItemRoutes;
