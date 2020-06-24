import express, { Application, Request, Response } from "express";
import mongoose from "mongoose";
import morgan from "morgan";
import users from "./routes/users";
import auth from "./routes/auth";
import config from "config";
import error from "./middlewares/error";

// Check for the existence of the server url
if (!config.get("gitly_server_url") || !config.get("jwt_private_key")) {
  console.error("FATAL ERROR: Server url not defined for Gitly");
  process.exit(1);
}

// Get application instance
let app: Application = express();

// Middlewares
app.use(express.json());
app.use(morgan("dev"));

// Routes
app.get(
  "/api",
  error((req: Request, res: Response) => {
    return res.send("Accessing Gitly server");
  })
);
app.post(
  "/api",
  error((req: Request, res: Response) => {
    return res.send("Accessing Gitly server");
  })
);
app.use("/api/users", users);
app.use("/oauth", auth);

// Start connection to MongoDB
mongoose
  .connect(/*config.get("gitly_server_url")*/ "mongodb+srv://quabynah:quabynah1993@cluster0-fz8dm.gcp.mongodb.net/gitly?retryWrites=true&w=majority", {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => console.log("Connected to Mongo DB"))
  .catch((err: Error) => console.log("Could not connect to Mongo DB"));

// Setup port
let port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Started server on port -> ${port}`));


export = app;