import _ from 'lodash';
import { USER_LOGIN } from '../actions';

export default function (state = {}, action) {
    switch (action.type) {
        case USER_LOGIN:
            console.log(action)
            return action.payload.data;
        default:
            return state;
    }
}