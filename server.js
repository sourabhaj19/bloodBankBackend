const express = require('express');
const db = require('./config/db');
const userRoute = require('./routes/userRoute');
const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('./swagger-output.json');
const app = express();



// Middleware
app.use(express.json());
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

/**
 * @swagger
 * /:
 *   get:
 *     summary: Welcome message
 *     description: Returns a greeting message.
 *     responses:
 *       200:
 *         description: Success message
 */
app.get('/', (req, res) => {
  res.send('Hello World from Express Server!');
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
