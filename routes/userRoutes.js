const router = require('express').Router();
const {
  registerUser,
  loginUser,
  getCurrentUser,
  getAllUsers,
  updateUser,
  deleteUser,
  changePassword,
  resetPassword,
} = require('../controllers/userController');
const { protect } = require('../middleware/authMiddleware');

router.post('/register', registerUser);
router.post('/login', loginUser);
router.get('/current', protect, getCurrentUser);
router.get('/', protect, getAllUsers);
router.put('/:id', protect, updateUser);
router.delete('/:id', protect, deleteUser);
router.put('/change-password', protect, changePassword);
router.post('/reset-password', resetPassword);

module.exports = router;
