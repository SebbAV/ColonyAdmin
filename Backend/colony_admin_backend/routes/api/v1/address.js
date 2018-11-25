var express = require('express');
var router = express.Router();
var mongodbHelper = require('../../../helpers/mongodb.helper');
var responseHelper = require('../../../helpers/response.helper');

router.post('/', (req, res, next) => {
    var address = req.body;
    if (!address.address) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var registeredObject = {
        address: address.address
    }
    mongodbHelper.findOne(registeredObject, "address").then(function (success) {
        if (!success) {
            mongodbHelper.insertOne(address, "address").then(function (success) {
                responseHelper.respond(res, 200, "Inserted Correctly", success);
            }).catch(function (error) {
                responseHelper.respond(res, 500, error);
            });
        } else {
            responseHelper.respond(res, 409, "Address already registered.", null);
        }
    }).catch(function (error) {
        responseHelper.respond(res, 500, error);
    });
});
router.get('/', (req, res, next) => {
    var addresses = []
    mongodbHelper.find({}, "address").then((data) => {
        mongodbHelper.find({}, "user").then((users) => {
            data.forEach(address => {
                address.users = []
                users.forEach(user => {
                    if (address._id == user.address) {
                        address.users.push(user);
                    }
                });
            });
            responseHelper.respond(res, 200, undefined, data);
        }).catch((error) => {
            responseHelper.respond(res, 500, error);
        });
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
module.exports = router;