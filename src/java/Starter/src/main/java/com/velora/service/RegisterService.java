package com.velora.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.velora.connector.MongoConnector;
import com.velora.data.User;
import com.velora.utility.HashUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
  private MongoConnector mongoConnector;
  @Value("${spring.data.mongodb.database.register}")
  private String dataBaseName;
  @Value("${spring.data.mongodb.collection.register}")
    private String collectionName;
  @Value("${spring.data.mongodb.key}")
  private String key;

  public String register(final User user) {
    try {
      final MongoClient mongoClient = MongoClients.create(key);
      mongoConnector = new MongoConnector(mongoClient, new HashUtility())
          .setMongoDatabase(dataBaseName)
          .setCollection(collectionName);
      mongoConnector.insertUser(user);
    } catch (Exception e) {
      if(e.getMessage().contains("duplicate key error")) {
        throw new RuntimeException("User already exists!");
      }
      throw new RuntimeException("Error registering user: " + e.getMessage());
    }
    return "User registered successfully!";
  }

}
