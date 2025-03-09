const { Sequelize } = require('sequelize');

const db = new Sequelize('bloodbank', 'root', 'root', {
  host: 'localhost',
  dialect: 'mysql',
  logging: console.log // Enables SQL logging for debugging
});

module.exports = db;
