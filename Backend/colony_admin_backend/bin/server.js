var io = require('socket.io')();
io.sockets.on('connection', (socket) => {
    console.log("nuevo Cliente conectado con id: " + socket.id);
    socket.on('sos', (data) => {
        io.sockets.emit('sos_request', data);
    });
    socket.on('allow_entrance', (data) => {
        io.sockets.emit('allow_entrance_check', data);
    });
});
module.exports = io;