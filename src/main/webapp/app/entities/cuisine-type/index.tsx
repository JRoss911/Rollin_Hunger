import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CuisineType from './cuisine-type';
import CuisineTypeDetail from './cuisine-type-detail';
import CuisineTypeUpdate from './cuisine-type-update';
import CuisineTypeDeleteDialog from './cuisine-type-delete-dialog';

const CuisineTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CuisineType />} />
    <Route path="new" element={<CuisineTypeUpdate />} />
    <Route path=":id">
      <Route index element={<CuisineTypeDetail />} />
      <Route path="edit" element={<CuisineTypeUpdate />} />
      <Route path="delete" element={<CuisineTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CuisineTypeRoutes;
