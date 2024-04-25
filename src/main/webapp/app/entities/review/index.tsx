import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Review from './review';
import ReviewDetail from './review-detail';
import ReviewUpdate from './review-update';
import ReviewDeleteDialog from './review-delete-dialog';

const ReviewRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Review />} />
    <Route path="new" element={<ReviewUpdate />} />
    <Route path=":id">
      <Route index element={<ReviewDetail />} />
      <Route path="edit" element={<ReviewUpdate />} />
      <Route path="delete" element={<ReviewDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReviewRoutes;
