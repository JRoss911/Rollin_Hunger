import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cuisine-type.reducer';

export const CuisineTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cuisineTypeEntity = useAppSelector(state => state.cuisineType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cuisineTypeDetailsHeading">
          <Translate contentKey="rollinHungerApp.cuisineType.detail.title">CuisineType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cuisineTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rollinHungerApp.cuisineType.name">Name</Translate>
            </span>
          </dt>
          <dd>{cuisineTypeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/cuisine-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cuisine-type/${cuisineTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CuisineTypeDetail;
