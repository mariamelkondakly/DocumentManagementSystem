import React, { Suspense, Fragment, lazy } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import Loader from './components/Loader/Loader';
import AdminLayout from './layouts/AdminLayout';

import { BASE_URL } from './config/constant';

export const renderRoutes = (routes = []) => (
  <Suspense fallback={<Loader />}>
    <Routes>
      {routes.map((route, i) => {
        const Guard = route.guard || Fragment;
        const Layout = route.layout || Fragment;
        const Element = route.element;

        return (
          <Route
            key={i}
            path={route.path}
            element={
              <Guard>
                <Layout>{route.routes ? renderRoutes(route.routes) : <Element props={true} />}</Layout>
              </Guard>
            }
          />
        );
      })}
    </Routes>
  </Suspense>
);

const routes = [
  {
    exact: 'true',
    path: '/login',
    element: lazy(() => import('./views/auth/signin/SignIn1'))
  },
  {
    exact: 'true',
    path: '/register',
    element: lazy(() => import('./views/auth/signup/SignUp1'))
  },
  {
    path: '*',
    layout: AdminLayout,
    routes: [
      {
        exact: 'true',
        path: '/Home',
        element: lazy(() => import('./views/dashboard/index'))
      },
      {
        exact: 'true',
        path: '/workspaces',
        element: lazy(() => import('./views/workspaces/index'))
      },
      {
        exact: 'true', 
        path: '/rootDirectories/:workspaceId', 
        element: lazy(() => import('./views/directories/root/index'))
      },
      {
        exact: 'true', 
        path: '/subDirectories/:parentId', 
        element: lazy(() => import('./views/directories/sub/index'))
      },
      {
        exact: 'true', 
        path: '/profile', 
        element: lazy(() => import('./views/user/userPage'))
      },
      {
        path: '*',
        exact: 'true',
        element: () => <Navigate to="/login" />
      }
    ]
  }
];

export default routes;
