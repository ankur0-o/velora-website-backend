package com.velora.controller;

import com.velora.data.login.LoginRequest;
import com.velora.data.login.LoginResponse;
import com.velora.service.LoginService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
  final Logger logger = org.apache.logging.log4j.LogManager.getLogger(LoginController.class);
  private final LoginService loginService;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @RequestMapping(method = RequestMethod.POST,
        value = "/login",
      consumes = "application/json",
      produces = "application/json")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {

    logger.info(loginRequest);
    String message = loginService.login(loginRequest.getUserName(), loginRequest.getPassword());
   if(message.equals("Login successful!")) {
     return new LoginResponse("Login successful!");
   }
    throw new RuntimeException("Invalid email or password.");
  }
}