const express = require("express");
const { assignRolesToUser } = require("../controllers/userController");
// ...existing code...

const router = express.Router();

// Route to assign multiple roles to a user
router.post("/assign-roles", assignRolesToUser);

// ...existing code...
module.exports = router;
