const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv').config();
const db = require('./config/db');
const userRoutes = require('./routes/userRoutes');
const roleRoutes = require('./routes/roleRoutes');

const app = express();

// Middleware
app.use(express.json());
app.use(cors());

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));

// Routes
app.use('/api/users', userRoutes);
app.use('/api/roles', roleRoutes);

// Database connection
db.sync()
  .then(() => console.log('Database connected and tables synced!'))
  .catch(err => {
    console.error('Database connection failed:', err);
    process.exit(1);
  });

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
