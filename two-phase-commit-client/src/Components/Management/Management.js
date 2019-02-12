import React from 'react'

import './Management.css'
import ServerLog from "./ServerLog/ServerLog";

export default class Management extends React.Component{

    render(){

        return(
            <div className={"management"}>
                {/*<CrudOperationsNav/>*/}
                <div className={"serversInfo"}>
                    <ServerLog id={"leftServer"} place={'float-left'} server={"localhost:8080"}/>
                    <ServerLog id={"rightServer"} place={'float-right'} server={"localhost:8081"}/>
                </div>
            </div>
        );
    }
}