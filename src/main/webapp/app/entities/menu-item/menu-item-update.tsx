import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFoodTruck } from 'app/shared/model/food-truck.model';
import { getEntities as getFoodTrucks } from 'app/entities/food-truck/food-truck.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { IMenuItem } from 'app/shared/model/menu-item.model';
import { getEntity, updateEntity, createEntity, reset } from './menu-item.reducer';

export const MenuItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const foodTrucks = useAppSelector(state => state.foodTruck.entities);
  const orders = useAppSelector(state => state.order.entities);
  const menuItemEntity = useAppSelector(state => state.menuItem.entity);
  const loading = useAppSelector(state => state.menuItem.loading);
  const updating = useAppSelector(state => state.menuItem.updating);
  const updateSuccess = useAppSelector(state => state.menuItem.updateSuccess);

  const handleClose = () => {
    navigate('/menu-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFoodTrucks({}));
    dispatch(getOrders({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...menuItemEntity,
      ...values,
      truck: foodTrucks.find(it => it.id.toString() === values.truck?.toString()),
      orders: mapIdList(values.orders),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...menuItemEntity,
          truck: menuItemEntity?.truck?.id,
          orders: menuItemEntity?.orders?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rollinHungerApp.menuItem.home.createOrEditLabel" data-cy="MenuItemCreateUpdateHeading">
            <Translate contentKey="rollinHungerApp.menuItem.home.createOrEditLabel">Create or edit a MenuItem</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="menu-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rollinHungerApp.menuItem.name')}
                id="menu-item-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.menuItem.description')}
                id="menu-item-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.menuItem.price')}
                id="menu-item-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                id="menu-item-truck"
                name="truck"
                data-cy="truck"
                label={translate('rollinHungerApp.menuItem.truck')}
                type="select"
              >
                <option value="" key="0" />
                {foodTrucks
                  ? foodTrucks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('rollinHungerApp.menuItem.orders')}
                id="menu-item-orders"
                data-cy="orders"
                type="select"
                multiple
                name="orders"
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/menu-item" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MenuItemUpdate;
