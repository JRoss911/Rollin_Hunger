import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './food-truck.reducer';

export const FoodTruckDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const foodTruckEntity = useAppSelector(state => state.foodTruck.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="foodTruckDetailsHeading">
          <Translate contentKey="rollinHungerApp.foodTruck.detail.title">FoodTruck</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{foodTruckEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rollinHungerApp.foodTruck.name">Name</Translate>
            </span>
          </dt>
          <dd>{foodTruckEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="rollinHungerApp.foodTruck.description">Description</Translate>
            </span>
          </dt>
          <dd>{foodTruckEntity.description}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="rollinHungerApp.foodTruck.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{foodTruckEntity.rating}</dd>
          <dt>
            <span id="profilePicture">
              <Translate contentKey="rollinHungerApp.foodTruck.profilePicture">Profile Picture</Translate>
            </span>
          </dt>
          <dd>{foodTruckEntity.profilePicture}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.foodTruck.owner">Owner</Translate>
          </dt>
          <dd>{foodTruckEntity.owner ? foodTruckEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.foodTruck.cuisineType">Cuisine Type</Translate>
          </dt>
          <dd>{foodTruckEntity.cuisineType ? foodTruckEntity.cuisineType.id : ''}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.foodTruck.location">Location</Translate>
          </dt>
          <dd>{foodTruckEntity.location ? foodTruckEntity.location.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/food-truck" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/food-truck/${foodTruckEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FoodTruckDetail;
