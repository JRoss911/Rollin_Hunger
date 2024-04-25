import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order.reducer';

export const OrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">
          <Translate contentKey="rollinHungerApp.order.detail.title">Order</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="rollinHungerApp.order.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderEntity.quantity}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="rollinHungerApp.order.status">Status</Translate>
            </span>
          </dt>
          <dd>{orderEntity.status}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="rollinHungerApp.order.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{orderEntity.timestamp ? <TextFormat value={orderEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.order.menuItem">Menu Item</Translate>
          </dt>
          <dd>
            {orderEntity.menuItems
              ? orderEntity.menuItems.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {orderEntity.menuItems && i === orderEntity.menuItems.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="rollinHungerApp.order.user">User</Translate>
          </dt>
          <dd>{orderEntity.user ? orderEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.order.foodTruck">Food Truck</Translate>
          </dt>
          <dd>{orderEntity.foodTruck ? orderEntity.foodTruck.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
