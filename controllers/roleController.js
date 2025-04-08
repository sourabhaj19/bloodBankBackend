const expressAsyncHandler = require('express-async-handler');
const Role = require('../models/Role');

// Create a new role
const createRole = expressAsyncHandler(async (req, res) => {
  const { name } = req.body;

  if (!name) {
    res.status(400);
    throw new Error('Role name is required');
  }

  const roleExists = await Role.findByPk(name);
  if (roleExists) {
    res.status(400);
    throw new Error('Role already exists');
  }

  const role = await Role.create({ name });
  res.status(201).json(role);
});

// Get all roles
const getRoles = expressAsyncHandler(async (req, res) => {
  const roles = await Role.findAll();
  res.json(roles);
});

// Update a role
const updateRole = expressAsyncHandler(async (req, res) => {
  const { name } = req.body;
  const { roleName } = req.params;

  const role = await Role.findByPk(roleName);
  if (!role) {
    res.status(404);
    throw new Error('Role not found');
  }

  if (name) {
    const roleExists = await Role.findByPk(name);
    if (roleExists) {
      res.status(400);
      throw new Error('Role with the new name already exists');
    }
    role.name = name;
  }

  await role.save();
  res.json({ message: 'Role updated successfully', role });
});

// Delete a role
const deleteRole = expressAsyncHandler(async (req, res) => {
  const { roleName } = req.params;

  const role = await Role.findByPk(roleName);
  if (!role) {
    res.status(404);
    throw new Error('Role not found');
  }

  await role.destroy();
  res.json({ message: 'Role deleted successfully' });
});

module.exports = { createRole, getRoles, updateRole, deleteRole };
