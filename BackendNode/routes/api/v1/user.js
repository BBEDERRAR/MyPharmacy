let express = require('express');
let router = express.Router();
const generate = require("nanoid/generate");
const config = require('../../../config/config.json');
const jwt = require('jsonwebtoken');
const env = process.env.NODE_ENV || 'development';

// users hardcoded for simplicity, store in a db for production applications
let User = require('../../../models/index').User;


const generatePassword = () => {
    const password = generate("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 6);
    return password
};


// Post a New User
router.post('/sign_up', async function (req, res) {
    console.log(req.body)
    await User.findAll({where: {phone: req.body.phone}}).then(user => {
        if (user.length) {
            res.status(400).json({message: 'This phone already exists'})
        } else {
            const newPassword = generatePassword();
            const accountSid = config[env].accountSid; // Your Account SID from www.twilio.com/console
            const authToken = config[env].authToken;   // Your Auth Token from www.twilio.com/console
            const client = require('twilio')(accountSid, authToken);

            User.create({...req.body, password: newPassword, activated: false}).then(function (result) {
                console.log(req.body.phone)
                client.messages.create({
                    body: `Hello ${req.body.firstName} ${req.body.lastName} \n Your account created successfully \n The password: ${newPassword}`,
                    from: config[env].phoneNumber, // From a valid Twilio number,
                    to: req.body.phone  // Text this number
                }).then((message) => {
                    console.log(message)
                    res.send(result)
                }).catch((err) => {
                    console.log(err)
                })

            })
        }
    })


})


router.post('/authenticate', function (req, res) {
    User.findOne({
        where: {
            phone: req.body.phone,
            password: req.body.password
        }
    })
        .then(user => {
            if (user) {
                const token = jwt.sign({sub: user.id}, config[env].secret);
                delete user.password
                res.status(200).send({
                    message: "Login Successfully",
                    status: true,
                    user,
                    token
                });
            } else {
                res.status(400).json({
                    status: false,
                    user: null,
                    token: null,
                    message: 'phone or password is incorrect'
                });
            }
        });

});
router.post('/change_password', function (req, res) {
    User.findOne({
        where: {
            phone: req.body.phone,
            password: req.body.old_password
        }
    })
        .then(user => {
            if (user) {
                user.update({
                    password: req.body.new_password,
                    activated: 1
                }).then(function (result) {
                    res.status(200).send({
                        message: "Password Changed Successfully",
                        status: true,
                        user,
                        token: ""
                    });
                })
            } else {
                res.status(400).send({
                    message: "Password Is Wrong !!",
                    status: false,
                    user,
                    token: ""
                });
            }
        });

});

router.get('/', function (req, res) {
    User.findAll()
        .then(Users => {
            res.json(Users);
        });
});

router.patch('/:id', function (req, res) {
    User.findByPk(req.params.id).then((user) => {
        user.update({...req.body})
            .then(function () {
                res.send({
                    success: true
                });
            });
    });
});


module.exports = router;
