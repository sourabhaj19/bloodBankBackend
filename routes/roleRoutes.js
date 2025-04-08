const router = require('express').Router();
const {
  createRole,
  getRoles,
  updateRole,
  deleteRole,
} = require('../controllers/roleController');
const { protect } = require('../middleware/authMiddleware');

router.post('/', protect, createRole);
router.get('/', protect, getRoles);
router.put('/:roleName', protect, updateRole);
router.delete('/:roleName', protect, deleteRole);

module.exports = router;
