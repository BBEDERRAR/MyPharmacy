let city = require('./api/v1/city')
let pharmacy = require('./api/v1/pharmacy')
let user = require('./api/v1/user')
let order = require('./api/v1/order')


module.exports = function (app) {
    app.use('/api/v1/city',city)
    app.use('/api/v1/pharmacy',pharmacy)
    app.use('/api/v1/user',user)
    app.use('/api/v1/order',order)
}



