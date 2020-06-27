import express, { Application, Request, Response } from "express";

// get application instance
let app: Application = express();

app.get("/", (req: Request, res: Response) => {
  return res.send({ message: "Server started" });
});

// start server
let port = process.env.PORT || 5000;
app.listen(port, () => console.log("Server started"));

export = app;
