'use strict';
module.exports = (sequelize, DataTypes) => {
  const Pharmacy = sequelize.define('Pharmacy', {
    name: DataTypes.STRING,
    city_id: DataTypes.STRING,
    longitude: DataTypes.STRING,
    latitude: DataTypes.STRING,
    check_in: DataTypes.STRING,
    check_out: DataTypes.STRING,
    facebook_page: DataTypes.STRING,
    phone: DataTypes.STRING,
    address: DataTypes.STRING
  }, {});
  Pharmacy.associate = function(models) {
    // associations can be defined here
  };
  return Pharmacy;
};
