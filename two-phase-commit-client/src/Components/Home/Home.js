import React, { Component } from 'react';
import {Button} from 'reactstrap';
import {Link} from 'react-router-dom';
import DesignSlides from './DesignSlides/DesignSlides';
import './Home.css';

class Home extends Component {
    render() {
        return (
            <div className="Home">
                <div className={"bg"} />
                <div className={"jumbo"}>
                        <h1 className="display-3">
                            <span className={"insta"}>Face</span>
                            <span className={"two"}>2</span>
                            <span className={"phase"}>Phase</span>
                        </h1>
                        <p className="lead">2 Phase Commit Protocol for Social Network Purposes</p>
                        <hr className="my-2" />
                        <p className="lead">by Linor Dolev & Roie Danino</p>
                        <p className="lead">
                            <Link to={"/management"}>
                                <Button className={"startSimulatingButton"}>Start Your Own Social Network</Button>
                            </Link>
                        </p>
                </div>

            </div>
        );
    }
}

export default Home;
