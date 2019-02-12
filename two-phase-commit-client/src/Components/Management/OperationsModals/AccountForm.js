import React from 'react'
import './modals.css'

import { Button, Form, CardTitle } from 'reactstrap';

export default class AccountForm extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            account: props.account ? props.account : {email: "", fullName:"", username:"", passwordHash:""},
            fields: props.fields,
            title: props.title,
            onSubmit: this.props.onSubmit
        };

    }

    render() {
        return (
            <div>
                <Form className={"accountForm"} innerRef={"/management"}>
                    <CardTitle className={"formTitle"}>{this.state.title}</CardTitle>
                    {this.state.fields}
                    <Button className="btn btn-dark btn-lg btn-block col-sm-11" onClick={this.state.onSubmit}>
                        {this.state.title}
                    </Button>
                </Form>
            </div>
        );
    }
}
