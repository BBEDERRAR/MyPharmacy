let express = require('express')
let router = express.Router()
let multer = require('multer');
let Order = require('../../../models/index').Order

// FETCH all Orders
router.get('/', function (req, res) {
    Order.findAll().then(Orders => {
        res.send(Orders);
    });
})

// Find a Order by Id
router.get('/:id', function (req, res) {
    Order.findByPk(req.params.id).then(Order => {
        res.send(Order);
    })
})


// Post a Order
router.post('/', function (req, res) {
    let storage = multer.diskStorage({
        destination: (req, file, cb) => {
            cb(null, 'public/prescription');
        },
        filename: (req, file, cb) => {
            cb(null, Date.now() + file.originalname);
        }
    });
    let upload = multer({storage: storage}).single('file');
    upload(req, res, function (err) {
        if (err) {
            res.send({
                message: err
            });
        } else {
            Order.create({
                ...req.body,
                img: "",//req.file.filename
            }).then(function (result) {
                res.send(result)
            })
        }
    })
})

// Update a Order
router.patch('/:id', function (req, res) {
    Order.findByPk(req.params.id).then(Order => {
        Order.update({...req.body}).then(function (result) {
            res.send(result)
        })
    })
})

// Find a Orders By User by Id
router.get('/:user_id/my_orders', function (req, res) {
    Order.findAll({
        where: {user_id: req.params.user_id}
    }).then(Orders => {
        res.send(Orders);
    })
})

module.exports = router
