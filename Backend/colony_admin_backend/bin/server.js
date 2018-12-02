var io = require('socket.io')();
var visitors = []
io.sockets.on('connection', (socket) => {
    console.log("nuevo Cliente conectado con id: " + socket.id);
    socket.on('sos', (data) => {
        io.sockets.emit('sos_request', data);
    });
    socket.on('allow_entrance', (data) => {
        io.sockets.emit('allow_entrance_check', data);
    });
    socket.on('visitor', (data) => {
        visitors.push(data);
    });
    socket.on('visitor_location', (data) => {
        io.sockets.emit('visitors_location', visitors);
    });
    socket.on('visitor_exit', (data) => {
        //code to pop from visitors array.
        console.log(data);
    });
});
module.exports = io;