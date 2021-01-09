import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import {createStore} from 'redux';
import rootReducer from './reducers'
import {Provider} from 'react-redux';
import network from './network'

const store = createStore(rootReducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__())

//const ch = network.credHolder;


//network.credHolder.authenticate()
/*while(!ch.authenticated()){

}*/
console.log(network.credHolder.checkToken())
//network.credHolder.authenticated()

//network.credHolder.checkToken()
console.log(network.checkToken())

/*while(!network.credHolder.checkToken()){

}*/
/*
if(network.credHolder.authenticated()){
//if(ch.authenticated()){
    //network.credHolder.getResource()

    var callback = (resp) =>{console.log(resp)}
    //console.log(ch.getResource("/block",callback))
    console.log(network.credHolder.getResource("/block",callback))

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>, 
    document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
}*/


//network.credHolder.authenticated(()=>{
network.authenticated(()=>{

var callback = (resp) =>{console.log(resp)}
//console.log(network.credHolder.getResource("/block",callback))
console.log(network.getResource("/block",callback))

ReactDOM.render(
<Provider store={store}>
    <App />
</Provider>, 
document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
})



export {
    store
};
