import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "../Components/Home/Home";
import Management from "../Components/Management/Management";

export default () =>
    <Switch>
        <Route exact path="/" component={Home} />
        <Route exact path="/management" component={Management} />
        <Route exact path="/createCellType" />
    </Switch>;