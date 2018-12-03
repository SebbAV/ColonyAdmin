var io = require('socket.io')();
var PythonShell = require('python-shell');
var opt =
{
    scriptPath: __dirname + "/scripts/",
    args: []
}
io.sockets.on('connection', (socket) => {
    console.log("nuevo Cliente conectado con id: " + socket.id);
    socket.on('local_allow_entrance', (data) => {
        opt.args = []
        opt.args.push(data);
        PythonShell.run("entrance.py", opt, (err, res) => {
            opt.args = []
            if (err) {
                throw err;
            }
            else {
                console.log(res[0])
            }
        });
    });
    socket.on('local_sos_request',(data)=>{
        opt.args = []
        opt.args.push(JSON.stringify(data));
        PythonShell.run("sos.py", opt, (err, res) => {
            opt.args = []
            if (err) {
                throw err;
            }
            else {
                console.log(res[0])
            }
        });
    })
});
module.exports = io;