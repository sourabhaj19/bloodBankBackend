const dotenv  =  require('dotenv').config();
const express = require('express');
const db = require('./config/db');
const userRoute = require('./routes/userRoute.js');
const rolesRoute = require('./routes/rolesRoute.js');
const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('./swagger-output.json');
const app = express();
const cors = require('cors')
app.use(express.json());
app.use(cors());
// Middleware
app.use("/api", rolesRoute);
app.use("/api/user", userRoute);

// Swagger configuration
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
// Database connection and sync
db.sync()
  .then(() => console.log('Database & tables created!'))
  .catch(err => {
    console.error('Unable to create tables, shutting down...', err);
    process.exit(1);
  });

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
