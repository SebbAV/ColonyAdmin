import React from "react";
import ReactDOM from "react-dom";
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import './components/index.css'


import promise from 'redux-promise';
import reducers from './reducers/index'

import Login from './components/login'
import MainMenu from './components/main_menu'
import SignUp from './components/signup'

const createStoreMiddleware = applyMiddleware(promise)(createStore)
ReactDOM.render(
  <Provider store={createStoreMiddleware(reducers)}>
    <BrowserRouter>
      <div>
        <Switch>
          <Route path="/main" component={MainMenu} />
          <Route path="/register" component={SignUp} />
          <Route path="/" component={Login} />
        </Switch>
      </div>
    </BrowserRouter>
  </Provider>
  , document.querySelector('.base'))