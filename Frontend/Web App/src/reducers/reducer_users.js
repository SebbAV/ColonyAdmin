import _ from 'lodash';
import { GET_NEIGHBOR,GET_USER } from '../actions';

export default function (state = {}, action) {
    switch (action.type) {
        case GET_NEIGHBOR:
            console.log(action.payload.data)
            return { ...state, 'users': action.payload.data }
        case GET_USER:
            console.log(action.payload.data)
            return { ...state, 'users_role': action.payload.data }
        default:
            return state;
    }
}