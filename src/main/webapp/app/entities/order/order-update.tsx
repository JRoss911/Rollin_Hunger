import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMenuItem } from 'app/shared/model/menu-item.model';
import { getEntities as getMenuItems } from 'app/entities/menu-item/menu-item.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IFoodTruck } from 'app/shared/model/food-truck.model';
import { getEntities as getFoodTrucks } from 'app/entities/food-truck/food-truck.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { getEntity, updateEntity, createEntity, reset } from './order.reducer';

export const OrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const menuItems = useAppSelector(state => state.menuItem.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const foodTrucks = useAppSelector(state => state.foodTruck.entities);
  const orderEntity = useAppSelector(state => state.order.entity);
  const loading = useAppSelector(state => state.order.loading);
  const updating = useAppSelector(state => state.order.updating);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);

  const handleClose = () => {
    navigate('/order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMenuItems({}));
    dispatch(getUserProfiles({}));
    dispatch(getFoodTrucks({}));
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
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...orderEntity,
      ...values,
      menuItems: mapIdList(values.menuItems),
      user: userProfiles.find(it => it.id.toString() === values.user?.toString()),
      foodTruck: foodTrucks.find(it => it.id.toString() === values.foodTruck?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          timestamp: displayDefaultDateTime(),
        }
      : {
          ...orderEntity,
          timestamp: convertDateTimeFromServer(orderEntity.timestamp),
          menuItems: orderEntity?.menuItems?.map(e => e.id.toString()),
          user: orderEntity?.user?.id,
          foodTruck: orderEntity?.foodTruck?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rollinHungerApp.order.home.createOrEditLabel" data-cy="OrderCreateUpdateHeading">
            <Translate contentKey="rollinHungerApp.order.home.createOrEditLabel">Create or edit a Order</Translate>
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
                  id="order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rollinHungerApp.order.quantity')}
                id="order-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.order.status')}
                id="order-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.order.timestamp')}
                id="order-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('rollinHungerApp.order.menuItem')}
                id="order-menuItem"
                data-cy="menuItem"
                type="select"
                multiple
                name="menuItems"
              >
                <option value="" key="0" />
                {menuItems
                  ? menuItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="order-user" name="user" data-cy="user" label={translate('rollinHungerApp.order.user')} type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-foodTruck"
                name="foodTruck"
                data-cy="foodTruck"
                label={translate('rollinHungerApp.order.foodTruck')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order" replace color="info">
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

export default OrderUpdate;
