import _ from 'lodash';
import { GET_NEIGHBOR } from '../actions';

export default function (state = {}, action) {
    switch (action.type) {
        case GET_NEIGHBOR:
            console.log(action.payload.data)
            return {...state,'users': action.payload.data}
        default:
            return state;
    }
}