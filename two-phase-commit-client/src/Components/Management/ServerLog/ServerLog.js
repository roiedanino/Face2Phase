import './ServerLog.css'
import React from 'react'
import moment from 'moment'

import {ListGroup} from 'reactstrap'
import DatabaseView from "../DatabaseView/DatabaseView";

export default class ServerLog extends React.Component{

    constructor(props){
        super(props);

        /*
        {title: "Init Phase", timestamp: new Date(), content:"Create linordolev@gmail.com"},
                {title: "Commit Phase", timestamp: new Date(), content:"roiedanino@gmail.com"},
                {title: "Init Phase", timestamp: new Date(), content:"Create linordolev@gmail.com"},
                {title: "Commit Phase", timestamp: new Date(), content:"roiedanino@gmail.com"}
         */
        this.state = {
            server: props.server,
            messages: [{title:"Hello!", id:-1, content:"Logs will be shown here", timestamp:new Date()}]
        }
        this.server = props.server;
    }

    componentDidMount(){
        let url = "http://" + this.server + "/logs/all";

        let eventSource = new EventSource(url);
        eventSource.setHeaderValue = () => {'Access-Control-Allow-Origin:*'};
        eventSource.onmessage = event => {
            let log = JSON.parse(event.data);
            //console.log(log);

            let messages = this.state.messages;
            if(messages.filter(msg => msg.id === log.id).length === 0){
                // console.log("NEW LOG: " + log);
                messages.push(log);
                this.setState({accounts: messages});
            }
        };

        console.log(this.state.accounts);
    }

    render(){
        return(
            <div className={"serverLog " + this.props.place}>
                <div className={"logView"}>
                    <h3 className={"title"}>{this.state.server}</h3>
                    <ListGroup className={"messagesList"}>
                        {
                            this.state.messages.map((message, index) =>
                                    <LogMessage key={index} index={index} message={message} className={"message"}/>)
                        }
                    </ListGroup>
                </div>
                <hr className={"my-4"}/>
                <DatabaseView className={"databaseView"} server={this.state.server} accounts={
                    [
                        {
                            email:"linordolev@gmail.com",
                            fullName:"Linor Dolev",
                            username:"linordolev",
                            passwordHash:"e10adc3949ba59abbe56e057f20f883e"
                        },
                        {
                            email:"roiedanino@gmail.com",
                            fullName:"Roie Danino",
                            username:"roiedanino",
                            passwordHash:"58b4e38f66bcdb546380845d6af27187"
                        }]}/>
            </div>
        );
    }
}

class LogMessage extends React.Component{
    constructor(props){
        super(props);

        this.state = {
            index: this.props.index,
            message: this.props.message
        }
    }

    render(){
        let index = this.state.index;
        let message = this.state.message;

        return (
            <div key={index} className="list-group-item list-group-item-action flex-column align-items-start">
                <div className="d-flex w-100 justify-content-between">
                    <h5 className="mb-1"> {message.title} </h5>
                    <small>{moment(new Date(message.timestamp),"x").fromNow()}</small>
                </div>
                <p className="mb-1"> {message.content} </p>
                <small>{message.role}</small>
            </div>
        );
    }
}