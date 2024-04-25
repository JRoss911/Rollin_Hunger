import userProfile from 'app/entities/user-profile/user-profile.reducer';
import foodTruck from 'app/entities/food-truck/food-truck.reducer';
import cuisineType from 'app/entities/cuisine-type/cuisine-type.reducer';
import menuItem from 'app/entities/menu-item/menu-item.reducer';
import location from 'app/entities/location/location.reducer';
import event from 'app/entities/event/event.reducer';
import review from 'app/entities/review/review.reducer';
import order from 'app/entities/order/order.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  foodTruck,
  cuisineType,
  menuItem,
  location,
  event,
  review,
  order,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
