let express = require('express')
let router = express.Router()
let Pharmacy = require('../../../models/index').Pharmacy

// FETCH all Pharmacies
router.get('/', function (req, res) {
    Pharmacy.findAll().then(Pharmacies => {
        res.send(Pharmacies);
    });
})

// Find a Pharmacy by Id
router.get('/:id', function (req, res) {
    Pharmacy.findByPk(req.params.id).then(Pharmacy => {
        res.send(Pharmacy);
    })
})

// Post a Pharmacy
router.post('/', function (req, res) {
    Pharmacy.create({...req.body}).then(function (result) {
        res.send(result)
    })
})

// Update a Pharmacy
router.patch('/:id', function (req, res) {
    Pharmacy.findByPk(req.params.id).then(player => {
        player.update({...req.body}).then(function (result) {
            res.send(result)
        })
    })
})




// Delete a Pharmacy by Id
router.delete('/:id', function (req, res) {
    const id = req.params.id
    Pharmacy.destroy({
        where: {id: id}
    }).then(() => {
        res.status(200).send('deleted successfully a the player with id = ' + id);
    })
})

module.exports = router
