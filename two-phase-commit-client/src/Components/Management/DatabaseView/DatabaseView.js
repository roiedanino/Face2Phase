import React from 'react'
import {Modal, Table, Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle} from 'reactstrap'
import './DatabaseView.css'
import UpdateAccountForm from "../OperationsModals/UpdateAccountForm";
import CreateAccountForm from "../OperationsModals/CreateAccountForm";
import ReadAccountView from "../OperationsModals/ReadAccountView";
import AccountService from "../../../services/AccountService";

export default class DatabaseView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            accounts: [],
            server: props.server
        };
        this.server = this.state.server;
        this.service = new AccountService(props.server);
    }

    componentDidMount(){
        let url = "http://" + this.server + "/accounts/list";

        let eventSource = new EventSource(url);
        eventSource.setHeaderValue = () => {'Access-Control-Allow-Origin:*'};
        eventSource.onmessage = event => {
            let accounts = JSON.parse(event.data);
            this.setState({accounts: accounts});
        };
    }

    render() {
        let rows = this.state.accounts.map((account, index) => {
            return <tr key={index} className={"table-wrapper"}>
                <th scope="row">{index + 1}</th>
                <td>{account.email}</td>
                <td>{account.fullName}</td>
                <td>{account.username}</td>
                <td style={{display:"flow", flowDirection:"row"}}>

                    <InfoButton email={account.email} account={account}/>
                    &nbsp;&nbsp;
                    <EditButton account={account} service={this.service}/>
                    &nbsp;
                    <DeleteButton email={account.email} service={this.service}/>
                    &nbsp;
                </td>
                <td>
                    <FollowButton accounts={this.state.accounts}/>
                </td>
                {/*<td>{account.passwordHash}</td>*/}
            </tr>
        });

        return (
            <div>
            <div className={"table-wrapper tableBody"}>
                <Table className={"table"} responsive>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Email</th>
                        <th>Full Name</th>
                        <th>Username</th>
                        <th>Actions</th>
                        <th>&nbsp;</th>

                        {/*<th>Password MD5 Hash</th>*/}
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>

                </Table>
            </div>
            <br/>
            <CreateButton service={this.service}/>
            </div>
    );
    }
}


class CreateButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            create: false,
            service: props.service
        }
    }

    onClick(event) {
        let create = this.state.create;
        this.setState({create: !create});
    }

    render() {
        return (
            <span>
                <Button onClick={this.onClick.bind(this)}>
                    <img src={"images/add.png"} alt={"create button"} />
                    &nbsp; Create Account
                </Button>
                 <Modal isOpen={this.state.create} toggle={this.onClick.bind(this)}>
                    <CreateAccountForm service={this.state.service} toggle={this.onClick.bind(this)}/>
                </Modal>
            </span>);
    }
}

class InfoButton extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            read: false,
            email: props.email,
            account: props.account
        }
    }

    onClick(event) {
        let read = this.state.read;
        this.setState({read: !read});
    }

    render() {
        return (
            <span>
                <img src={"images/information.png"} alt={"full info"} onClick={this.onClick.bind(this)}/>
                <Modal isOpen={this.state.read} toggle={this.onClick.bind(this)}>
                    <ReadAccountView account={this.state.account}/>
                </Modal>
            </span>);
    }
}


class EditButton extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            update: false,
            account:props.account
        };
        this.service = props.service;
    }

    onClick(event) {
        let update = this.state.update;
        this.setState({update: !update});
    }

    render() {
        return (
            <span>
                <img src={"images/edit.png"} alt={"edit"} onClick={this.onClick.bind(this)}/>
                <Modal isOpen={this.state.update} toggle={this.onClick.bind(this)}>
                    <UpdateAccountForm service={this.service} account={this.state.account}/>
                </Modal>
            </span>
        );
    }
}


class DeleteButton extends React.Component {

    constructor(props) {
        super(props);
        this.email = props.email;
        this.service = props.service;
    }

    onClick(event) {
        let result = window.confirm('Are you sure you want to delete ' + this.email +' ?');
        if(result){
            this.service.deleteAccount(this.email);
        }
    }

    render() {
        return (
            <span>
                <img src={"images/trash.png"} alt={"delete"} onClick={this.onClick.bind(this)}/>
            </span>
        );
    }
}


class FollowButton extends React.Component {

    constructor(props){
        super(props);

        this.state = {
            dropDownOpen:false,
            accounts: props.accounts
        }
    }

    onClick(event){
        let dropDownOpen = this.state.dropDownOpen;
        this.setState({dropDownOpen: !dropDownOpen});
    }

    render() {
        return (
                <Dropdown isOpen={this.state.dropDownOpen} toggle={this.onClick.bind(this)} direction="left">
                    <DropdownToggle
                        tag="span"
                        onClick={this.toggle}
                        data-toggle="dropdown"
                        aria-expanded={this.state.dropDownOpen}
                    >
                        <img src={"images/follower.png"} alt={"follower"} onClick={this.onClick.bind(this)}/>
                    </DropdownToggle>
                    <DropdownMenu>
                        {this.state.accounts.map((account, index) => <DropdownItem key={index}>{account.username}</DropdownItem>)}
                    </DropdownMenu>
                </Dropdown>


        );
    }
    /*

     */
}