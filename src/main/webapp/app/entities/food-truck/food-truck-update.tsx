import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ICuisineType } from 'app/shared/model/cuisine-type.model';
import { getEntities as getCuisineTypes } from 'app/entities/cuisine-type/cuisine-type.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IFoodTruck } from 'app/shared/model/food-truck.model';
import { getEntity, updateEntity, createEntity, reset } from './food-truck.reducer';

export const FoodTruckUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const cuisineTypes = useAppSelector(state => state.cuisineType.entities);
  const locations = useAppSelector(state => state.location.entities);
  const foodTruckEntity = useAppSelector(state => state.foodTruck.entity);
  const loading = useAppSelector(state => state.foodTruck.loading);
  const updating = useAppSelector(state => state.foodTruck.updating);
  const updateSuccess = useAppSelector(state => state.foodTruck.updateSuccess);

  const handleClose = () => {
    navigate('/food-truck');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getCuisineTypes({}));
    dispatch(getLocations({}));
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
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }

    const entity = {
      ...foodTruckEntity,
      ...values,
      owner: userProfiles.find(it => it.id.toString() === values.owner?.toString()),
      cuisineType: cuisineTypes.find(it => it.id.toString() === values.cuisineType?.toString()),
      location: locations.find(it => it.id.toString() === values.location?.toString()),
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
          ...foodTruckEntity,
          owner: foodTruckEntity?.owner?.id,
          cuisineType: foodTruckEntity?.cuisineType?.id,
          location: foodTruckEntity?.location?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rollinHungerApp.foodTruck.home.createOrEditLabel" data-cy="FoodTruckCreateUpdateHeading">
            <Translate contentKey="rollinHungerApp.foodTruck.home.createOrEditLabel">Create or edit a FoodTruck</Translate>
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
                  id="food-truck-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rollinHungerApp.foodTruck.name')}
                id="food-truck-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.foodTruck.description')}
                id="food-truck-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.foodTruck.rating')}
                id="food-truck-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <ValidatedField
                label={translate('rollinHungerApp.foodTruck.profilePicture')}
                id="food-truck-profilePicture"
                name="profilePicture"
                data-cy="profilePicture"
                type="text"
              />
              <ValidatedField
                id="food-truck-owner"
                name="owner"
                data-cy="owner"
                label={translate('rollinHungerApp.foodTruck.owner')}
                type="select"
              >
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
                id="food-truck-cuisineType"
                name="cuisineType"
                data-cy="cuisineType"
                label={translate('rollinHungerApp.foodTruck.cuisineType')}
                type="select"
              >
                <option value="" key="0" />
                {cuisineTypes
                  ? cuisineTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="food-truck-location"
                name="location"
                data-cy="location"
                label={translate('rollinHungerApp.foodTruck.location')}
                type="select"
              >
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/food-truck" replace color="info">
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

export default FoodTruckUpdate;
