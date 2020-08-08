import { getMetaProperty } from "../misc";

const defaultUser = getMetaProperty('user:authenticated') === 'true'
    ? {
        id: +getMetaProperty('user:id'),
        name: getMetaProperty('user:name'),
        surname: getMetaProperty('user:surname'),
        email: getMetaProperty('user:email'),
        roles: getMetaProperty('user:roles')
    }
    : null;

const USER_ACTIONS = {
    LOGOUT: "logout_user",
    UPDATE_NAME_SURNAME: "update_user_name_surname"
}

export default function (state = defaultUser, action) {
    switch (action.type) {
        case USER_ACTIONS.UPDATE_NAME_SURNAME:
            return action.user;
        case USER_ACTIONS.LOGOUT:
            return null;
        default:
            return state;
    }
}

export const updateUser = user => ({
    type: USER_ACTIONS.UPDATE_NAME_SURNAME,
    user
});

export const removeUser = () => ({ type: USER_ACTIONS.LOGOUT });