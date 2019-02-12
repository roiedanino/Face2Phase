import React from 'react'
import './modals.css'

import { FormGroup, Label} from 'reactstrap';
import AccountForm from "./AccountForm";

export default class CreateAccountForm extends React.Component {

    constructor(props){
        super(props);

        this.state = {
            email: "",
            fullName:"",
            username:"",
            password:"",
        };
        this.toggle = props.toggle;
        this.service = props.service;
    }

    handleEmailChange(event){
        this.setState({
        email: event.target.value
        });
        return event
    }

    handleFullNameChange(event){
        this.setState({
            fullName: event.target.value
        });
        return event
    }
    handleUsernameChange(event){
        this.setState({
            username: event.target.value
        });
        return event
    }
    handlePasswordChange(event){
        this.setState({
            password: event.target.value
        });
        return event
    }
    onSubmit(event){
        event.preventDefault();
        console.log(JSON.stringify(this.state));

        this.service.createAccount(this.state).then(() => {
            this.toggle();
        });

        return event;
    }

    render() {
        let title = "Create Account";

        let fields = <div>
            <FormTextRow text={"Email"} placeholder={"example@gmail.com"} value={this.state.email} onChange={this.handleEmailChange.bind(this)}/>
            <FormTextRow text={"Full Name"} placeholder={"Emil Eifrem"} value={this.state.fullName} onChange={this.handleFullNameChange.bind(this)}/>
            <FormTextRow text={"Username"} placeholder={"emil.eifrem"} value={this.state.username} onChange={this.handleUsernameChange.bind(this)}/>
            <FormTextRow text={"Password"} placeholder={"123456"} type={"password"} value={this.state.password} onChange={this.handlePasswordChange.bind(this)}/>
        </div>;

        return (
            <AccountForm fields={fields} title={title} onSubmit={this.onSubmit.bind(this)}/>
        );
    }
}

export class FormTextRow extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            text: props.text,
            placeholder: props.placeholder,
            onChange: props.onChange,
            value: props.value,
            type: props.type ? props.type : "text",
            disabled: !!props.disabled
        };
    }

    onChange(event){
        this.setState({
            value: event.target.value
        });
        this.state.onChange(event);
    }

    render(){
        return(
            <div>
                <FormGroup>
                    <Label className={"col-form-label"}> {this.state.text} </Label>

                    <input className={"textField form-control"}
                           value={this.state.value}
                           placeholder={this.state.placeholder}
                           type={this.state.type}
                           onChange={this.onChange.bind(this)}
                           disabled={this.state.disabled}
                    />
                </FormGroup>
            </div>
        );
    };
}