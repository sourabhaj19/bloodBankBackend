const swaggerAutogen = require("swagger-autogen")();

const doc = {
  info: {
    title: "Blood Bank API",
    description: "API documentation for Blood Bank application",
    version: "1.0.0",
  },
  host: "localhost:3002",
  schemes: ["http"],
};

const outputFile = "./swagger-output.json";
const routes = ["./routes/userRoute.js", "./routes/rolesRoute.js"]; // Add more routes as needed

swaggerAutogen(outputFile, routes, doc).then(() => {
  console.log("✅ Swagger JSON generated successfully.");
});
