package com.velora.data.login;

public class LoginResponse {
private String message;

  public LoginResponse(String message) {
    this.message = message;
  }

  // Getters and setters
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
