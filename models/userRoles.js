const sequelize = require('sequelize');
const db = require('../config/db');

const User = db.define('user_roles', 
  {
    user_id : {
      type: sequelize.BIGINT,
      primaryKey: true,
      references: {
        model: 'users',
        key: 'id'
      }
    },
    role_name : {
      type: sequelize.STRING,
      references: {
        model: 'roles',
        key: 'name'
      }
    },
  }
)