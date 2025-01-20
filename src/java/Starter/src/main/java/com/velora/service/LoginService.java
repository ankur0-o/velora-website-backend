package com.velora.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.velora.connector.MongoConnector;
import com.velora.utility.HashUtility;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class LoginService {
  private MongoConnector mongoConnector;
  @Value("${spring.data.mongodb.database.register}")
  private String dataBaseName;
  @Value("${spring.data.mongodb.collection.register}")
  private String collectionName;
  @Value("${spring.data.mongodb.key}")
  private String key;
  private final HashUtility hashUtility;
  final Logger logger = Logger.getLogger(LoginService.class.getName());

  @Autowired
  public LoginService(HashUtility hashUtility) {
    this.hashUtility = hashUtility;
  }
  public String login(String userName, String password) {
    final MongoClient mongoClient = MongoClients.create(key);
    mongoConnector = new MongoConnector(mongoClient, hashUtility)
        .setMongoDatabase(dataBaseName)
        .setCollection(collectionName);
    try {
      final Document document = mongoConnector.getUser(userName);
      logger.info("User found: " + document.get("email").toString());
      if (hashUtility.validatePassword(password, document.get("password").toString())) {
        return "Login successful!";
      }
    } catch (Exception e) {
      throw new RuntimeException("Unexpected error.");
    }
    throw new RuntimeException("Invalid email or password.");
  }
}