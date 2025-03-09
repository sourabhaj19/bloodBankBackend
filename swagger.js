const swaggerAutogen = require("swagger-autogen");

const swaggerOptions = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'Blood Bank API',
      version: '1.0.0',
      description: 'API documentation for Blood Bank application',
    },
    host : 'localhost:3002'
  },
  apis: [`${__dirname}/routes/*.js`], // Corrected path to ensure Swagger reads route files
};

const outputFile = './swagger-output.json '
const routes = ['./routes/userRoute.js']; // Corrected path to ensure Swagger reads route files

swaggerAutogen(outputFile, routes, swaggerOptions); // Corrected path to ensure Swagger reads route files