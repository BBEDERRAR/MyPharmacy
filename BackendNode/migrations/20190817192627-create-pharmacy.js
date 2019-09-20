'use strict';
module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.createTable('Pharmacies', {
            id: {
                allowNull: false,
                autoIncrement: true,
                primaryKey: true,
                type: Sequelize.INTEGER
            },
            name: {
                type: Sequelize.STRING
            }, city_id: {
                type: Sequelize.INTEGER
            }, longitude: {
                type: Sequelize.INTEGER
            }, latitude: {
                type: Sequelize.INTEGER
            }, check_in: {
                type: Sequelize.STRING
            }, check_out: {
                type: Sequelize.STRING
            }, facebook_page: {
                type: Sequelize.STRING
            }, address: {
                type: Sequelize.STRING
            }, phone: {
                type: Sequelize.STRING
            },
            createdAt: {
                allowNull: true,
                type: Sequelize.DATE
            },
            updatedAt: {
                allowNull: true,
                type: Sequelize.DATE
            }
        });
    },
    down: (queryInterface, Sequelize) => {
        return queryInterface.dropTable('Pharmacies');
    }
};
