var io = require('socket.io')();
io.sockets.on('connection', (socket) => {
    console.log("nuevo Cliente conectado con id: " + socket.id);
    socket.on('sos',(data)=>
    {
        io.sockets.emit('sos_request', data);
    });
});
module.exports = io;