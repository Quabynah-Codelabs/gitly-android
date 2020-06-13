import { Router, Request, Response } from "express";
import bycrypt from "bcrypt";
import Joi from "joi";
import _ from "lodash";
import { User } from "../models/user";

// Setup router
let router = Router();

// Validation
let validate = (user: any) => {
  let schema = {
    email: Joi.string().min(5).max(255).required().email(),
    password: Joi.string().min(5).max(255).required(),
  };

  return Joi.validate(user, schema);
};

// Login user route
router.post("/", async (req: Request, res: Response) => {
  // validate request
  let { error } = validate(req.body);
  if (error) return res.status(400).send(error.details[0].message);

  // check for user data existence
  let user = await User.findOne({ email: req.body.email });
  if (!user) return res.status(400).send("Invalid email or password");

  //  validate password
  let validPassword = await bycrypt.compare(req.body.password, user.password);
  if (!validPassword) return res.status(400).send("Invalid email or password");

  // send user data
  let token = user.genAuthToken();
  return res
    .header("x-auth-token", token)
    .send({
      access_token: token,
      token_type: user.isAdmin ? "admin" : "basic",
    });
});

// Export
export = router;
