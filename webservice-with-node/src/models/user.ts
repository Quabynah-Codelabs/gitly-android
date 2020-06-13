import Joi from "joi";
import mongoose from "mongoose";
import jwt from "jsonwebtoken";
import config from "config";

// Create schema
let userSchema: mongoose.Schema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
    minlength: 5,
    maxlength: 50,
  },
  email: {
    type: String,
    required: true,
    minlength: 5,
    maxlength: 255,
    unique: true,
  },
  password: {
    type: String,
    required: true,
    minlength: 5,
    maxlength: 1024,
  },
  avatar: {
    type: String,
    default: "",
  },
  isAdmin: {
    type: Boolean,
    default: false,
  },
  timestamp: {
    type: Date,
    default: Date.now(),
  },
});

// generate json web token
userSchema.methods.genAuthToken = function () {
  let token = jwt.sign({ _id: this._id }, config.get("jwt_private_key"));
  return token;
};

// Define model
let User: mongoose.Model<mongoose.Document, {}> = mongoose.model(
  "User",
  userSchema,
  "users"
);

// Validation
let validate = (user: any) => {
  let schema = {
    name: Joi.string().min(5).max(50).required(),
    email: Joi.string().min(5).max(255).required().email(),
    password: Joi.string().min(5).max(255).required(),
  };

  return Joi.validate(user, schema);
};

// Export
export = {
  // typeof UserModel,
  User,
  userSchema,
  validate,
};
