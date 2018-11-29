import _ from 'lodash';
import { GET_ADDRESS } from '../actions';

export default function (state = {}, action) {
    switch (action.type) {
        case GET_ADDRESS:
            return { ...state, 'addresses': action.payload.data }
        default:
            return state;
    }
}