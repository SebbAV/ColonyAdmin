var config = require('config');
var socket = require('socket.io-client')(config.socket_server);
module.exports = {
    allowEntrance(allow) {
        socket.emit('allow_entrance', allow);
    }
}