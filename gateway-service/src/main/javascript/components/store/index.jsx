import { createStore, combineReducers } from "redux";
import user from './user';

let reducers = combineReducers({ user });
export default createStore(reducers);