import {createStore as createReduxStore, combineReducers, ReducersMapObject} from "redux";
import user from './user';

const createStore = (additionalStores: ReducersMapObject = {}) => {
    let reducers = combineReducers({ user, ...additionalStores });
    return createReduxStore(reducers);
}

export default createStore;