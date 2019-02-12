import React from "react";
import AccountForm from "./AccountForm";
import {FormTextRow} from "./CreateAccountForm";

export default class UpdateAccountForm extends React.Component {

    constructor(props){
        super(props);
        this.state= {
            email: this.props.account ? this.props.account.email : "",
            fullName: this.props.account ? this.props.account.fullName : "",
            username: this.props.account ? this.props.account.username : "",
            oldPassword: "",
            newPassword: ""
        };
        this.service = props.service;

    }

    onFullNameChange(event){
        this.setState({
            fullName: event.target.value
        });
        return event;
    }


    onUsernameChange(event){
        this.setState({
            username: event.target.value
        });
        return event;
    }


    onOldPasswordChange(event){
        this.setState({
            oldPassword: event.target.value
        });
        return event;
    }

    onNewPasswordChange(event){
        this.setState({
            newPassword: event.target.value
        });
        return event;
    }

    onSubmit(event){
        event.preventDefault();
        this.service.updateAccount(this.state);
        return event;
    }

    render() {
        let title = "Update Account";

        let fields = <div>
            <FormTextRow text={"Email"} placeholder={"example@gmail.com"} value={this.state.email} disabled={true}/>
            <FormTextRow text={"Full Name"} placeholder={"Emil Eifrem"} value={this.state.fullName}
                         onChange={this.onFullNameChange.bind(this)}/>
            <FormTextRow text={"Username"} placeholder={"emil.eifrem"} value={this.state.username}
                         onChange={this.onUsernameChange.bind(this)}/>
            <FormTextRow text={"Old Password"} placeholder={"123456"} type={"password"} value={this.state.oldPassword}
                         onChange={this.onOldPasswordChange.bind(this)}/>
            <FormTextRow text={"New Password"} placeholder={"123456"} type={"password"} value={this.state.newPassword}
                         onChange={this.onNewPasswordChange.bind(this)}/>
        </div>;

        return (
            <AccountForm fields={fields} title={title} onSubmit={this.onSubmit.bind(this)}/>
        );
    }
}
