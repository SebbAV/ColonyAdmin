import _ from 'lodash';
import { LOAD_EMP } from '../actions';


export default function (state = {}, action) {
    switch (action.type) {
        
        case LOAD_EMP:
            return action.payload;
        default:
            return state;
    }
}