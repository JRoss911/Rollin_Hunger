import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Event from './event';
import EventDetail from './event-detail';
import EventUpdate from './event-update';
import EventDeleteDialog from './event-delete-dialog';

const EventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Event />} />
    <Route path="new" element={<EventUpdate />} />
    <Route path=":id">
      <Route index element={<EventDetail />} />
      <Route path="edit" element={<EventUpdate />} />
      <Route path="delete" element={<EventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EventRoutes;
