import { Router, Request, Response } from "express";
import { User, validate } from "../models/user";
import bcrypt from "bcrypt";
import _ from "lodash";
import auth from "../middlewares/auth";
import error from "../middlewares/error";

let router = Router();

// create a new user
router.post(
  "/",
  error(async (req: Request, res: Response) => {
    let { error } = validate(req.body);
    if (error) return res.status(400).send(error.details[0].message);

    let user = await User.findOne({ email: req.body.email });
    if (user) return res.status(400).send("User already registered");

    // new user
    user = new User(_.pick(req.body, ["name", "email", "password"]));

    // hash password
    let salt: string = await bcrypt.genSalt(10);
    user.password = await bcrypt.hash(user.password, salt);

    // save
    await user.save();

    // send user data
    return res
      .status(200)
      .send(
        _.pick(user, ["_id", "name", "email", "timestamp", "isAdmin", "avatar"])
      );
  })
);

// get the current user data
router.get(
  "/me",
  auth,
  error(async (req: Request, res: Response) => {
    let user = await User.findById(req.user);
    if (!user) return res.status(400).send("Invalid user credentials");

    // send user data
    return res
      .status(200)
      .send(
        _.pick(user, ["_id", "name", "email", "timestamp", "isAdmin", "avatar"])
      );
  })
);

// update user
router.put(
  "/me",
  auth,
  error(async (req: Request, res: Response) => {
    let error = req.body == null;
    if (error) return res.status(400).send("User details required");

    // update user document
    let user = await User.findByIdAndUpdate(
      req.user,
      _.pick(req.body, ["name", "avatar", "isAdmin"])
    );

    // return updated user
    return res
      .status(200)
      .send(
        _.pick(user, ["_id", "name", "email", "timestamp", "isAdmin", "avatar"])
      );
  })
);

export = router;
