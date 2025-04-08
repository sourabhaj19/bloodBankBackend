const { DataTypes } = require('sequelize');
const db = require('../config/db');
const User = require('./User');
const Role = require('./Role');

const UserRole = db.define('UserRole', {
  userId: {
    type: DataTypes.BIGINT,
    references: {
      model: User,
      key: 'id',
    },
  },
  roleName: {
    type: DataTypes.STRING,
    references: {
      model: Role,
      key: 'name',
    },
  },
});

// Associations
User.belongsToMany(Role, { through: UserRole, foreignKey: 'userId' });
Role.belongsToMany(User, { through: UserRole, foreignKey: 'roleName' });

module.exports = UserRole;
