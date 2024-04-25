import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './menu-item.reducer';

export const MenuItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const menuItemEntity = useAppSelector(state => state.menuItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="menuItemDetailsHeading">
          <Translate contentKey="rollinHungerApp.menuItem.detail.title">MenuItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rollinHungerApp.menuItem.name">Name</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="rollinHungerApp.menuItem.description">Description</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="rollinHungerApp.menuItem.price">Price</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.price}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.menuItem.truck">Truck</Translate>
          </dt>
          <dd>{menuItemEntity.truck ? menuItemEntity.truck.id : ''}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.menuItem.orders">Orders</Translate>
          </dt>
          <dd>
            {menuItemEntity.orders
              ? menuItemEntity.orders.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {menuItemEntity.orders && i === menuItemEntity.orders.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/menu-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/menu-item/${menuItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MenuItemDetail;
