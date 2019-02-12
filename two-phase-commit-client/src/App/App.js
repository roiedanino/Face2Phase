import React, { Component } from 'react';
import {BrowserRouter} from 'react-router-dom';
import NavRoutes from '../Routes/NavRoutes';
import { NavbarBrand, Navbar, Nav, NavItem, NavLink } from 'reactstrap';
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
          <BrowserRouter>
              <div>
                  <Navbar className={"Nav"}  expand="md">
                      <NavbarBrand href="/" className={"homeButton"}>
                          <h1 className={"homeButton"}>
                              <span className={"insta"}>Face</span>
                              <span className={"two"}>2</span>
                              <span className={"phase"}>Phase</span>
                          </h1>
                      </NavbarBrand>
                      <Nav className="ml-auto" navbar>
                          <NavItem>
                              <NavLink className={"navLink"} href="/management">Manage Social Network</NavLink>
                          </NavItem>
                          <NavItem>
                              <NavLink className={"navLink"} href="/about">About Architecture</NavLink>
                          </NavItem>
                      </Nav>
                  </Navbar>
                  <NavRoutes/>
              </div>
          </BrowserRouter>
      </div>
    );
  }
}

export default App;
