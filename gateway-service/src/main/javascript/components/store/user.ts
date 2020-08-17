import { getMetaProperty, getUserRoles } from "../misc";

export type UserRole = "ROLE_ADMIN" | "ROLE_USER";
interface User {
    id: number;
    name: string;
    surname: string;
    email: string;
    roles: UserRole[]
}

const defaultUser: User = getMetaProperty('user:authenticated') === 'true'
    ? {
        id: +getMetaProperty('user:id'),
        name: getMetaProperty('user:name'),
        surname: getMetaProperty('user:surname'),
        email: getMetaProperty('user:email'),
        roles: getUserRoles()
    }
    : null;


const UserActionsType = {
    UPDATE_USER_NAME_SURNAME: "update_user_name_surname",
    REMOVE_USER: "remove_user"
}

interface UpdateUserAction {
    type: typeof UserActionsType.UPDATE_USER_NAME_SURNAME,
    user: User
}

interface RemoveUserAction {
    type: typeof UserActionsType.REMOVE_USER,
}

type UserAction = RemoveUserAction | UpdateUserAction;

export default function (state: User = defaultUser, action: UserAction): User {
    switch (action.type) {
        case UserActionsType.UPDATE_USER_NAME_SURNAME:
            // @ts-ignore
            return action.user;
        case UserActionsType.REMOVE_USER:
            return null;
        default:
            return state;
    }
}

export const updateUser = (user: User): UpdateUserAction => ({
    type: UserActionsType.UPDATE_USER_NAME_SURNAME,
    user
});

export const removeUser = (): RemoveUserAction => ({ type: UserActionsType.REMOVE_USER });