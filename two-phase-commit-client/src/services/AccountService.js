import fetch from 'isomorphic-fetch'

export default class AccountService{

    constructor(server){
        this.server = server;

    }

    createAccount(account){
        let url = "http://" + this.server + "/primary/accounts/";

        return fetch(url,{
            method: 'POST',
            body: JSON.stringify(account),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            console.log(response);
            return response.json();
        }).then(body => {
            console.log(body);
            alert("Account " + JSON.stringify(body) + "\ncreated successfully");
            return body;
        }).catch((reason => alert(reason)));
    }

    updateAccount(updateAccountQuery){
        let url = "http://" + this.server + "/primary/accounts/";

        return fetch(url,{
            method: 'PUT',
            body: JSON.stringify(updateAccountQuery),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            console.log(response);
            return response.json();
        }).then(body => {
            console.log(body);
            alert("Account " + JSON.stringify(updateAccountQuery.fullName) + "\n updated successfully");
            return body;
        }).catch((reason => alert("Failed to update " + updateAccountQuery.email + ":" + reason)));
    }

    deleteAccount(email){
        let url =  "http://" + this.server + "/primary/accounts/"+email;

        return fetch(url,{
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Methods' :'POST, GET, DELETE, PUT'
            }
        }).then(response => {
            console.log(response);
            alert("Deleted successfully " + email);
            return response;
        }).catch((reason => alert(reason)));
    }

}