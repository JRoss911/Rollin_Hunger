import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/food-truck">
        <Translate contentKey="global.menu.entities.foodTruck" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cuisine-type">
        <Translate contentKey="global.menu.entities.cuisineType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/menu-item">
        <Translate contentKey="global.menu.entities.menuItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/location">
        <Translate contentKey="global.menu.entities.location" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/event">
        <Translate contentKey="global.menu.entities.event" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/review">
        <Translate contentKey="global.menu.entities.review" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        <Translate contentKey="global.menu.entities.order" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
