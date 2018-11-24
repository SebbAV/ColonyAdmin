var express = require('express');
var router = express.Router();
var crypto = require('crypto');
var mongodbHelper = require('../../../helpers/mongodb.helper');
var responseHelper = require('../../../helpers/response.helper');
var forgotHelper = require('../../../helpers/forgot.helper');
var generator = require('generate-password');

router.post('/assign_code/', function (req, res, next) {
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
    var code = generator.generate({
        length: 5,
        numbers: true
    });
    var object = {
        object_user_uid: mongodbHelper.ObjectId(credentials._id),
        invitation_code: code
    }
    mongodbHelper.insertOne(object, "invitation").then((data) => {
        responseHelper.respond(res, 200, undefined, data);
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
router.get('/invitations', (req, res, next) => {
    mongodbHelper.find({}, "invitation").then((data) => {
        responseHelper.respond(res, 200, undefined, data);
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});

module.exports = router;