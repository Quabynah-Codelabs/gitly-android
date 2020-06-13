import { Response, Request, NextFunction } from "express";
import config from "config";
import jwt from "jsonwebtoken";

// Middleware for requesting token for certain routes
export = (req: Request, res: Response, next: NextFunction) => {
  let token = req.header("x-auth-token");
  if (!token) return res.status(401).send("Access denied. No token provided");
  try {
    let decoded = jwt.verify(token, config.get("jwt_private_key"));
    req.user = decoded;
    next();
  } catch (error) {
    res.status(400).send("Invalid token");
  }
};
