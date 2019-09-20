'use strict';
module.exports = (sequelize, DataTypes) => {
    const City = sequelize.define('City', {
        name: DataTypes.STRING,
        code: DataTypes.STRING,
    }, {});
    City.associate = function (models) {
        // associations can be defined here
    };
    return City;
};
