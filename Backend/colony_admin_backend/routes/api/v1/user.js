var express = require('express');
var router = express.Router();
var crypto = require('crypto');
var mongodbHelper = require('../../../helpers/mongodb.helper');
var responseHelper = require('../../../helpers/response.helper');
var forgotHelper = require('../../../helpers/forgot.helper');
var generator = require('generate-password');

/* GET users listing. */
router.get('/', function (req, res, next) {
    mongodbHelper.find({}, "user").then(function (success) {
        responseHelper.respond(res, 200, undefined, success);
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.get('/get_by_role/:role', function (req, res, next) {
    var role = req.params.role;
    if (!role) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var queryObject =
    {
        role: role
    }
    mongodbHelper.find(queryObject, "user").then(function (success) {
        responseHelper.respond(res, 200, undefined, success);
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.post('/', function (req, res) {
    var credentials = req.body;
    if (!credentials.email ||
        !credentials.first_name ||
        !credentials.last_name ||
        !credentials.password ||
        !credentials.address ||
        !credentials.phone ||
        !credentials.role) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var sha1HashedPassword = crypto.createHash('sha1').update(credentials.password).digest('hex');
    var object =
    {
        email: credentials.email,
        password: sha1HashedPassword,
        first_name: credentials.first_name,
        last_name: credentials.last_name,
        address: credentials.address,
        phone: credentials.phone,
        role: credentials.role,
        vehicle: credentials.vehicle
    }
    var registeredObject = {
        email: credentials.email
    }
    mongodbHelper.findOne(registeredObject, "user").then(function (success) {
        if (!success) {
            mongodbHelper.insertOne(object, "user").then(function (success) {
                responseHelper.respond(res, 200, "Inserted Correctly", success);
            }).catch(function (error) {
                responseHelper.respond(res, 500, error);
            });
        } else {
            responseHelper.respond(res, 409, "User already registered.", null);
        }

    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.post('/login', function (req, res) {
    var credentials = req.body;
    if (!credentials.email || !credentials.password) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var sha1HashedPassword = crypto.createHash('sha1').update(credentials.password).digest('hex');
    var object =
    {
        $and: [
            { email: credentials.email },
            { password: sha1HashedPassword }]
    }
    mongodbHelper.findOne(object, "user").then(function (success) {
        if (!success)
            responseHelper.respond(res, 404, "Not found", "Wrong email or password.");
        else
            responseHelper.respond(res, 200, undefined, success);
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });

});
router.post('/forgot', function (req, res) {
    var emailObject = req.body;
    if (!emailObject.email) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var code = generator.generate({
        length: 5,
        numbers: true
    });
    var object =
    {
        email: emailObject.email,
        code: code
    }
    mongodbHelper.insertOne(object, "code").then(function (success) {
        forgotHelper.sendMail(emailObject.email, code).then(function (success) {
            responseHelper.respond(res, 200, undefined, success);
        }).catch(function (error) {
            responseHelper.respond(res, 500, error);
        });
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.put('/password_reset', function (req, res) {
    var user = req.body;
    if (!user.email || !user.password) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var sha1HashedPassword = crypto.createHash('sha1').update(user.password).digest('hex');
    var object =
    {
        email: user.email
    };
    var newPassword =
    {
        password: sha1HashedPassword
    }
    mongodbHelper.findOne(object, "user").then(function (success) {
        if (!success) {
            responseHelper.respond(res, 400, "User don't exist.");
        }
        else {
            mongodbHelper.updateOne(object, newPassword, "user").then(function (success_two) {
                responseHelper.respond(res, 200, "Password modified.", success_two)
            }).catch(function (error) {
                responseHelper.respond(res, 500, error);
            });
        }
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.put('/', function (req, res) {
    var credentials = req.body;
    if (!credentials.email ||
        !credentials.first_name ||
        !credentials.last_name ||
        !credentials.password ||
        !credentials.address ||
        !credentials.phone ||
        !credentials.role ||
        !credentials.vehicle ||
        !credentials._id) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var ObjectId = mongodbHelper.ObjectId;
    var object =
    {
        _id: ObjectId(credentials._id)
    }
    mongodbHelper.updateOne(object, credentials, "user").then(function (data) {
        responseHelper.respond(res, 200, "User modified.", data)
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.get('/check_code/:code', function (req, res) {
    var code = req.params.code;
    if (!code) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var object =
    {
        code: code
    }
    mongodbHelper.findOne(object, "code").then(function (success) {
        if (!success)
            responseHelper.respond(res, 404, "Not Found", "Code does not exists.");
        else
            responseHelper.respond(res, 200, undefined, success);
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
module.exports = router;
