var express = require('express');
var router = express.Router();
var crypto = require('crypto');
var mongodbHelper = require('../../../helpers/mongodb.helper');
var responseHelper = require('../../../helpers/response.helper');
var forgotHelper = require('../../../helpers/forgot.helper');
var generator = require('generate-password');
var dateHelper = require('../../../helpers/date.helper');

router.post('/assign_code/', function (req, res, next) {
    var credentials = req.body;
    if (!credentials.neighbor_id ||
        !credentials.visitor_id) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var code = generator.generate({
        length: 5,
        numbers: true
    });
    var object = {
        visitor_id: mongodbHelper.ObjectId(credentials.visitor_id),
        neighbor_id: mongodbHelper.ObjectId(credentials.neighbor_id),
        invitation_code: code.toUpperCase()
    }
    mongodbHelper.insertOne(object, "invitation").then((data) => {
        responseHelper.respond(res, 200, undefined, data);
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
router.get('/invitations/', (req, res, next) => {
    mongodbHelper.find({}, "invitation").then((data) => {
        responseHelper.respond(res, 200, undefined, data);
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
router.get('/', (req, res, next) => {
    mongodbHelper.find({}, "visit").then((data) => {
        responseHelper.respond(res, 200, undefined, data);
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    })
});
router.post('/user/', (req, res, next) => {
    var invitation = req.body;
    if (!invitation.code) {
        responseHelper.respond(res, 404, "No invitation code");
        return;
    }
    var object = {
        invitation_code: invitation.code
    }
    mongodbHelper.findOne(object, "invitation").then((data) => {
        if (!data) {
            responseHelper.respond(res, 404, "No invitation code");
            return;
        }
        var userQuery =
        {
            _id: mongodbHelper.ObjectId(data.visitor_id)
        }
        mongodbHelper.findOne(userQuery, "user").then((visitor) => {
            if (!visitor) {
                responseHelper.respond(res, 404, "No existing user");
                return;
            }
            var userQuery =
            {
                _id: mongodbHelper.ObjectId(data.neighbor_id)
            }
            mongodbHelper.findOne(userQuery, "user").then((neighbor) => {
                if (!neighbor) {
                    responseHelper.respond(res, 404, "No existing user");
                    return;
                }
                var visit =
                {
                    name: visitor.first_name,
                    address: neighbor.address,
                    entrance_date: dateHelper.getCurrentDatetime(),
                    exit_date: "",
                    vehicle: visitor.vehicle,
                    object_invitation_uid: mongodbHelper.ObjectId(data._id),
                    object_neighbor_uid: mongodbHelper.ObjectId(neighbor._id),
                    object_visitor_uid: mongodbHelper.ObjectId(visitor._id)
                }
                mongodbHelper.insertOne(visit, "visit").then((success) => {
                    responseHelper.respond(res, 200, undefined, success);
                }).catch((error) => {
                    responseHelper.respond(res, 500, error);
                })
            }).catch((error) => {
                responseHelper.respond(res, 500, error);
            })
        }).catch((error) => {
            responseHelper.respond(res, 500, error);
        })
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
router.post('/no_user/', (req, res, next) => {
    var visitor = req.body;
    if (!visitor.first_name ||
        !visitor.last_name ||
        !visitor.vehicle ||
        !visitor.address ||
        !visitor.address_number) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    mongodbHelper.insertOne(visitor, "visitor").then((data) => {
        var addressQuery = {
            $and: [
                { address: visitor.address },
                { address_number: visitor.address_number }
            ]
        };
        mongodbHelper.findOne(addressQuery, "user").then((neighbor) => {
            var visit = {
                name: visitor.first_name,
                address: neighbor.address,
                entrance_date: dateHelper.getCurrentDatetime(),
                exit_date: "",
                vehicle: visitor.vehicle,
                object_neighbor_uid: mongodbHelper.ObjectId(neighbor._id),
                object_visitor_uid: mongodbHelper.ObjectId(data.ops[0]._id)
            };
            mongodbHelper.insertOne(visit, "visit").then((success) => {
                responseHelper.respond(res, 200, undefined, success);
            }).catch((error) => {
                responseHelper.respond(res, 500, error);
            })
        }).catch((error) => {
            responseHelper.respond(res, 500, error);
        })
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
router.post('/exit', (req, res, next) => {
    var vehicle = req.body;
    if (!vehicle.vehicle) {
        responseHelper.respond(res, 400, 'Bad request. The request was missing some parameters.');
        return;
    }
    var queryObject = {
        $and: [
            { vehicle: vehicle.vehicle },
            { exit_date: "" }
        ]
    };
    mongodbHelper.updateMany(queryObject, { exit_date: dateHelper.getCurrentDatetime() }, "visit").then((success) => {
        responseHelper.respond(res, 200, undefined, success);
    }).catch((error) => {
        responseHelper.respond(res, 500, error);
    });
});
module.exports = router;