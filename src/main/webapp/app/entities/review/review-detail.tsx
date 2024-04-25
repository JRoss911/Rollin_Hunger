import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './review.reducer';

export const ReviewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reviewEntity = useAppSelector(state => state.review.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reviewDetailsHeading">
          <Translate contentKey="rollinHungerApp.review.detail.title">Review</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.id}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="rollinHungerApp.review.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.rating}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="rollinHungerApp.review.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.comment}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="rollinHungerApp.review.date">Date</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.date ? <TextFormat value={reviewEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="rollinHungerApp.review.user">User</Translate>
          </dt>
          <dd>{reviewEntity.user ? reviewEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/review" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/review/${reviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReviewDetail;
