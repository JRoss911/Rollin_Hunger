import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import FoodTruck from './food-truck';
import CuisineType from './cuisine-type';
import MenuItem from './menu-item';
import Location from './location';
import Event from './event';
import Review from './review';
import Order from './order';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="food-truck/*" element={<FoodTruck />} />
        <Route path="cuisine-type/*" element={<CuisineType />} />
        <Route path="menu-item/*" element={<MenuItem />} />
        <Route path="location/*" element={<Location />} />
        <Route path="event/*" element={<Event />} />
        <Route path="review/*" element={<Review />} />
        <Route path="order/*" element={<Order />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
