const expressAsyncHandler = require('express-async-handler');
const User = require('../models/userModel');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const registerUser = expressAsyncHandler(async (req, res) => {
  console.log(req.body);
  
  const { name, username, phone, email, password, bloodGroup, location, role, latitude, longitude, isAvailable } = req.body;

  // Ensure required fields are present
  if (!name || !username || !phone || !email || !password || !bloodGroup || !location || !role) {
    res.status(400);
    throw new Error('Please fill all the required fields');
  }

  // Convert to uppercase to match ENUM definitions
  const formattedBloodGroup = bloodGroup.toUpperCase();
  const formattedRole = role.toUpperCase();

  // Validate ENUM values
  const validBloodGroups = ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'];
  const validRoles = ['DONOR', 'SEEKER', 'BOTH'];

  if (!validBloodGroups.includes(formattedBloodGroup)) {
    res.status(400);
    throw new Error(`Invalid blood group. Allowed values: ${validBloodGroups.join(', ')}`);
  }

  if (!validRoles.includes(formattedRole)) {
    res.status(400);
    throw new Error(`Invalid role. Allowed values: ${validRoles.join(', ')}`);
  }

  // Check if user already exists
  const existingUser = await User.findOne({ where: { email } });
  if (existingUser) {
    res.status(400);
    throw new Error('User already exists');
  }

  // Hash password
  const hashedPassword = await bcrypt.hash(password, 10);
  console.log("hashedPassword", hashedPassword);

  // Create user
  const user = await User.create({
    name,
    username: username, // Adjusted field name to match model
    phone,
    email,
    password: hashedPassword,
    blood_group: formattedBloodGroup, // Corrected field name
    location,
    role: formattedRole, // Corrected field name
    latitude: latitude || null,
    longitude: longitude || null,
    is_available: isAvailable !== undefined ? isAvailable : true // Default to true
  });

  // Send response
  res.status(201).json({
    message : 'Registered successfully',
  });
});


const login = expressAsyncHandler(async (req, res) => {
  const { username, password } = req.body;
  if(!username || !password){
    res.status(400);
    throw new Error('Please fill all the fields');
  }
  const user = await User.findOne({where : {username}});
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
  delete user.dataValues.password
  res.status(200).json( {...user.dataValues
    , token: accessToken});
}
})

const currentUser = expressAsyncHandler(async (req, res) => {
  res.json(req.user);
})

const getSearchedBlood = expressAsyncHandler(async (req, res)=>{
  const queryParams = req.query;
  console.log(queryParams);
  
  const { bloodGroup, location } = queryParams; // Match exact keys
  console.log(bloodGroup, location);
  
  const users = await User.findAll({
    where: {
      blood_group: bloodGroup,
      location: location,
    },
  });
  if(users.length === 0){
    res.status(404).json({message : 'No users found'});
  }
  res.json(users);
  res.status(200).json(users);

  console.log(users);
})

module.exports = {registerUser, login,currentUser, getSearchedBlood};