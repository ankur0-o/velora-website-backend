package com.velora.controller;

import com.velora.data.RegisterResponse;
import com.velora.data.User;
import com.velora.data.login.LoginRequest;
import com.velora.data.login.LoginResponse;
import com.velora.service.LoginService;
import com.velora.service.RegisterService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {
  final Logger logger = org.apache.logging.log4j.LogManager.getLogger(LoginController.class);
  private final RegisterService registerService;

  @Autowired
  public RegisterController(RegisterService registerService) {
    this.registerService = registerService;
  }

  @CrossOrigin(origins = "http://localhost:63342")
  @RequestMapping(method = RequestMethod.POST,
      value = "/register",
      consumes = "application/json",
      produces = "application/json")
  public RegisterResponse register(@RequestBody User user) {
    logger.info("Registering user: " + user.getEmail() + " with password: " + user.getPassword() + ".");
    final String message = registerService.register(user);
    if (message.equals("User registered successfully!")) {
      return new RegisterResponse("User registered successfully!");
    }
    throw new RuntimeException("Error registering user.");
  }
}
