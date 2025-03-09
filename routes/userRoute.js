const router = require('express').Router();
const { registerUser, login } = require('../controllers/userController');

router.post('/login', login);
router.post('/register', registerUser);

module.exports = router;
