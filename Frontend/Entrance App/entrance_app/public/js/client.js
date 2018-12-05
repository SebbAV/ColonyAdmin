var socket = io('http://akarokhome.ddns.net:3000');
var local_socket = io('http://localhost:3000');

socket.on('allow_entrance_check', (data) => {
    local_socket.emit('local_allow_entrance', data);
});
socket.on('sos_request', (data) => {
    console.log(data);
    local_socket.emit('local_sos_request', data);
});
socket.on('sos_finished', (data) => {
    console.log(data);
    local_socket.emit('local_sos_finished', data);
});