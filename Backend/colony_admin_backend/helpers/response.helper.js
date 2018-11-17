/**
 * This helper allows to get a standard response, 
 * it is used with the database helper
 */

var respond = function(response, statusCode, message, data) {
    
    response.writeHead(statusCode, {
        'Content-Type': 'application/json'
    });

    response.end(JSON.stringify({
        message: message
        , data: data
    }));
};

module.exports = {
    respond: respond
};