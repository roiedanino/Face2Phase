import React from 'react'
import {Label, FormGroup} from 'reactstrap'
import AccountForm from "./AccountForm";

export default class ReadAccountView extends React.Component{
    constructor(props){
        super(props);

        this.state = {
            account: props.account
        }
    }

    render() {

        let fields =  <div>
            <FormGroup>
            <Label className={"col-form-label"}>Email:</Label><br/>
            <Label>{this.state.account.email}</Label>
            </FormGroup>
            <FormGroup>
            <Label className={"col-form-label"}>Full Name:</Label><br/>
            <Label>{this.state.account.fullName}</Label>
            </FormGroup>
            <FormGroup>
                <Label className={"col-form-label"}>Username:</Label><br/>
                <Label>{this.state.account.username}</Label>
            </FormGroup>
            <FormGroup>
            <Label className={"col-form-label"}>PasswordHash:</Label><br/>
            <Label>{this.state.account.passwordHash}</Label>
            </FormGroup>
            <FormGroup>
            <Label className={"col-form-label"}>Following: </Label><br/>
            {this.state.account.following.map((follow, index) => <Label key={index}>{follow.fullName}</Label>)}
            </FormGroup>
        </div>;

        return (
            <AccountForm fields={fields} title={"Read Account"}/>
        );
    }
}