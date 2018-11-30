import { combineReducers } from 'redux';
import {reducer as formReducer} from 'redux-form';
import loginReducer from './reducer_login'
import neighborReducer from './reducer_users'
import prefillReducer from './reducer_prefill_form'
import addressReducer from './reducer_address'
import visitorReducer from './reducer_visitors'
const rootReducer = combineReducers({
  form:formReducer,
  login:loginReducer,
  users: neighborReducer,
  prefill: prefillReducer,
  addresses: addressReducer,
  visitors: visitorReducer
});

export default rootReducer;