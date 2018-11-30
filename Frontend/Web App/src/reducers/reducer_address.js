import _ from 'lodash';
import { GET_ADDRESS, GET_VISITORS } from '../actions';

export default function (state = {}, action) {
    switch (action.type) {
        case GET_ADDRESS:
            return { ...state, 'addresses': action.payload.data }
        case GET_VISITORS:
        return { ...state, 'visitors': action.payload.data }
        default:
            return state;
    }
}