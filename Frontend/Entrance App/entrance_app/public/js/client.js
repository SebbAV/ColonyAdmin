var socket = io('http://akarokhome.ddns.net:3000');
var local_socket = io('http://localhost:3000');

socket.on('allow_entrance_check', (data) => {
    local_socket.emit('local_allow_entrance', data);
}); 