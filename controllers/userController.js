const expressAsyncHandler = require('express-async-handler');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const Role = require('../models/Role');
const UserRole = require('../models/UserRole');

// Register user
const registerUser = expressAsyncHandler(async (req, res) => {
  const { name, email, password, roles } = req.body;

  if (!name || !email || !password || !Array.isArray(roles) || roles.length === 0) {
    res.status(400);
    throw new Error('All fields are required, and roles must be a non-empty array');
  }

  const hashedPassword = await bcrypt.hash(password, 10);

  const user = await User.create({ name, email, password: hashedPassword });

  const roleRecords = await Role.findAll({ where: { name: roles } });
  if (roleRecords.length !== roles.length) {
    res.status(400);
    throw new Error('Invalid roles provided');
  }

  const userRoles = roles.map(role => ({ userId: user.id, roleName: role }));
  await UserRole.bulkCreate(userRoles);

  res.status(201).json({ message: 'User registered successfully' });
});

// Login user
const loginUser = expressAsyncHandler(async (req, res) => {
  const { email, password } = req.body;

  const user = await User.findOne({ where: { email } });
  if (!user || !(await bcrypt.compare(password, user.password))) {
    res.status(401);
    throw new Error('Invalid credentials');
  }

  const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: '1h' });
  res.json({ token });
});

// Get current user
const getCurrentUser = expressAsyncHandler(async (req, res) => {
  const user = await User.findByPk(req.user.id, { include: Role });
  res.json(user);
});

// Get all users
const getAllUsers = expressAsyncHandler(async (req, res) => {
  const users = await User.findAll({ include: Role });
  res.json(users);
});

// Update user
const updateUser = expressAsyncHandler(async (req, res) => {
  const { name, roles } = req.body;

  const user = await User.findByPk(req.params.id);
  if (!user) {
    res.status(404);
    throw new Error('User not found');
  }

  if (name) user.name = name;
  if (roles) {
    await UserRole.destroy({ where: { userId: user.id } });
    const userRoles = roles.map(role => ({ userId: user.id, roleName: role }));
    await UserRole.bulkCreate(userRoles);
  }

  await user.save();
  res.json({ message: 'User updated successfully' });
});

// Delete user
const deleteUser = expressAsyncHandler(async (req, res) => {
  const user = await User.findByPk(req.params.id);
  if (!user) {
    res.status(404);
    throw new Error('User not found');
  }

  await user.destroy();
  res.json({ message: 'User deleted successfully' });
});

// Change password
const changePassword = expressAsyncHandler(async (req, res) => {
  const { oldPassword, newPassword } = req.body;

  if (!oldPassword || !newPassword) {
    res.status(400);
    throw new Error('Old password and new password are required');
  }

  const user = await User.findByPk(req.user.id);
  if (!user) {
    res.status(404);
    throw new Error('User not found');
  }

  const isPasswordCorrect = await bcrypt.compare(oldPassword, user.password);
  if (!isPasswordCorrect) {
    res.status(400);
    throw new Error('Old password is incorrect');
  }

  user.password = await bcrypt.hash(newPassword, 10);
  await user.save();

  res.json({ message: 'Password changed successfully' });
});

// Reset password
const resetPassword = expressAsyncHandler(async (req, res) => {
  const { email, newPassword } = req.body;
  console.log('Resetting password for:', email);

  if (!email || !newPassword) {
    res.status(400);
    throw new Error('Email and new password are required');
  }

  const user = await User.findOne({ where: { email } });
  if (!user) {
    res.status(404);
    throw new Error('User not found');
  }

  user.password = await bcrypt.hash(newPassword, 10);
  await user.save();

  res.json({ message: 'Password reset successfully' });
});

module.exports = { 
  registerUser, 
  loginUser, 
  getCurrentUser, 
  getAllUsers, 
  updateUser, 
  deleteUser, 
  changePassword, 
  resetPassword 
};
