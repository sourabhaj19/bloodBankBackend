const router = require('express').Router();
const { getRoles, postRole, deleteROle } = require('../controllers/rolesController');

router.get('/roles', getRoles);
router.delete('/roles/:name', deleteROle);
router.post('/roles', postRole);
// router.put('/roles/:id', updateRole);

module.exports = router;