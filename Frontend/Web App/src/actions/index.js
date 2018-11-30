import axios from 'axios';

const IP = 'akarokhome.ddns.net'
const API_URL = `http://${IP}:3000`

export const USER_LOGIN = 'user_login';
export const USER_REGISTER = 'user_register';
export const GET_NEIGHBOR = 'get_neighbor';
export const GET_USER = 'get_user';
export const ADD_ADDRESS = 'add_address';
export const LOAD_EMP = 'load_emp';
export const ADD_VISIT = 'add_visit';
export const GET_ADDRESS ='get_address';
export const GET_VISITORS ='get_visitors';

export function loginUser(values, callback) {
    return (axios.post(`${API_URL}/v1/user/login`, values).then((response) => {
        callback()
        return {
            type: USER_LOGIN,
            payload: response
        }
    }))
}
export function registerUser(values, callback) {
    values["role"] = "4"
    const request = axios.post(`${API_URL}/v1/user/`, values).then((response) => {
        callback()
    })
    return {
        type: USER_REGISTER,
        payload: request
    }
}
export function getUsersByRole(values) {
    const request = axios.get(`${API_URL}/v1/user/get_by_role/4/`)
    return {
        type: GET_USER,
        payload: request
    }
}
export function getUserGroupedByAddress(values) {
    const request = axios.get(`${API_URL}/v1/address/grouped_addresses`)
    return {
        type: GET_NEIGHBOR,
        payload: request
    }
}
export function addUserAddress(values, callback) {
    return (axios.post(`${API_URL}/v1/address/`, values).then((response) => {
        callback()
        
        return {
            type: USER_LOGIN,
            payload: response
        }
    }))
}
export function createVisit(values, callback) {
    values["address_number"] = parseInt(values["address_number"]);
    console.log(values)
    return (axios.post(`${API_URL}/v1/visit/no_user/`, values).then((response) => {
        callback()
        return {
            type: ADD_VISIT,
            payload: response
        }                                                                                                                                                                                                                                                                                                                                                                                                       
    }))
}
export function loadVisitors(){

    const request =  axios.get(`${API_URL}/v1/visit`)
    return {
        type: GET_VISITORS,
        payload: request
    }
}
export function loadAddress(values){

    const request =  axios.get(`${API_URL}/v1/address`)
    return {
        type: GET_ADDRESS,
        payload: request
    }
}

export function sample(values) {
    console.log(values)
    return {
        type: LOAD_EMP,
        payload: values
    }
}
