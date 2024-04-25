import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './event.reducer';

export const EventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventEntity = useAppSelector(state => state.event.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventDetailsHeading">
          <Translate contentKey="rollinHungerApp.event.detail.title">Event</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="rollinHungerApp.event.name">Name</Translate>
            </span>
          </dt>
          <dd>{eventEntity.name}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="rollinHungerApp.event.date">Date</Translate>
            </span>
          </dt>
          <dd>{eventEntity.date ? <TextFormat value={eventEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="rollinHungerApp.event.description">Description</Translate>
            </span>
          </dt>
          <dd>{eventEntity.description}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.event.location">Location</Translate>
          </dt>
          <dd>{eventEntity.location ? eventEntity.location.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/event/${eventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventDetail;
