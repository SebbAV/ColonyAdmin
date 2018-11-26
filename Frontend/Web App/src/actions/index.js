import axios from 'axios';

const IP = 'akarokhome.ddns.net'
const API_URL = `http://${IP}:3000`

export const USER_LOGIN = 'user_login';
export const USER_REGISTER = 'user_register';
export const GET_NEIGHBOR = 'get_neighbor';
export const GET_USER = 'get_user';
export const ADD_ADDRESS = 'add_address';

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
export function getUsersByRole(values){
    const request = axios.get(`${API_URL}/v1/user/get_by_role/4/`)
    return {
        type: GET_USER,
        payload: request
    }
}
export function getUserGroupedByAddress(values){
    const request = axios.get(`${API_URL}/v1/address/grouped_addresses`)
    return {
        type: GET_NEIGHBOR,
        payload: request
    }
}
export function addUserAddress(values,callback){
    return (axios.post(`${API_URL}/v1/address/`,values).then((response) => {
        callback()
        return {
            type: USER_LOGIN,
            payload: response
        }
    }))
}

