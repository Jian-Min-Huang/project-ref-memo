import express from "express";

// Create Express server
const app = express();

// Express configuration
app.set("port", process.env.PORT || 3000);

export default app;