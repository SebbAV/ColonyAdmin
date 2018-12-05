var io = require('socket.io')();
var visitors = []
io.sockets.on('connection', (socket) => {
    console.log("nuevo Cliente conectado con id: " + socket.id);
    socket.on('sos', (data) => {
        console.log(data);
        io.sockets.emit('sos_request', data);
    });
    socket.on('sos_finish', (data) => {
        io.sockets.emit('sos_finished', data);
    });
    socket.on('allow_entrance', (data) => {
        io.sockets.emit('allow_entrance_check', data);
    });
    socket.on('visitor', (data) => {
        var check = false
        visitors.forEach(element => {
            if (element.userId == data.userId)
                check = true
        });
        if (!check)
            visitors.push(data);
    });
    socket.on('visitor_location', (data) => {
        console.log(data);
        visitors.forEach(element => {
            if (element.userId == data.userId)
                element = data
        });
        io.sockets.emit('visitors_location', visitors);
    });
    socket.on('visitor_exit', (data) => {
        //code to pop from visitors array.
        visitors.remove((el) => { return el.idUser === data.idUser; });
    });
});
module.exports = io;