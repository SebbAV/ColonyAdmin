var express = require('express');
var router = express.Router();
var crypto = require('crypto');
var mongodbHelper = require('../../../helpers/mongodb.helper');
var responseHelper = require('../../../helpers/response.helper');

/* GET users listing. */
router.get('/', function (req, res, next) {
    res.send('respond with a resource');
});
router.post('/', function (req, res) {
    var credentials = req.body;
    if (!credentials.email ||
        !credentials.first_name ||
        !credentials.last_name ||
        !credentials.password ||
        !credentials.address ||
        !credentials.phone ||
        !credentials.role ||
        !credentials.vehicle) {
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
module.exports = router;
