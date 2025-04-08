const { Sequelize } = require('sequelize');

const db = new Sequelize('bloodbank', 'root', 'root', {
  host: 'localhost',
  dialect: 'mysql',
  logging: false,
});

module.exports = db;
