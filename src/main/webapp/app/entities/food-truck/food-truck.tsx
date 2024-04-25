import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './food-truck.reducer';

export const FoodTruck = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const foodTruckList = useAppSelector(state => state.foodTruck.entities);
  const loading = useAppSelector(state => state.foodTruck.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="food-truck-heading" data-cy="FoodTruckHeading">
        <Translate contentKey="rollinHungerApp.foodTruck.home.title">Food Trucks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rollinHungerApp.foodTruck.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/food-truck/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rollinHungerApp.foodTruck.home.createLabel">Create new Food Truck</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {foodTruckList && foodTruckList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rollinHungerApp.foodTruck.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="rollinHungerApp.foodTruck.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="rollinHungerApp.foodTruck.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('rating')}>
                  <Translate contentKey="rollinHungerApp.foodTruck.rating">Rating</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rating')} />
                </th>
                <th className="hand" onClick={sort('profilePicture')}>
                  <Translate contentKey="rollinHungerApp.foodTruck.profilePicture">Profile Picture</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profilePicture')} />
                </th>
                <th>
                  <Translate contentKey="rollinHungerApp.foodTruck.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="rollinHungerApp.foodTruck.cuisineType">Cuisine Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="rollinHungerApp.foodTruck.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {foodTruckList.map((foodTruck, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/food-truck/${foodTruck.id}`} color="link" size="sm">
                      {foodTruck.id}
                    </Button>
                  </td>
                  <td>{foodTruck.name}</td>
                  <td>{foodTruck.description}</td>
                  <td>{foodTruck.rating}</td>
                  <td>{foodTruck.profilePicture}</td>
                  <td>{foodTruck.owner ? <Link to={`/user-profile/${foodTruck.owner.id}`}>{foodTruck.owner.id}</Link> : ''}</td>
                  <td>
                    {foodTruck.cuisineType ? <Link to={`/cuisine-type/${foodTruck.cuisineType.id}`}>{foodTruck.cuisineType.id}</Link> : ''}
                  </td>
                  <td>{foodTruck.location ? <Link to={`/location/${foodTruck.location.id}`}>{foodTruck.location.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/food-truck/${foodTruck.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/food-truck/${foodTruck.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/food-truck/${foodTruck.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="rollinHungerApp.foodTruck.home.notFound">No Food Trucks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FoodTruck;
