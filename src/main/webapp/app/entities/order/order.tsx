import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './order.reducer';

export const Order = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const orderList = useAppSelector(state => state.order.entities);
  const loading = useAppSelector(state => state.order.loading);

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
      <h2 id="order-heading" data-cy="OrderHeading">
        <Translate contentKey="rollinHungerApp.order.home.title">Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rollinHungerApp.order.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rollinHungerApp.order.home.createLabel">Create new Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {orderList && orderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rollinHungerApp.order.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="rollinHungerApp.order.quantity">Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quantity')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="rollinHungerApp.order.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('timestamp')}>
                  <Translate contentKey="rollinHungerApp.order.timestamp">Timestamp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('timestamp')} />
                </th>
                <th>
                  <Translate contentKey="rollinHungerApp.order.menuItem">Menu Item</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="rollinHungerApp.order.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="rollinHungerApp.order.foodTruck">Food Truck</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderList.map((order, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/order/${order.id}`} color="link" size="sm">
                      {order.id}
                    </Button>
                  </td>
                  <td>{order.quantity}</td>
                  <td>{order.status}</td>
                  <td>{order.timestamp ? <TextFormat type="date" value={order.timestamp} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {order.menuItems
                      ? order.menuItems.map((val, j) => (
                          <span key={j}>
                            <Link to={`/menu-item/${val.id}`}>{val.id}</Link>
                            {j === order.menuItems.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{order.user ? <Link to={`/user-profile/${order.user.id}`}>{order.user.id}</Link> : ''}</td>
                  <td>{order.foodTruck ? <Link to={`/food-truck/${order.foodTruck.id}`}>{order.foodTruck.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/order/${order.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/order/${order.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/order/${order.id}/delete`)}
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
              <Translate contentKey="rollinHungerApp.order.home.notFound">No Orders found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Order;
