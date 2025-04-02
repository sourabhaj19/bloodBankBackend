const router = require('express').Router();
const { registerUser, login,getSearchedBlood } = require('../controllers/userController');

router.post('/login', login);
router.post('/register', registerUser);
router.get('/search-blood', getSearchedBlood);

module.exports = router;
