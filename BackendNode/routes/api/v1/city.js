let express = require('express')
let router = express.Router()
let City = require('../../../models/index').City
let Pharmacy = require('../../../models/index').Pharmacy

// FETCH all Cites
router.get('/', function (req, res) {
    City.findAll().then(Citeis => {
        res.send(Citeis);
    });
})

// Find a City by Id
router.get('/:id', function (req, res) {
    City.findByPk(req.params.id).then(City => {
        res.send(City);
    })
})


// Post a City
router.post('/', function (req, res) {
    City.create({...req.body}).then(function (result) {
        res.send(result)
    })
})

// Update a City
router.patch('/:id', function (req, res) {
    City.findByPk(req.params.id).then(City => {
        City.update({...req.body}).then(function (result) {
            res.send(result)
        })
    })
})

// Find a Pharmacies By City by Id
router.get('/:city_id/pharmacies', function (req, res) {
    Pharmacy.findAll({
        where: {city_id: req.params.city_id}
    }).then(Pharmacies => {
        res.send(Pharmacies);
    })
})

module.exports = router
