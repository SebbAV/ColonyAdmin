var nodemailer = require('nodemailer');
var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: '1934.aigame@gmail.com',
        pass: 'alcatraz_1934'
    }
});
module.exports =
    {
        sendMail: function (email, code) {
            var mailOptions = {
                from: '1934.aigame@gmail.com',
                to: email,
                subject: 'Renew Password 1934',
                text: "code: " + code.toUpperCase()
            };
            return new Promise((resolve, reject) => {
                transporter.sendMail(mailOptions, function (error, info) {
                    if (error) reject(error);
                    resolve(info);
                });
            });

        }
    }
