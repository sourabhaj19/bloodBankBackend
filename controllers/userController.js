const express = require('express');
const db = require('../config/db');
const expressAsyncHandler = require('express-async-handler');
const User = require('../models/userModel');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const dotenv  =  require('dotenv').config();

const registerUser = expressAsyncHandler(async (req, res) => {
  console.log(req.body);
  const { username, email, password } = req.body;
  if(!username || !email || !password){
    res.status(400);
    throw new Error('Please fill all the fields');
  }
  const availableUser = await User.findOne({where : {email}});
  if(availableUser){
    res.status(400);
    throw new Error('User already exists');
  }
  const hashedPassword = await bcrypt.hash(password, 10);
  console.log("hashedPassword",hashedPassword);
  const user = await User.create({username, email, password: hashedPassword});
  res.status(201).json({username : user.username, email : user.email});
})


const login = expressAsyncHandler(async (req, res) => {
  const { email, password } = req.body;
  if(!email || !password){
    res.status(400);
    throw new Error('Please fill all the fields');
  }
  const user = await User.findOne({where : {email}});
  if(!user){
    res.status(400);
    throw new Error('User does not exist');
  }
  const isPasswordCorrect = await bcrypt.compare(password, user.password);
  if(!isPasswordCorrect){
    res.status(400);
    throw new Error('Invalid credentials');
  }else{
  const accessToken = jwt.sign({
    user :{
      user : user.username,
      email : user.email,
      id : user.id
    }
  }, process.env.ACCESS_TOKEN_SECRET, {
    expiresIn : "1440m"
  });
  res.status(200).json({name : user.username, email : user.email, token: accessToken});
}
})

const currentUser = expressAsyncHandler(async (req, res) => {
  res.json(req.user);
})

module.exports = {registerUser, login,currentUser};