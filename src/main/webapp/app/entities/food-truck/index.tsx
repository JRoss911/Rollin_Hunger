import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FoodTruck from './food-truck';
import FoodTruckDetail from './food-truck-detail';
import FoodTruckUpdate from './food-truck-update';
import FoodTruckDeleteDialog from './food-truck-delete-dialog';

const FoodTruckRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FoodTruck />} />
    <Route path="new" element={<FoodTruckUpdate />} />
    <Route path=":id">
      <Route index element={<FoodTruckDetail />} />
      <Route path="edit" element={<FoodTruckUpdate />} />
      <Route path="delete" element={<FoodTruckDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FoodTruckRoutes;
