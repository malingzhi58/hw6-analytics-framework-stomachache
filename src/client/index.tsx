import * as React from 'react';
// @ts-ignore
import ReactDOM from "react-dom";
import App from './App';
import './scss/app';
import Store from "./components/Store";

ReactDOM.render(
    <Store>
        <App />,
    </Store>,
    document.getElementById("root")
);