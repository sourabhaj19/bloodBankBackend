const expressAsyncHandler = require('express-async-handler');
const Role = require('../models/rolesModel');

// get all roles
const getRoles = expressAsyncHandler(async (req, res) => {
  const roles = await Role.findAll();
  res.json(roles);
});

const postRole = expressAsyncHandler(async (req, res)=>{
  console.log(req.body);
  const { name } = req.body;
  if(!name){
    res.status(400).json({message: "Please fill all the required fields"});
  }
  const newRole = await Role.create({name})
  res.status(201).json(newRole);
})

const deleteROle = expressAsyncHandler(async (req, res)=>{
  console.log(req.params.name);
  const role = await Role.findByPk(req.params.name);
  if(!role){
    res.status(404).json({message : "Role not found"});
  }
  await role.destroy();
  res.status(200).json({message : "Role deleted successfully"});
})

module.exports = { getRoles, postRole, deleteROle };