/**
 * This helper allows to control the database connections
 * and launching commands
 */

var config = require('config');
var MongoClient = require('mongodb').MongoClient;
function getUrl() {
    return `mongodb+srv://${config.databases.development.username}:${config.databases.development.password}@${config.databases.development.host}`
}
function getDB() {
    return config.databases.development.database;
}
module.exports = {
    insertOne: function (object, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                dbo.collection(collection).insertOne(object, function (err, res) {
                    if (err) reject(err);
                    db.close();
                    resolve(res);
                });
            });
        });
    },
    insertMany: function (object, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                dbo.collection(collection).insertMany(object, function (err, res) {
                    if (err) reject(err);
                    resolve(res);
                    db.close();
                });
            });
        });

    },
    findOne: function (object, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                dbo.collection(collection).findOne(object, function (err, res) {
                    if (err) reject(err);
                    resolve(res);
                    db.close();
                });
            });
        });
    },
    find: function (object, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                dbo.collection(collection).find(object).toArray(function (err, res) {
                    if (err) reject(err);
                    resolve(res);
                    db.close();
                });
            });
        });
    },
    deleteOne: function (object, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                dbo.collection(collection).deleteOne(object, function (err, res) {
                    if (err) reject(err);
                    resolve(res);
                    db.close();
                });
            });
        })
    },
    deleteMany: function (object, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                dbo.collection(collection).deleteMany(object, function (err, obj) {
                    if (err) reject(err);
                    db.close();
                    resolve(obj);
                });
            });
        });
    },
    updateOne: function (object, newObject, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                var newvalues = { $set: newObject };
                dbo.collection(collection).updateOne(object, newvalues, function (err, res) {
                    if (err) reject(err);
                    db.close();
                    resolve(res);
                });
            });
        })
    },
    updateMany: function (object, newObject, collection) {
        return new Promise((resolve, reject) => {
            MongoClient.connect(getUrl(), function (err, db) {
                if (err) reject(err);
                var dbo = db.db(getDB());
                var newvalues = { $set: newObject };
                dbo.collection(collection).updateMany(object, newvalues, function (err, res) {
                    if (err) reject(err);
                    db.close();
                    resolve(res);
                });
            });
        });
    },
    ObjectId: require('mongodb').ObjectId
}