package com.velora.connector;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.velora.data.User;
import com.velora.utility.HashUtility;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MongoConnector {
  private final MongoClient mongoClient;
  private final HashUtility hashUtility;
  private MongoDatabase database;
  private MongoCollection<Document> collection;
  private final Logger logger = Logger.getLogger(MongoConnector.class.getName());

  @Autowired
  public MongoConnector(final MongoClient mongoClient, final HashUtility hashUtility) {
    this.mongoClient = mongoClient;
    this.hashUtility = hashUtility;
  }

  public MongoConnector setMongoDatabase(String databaseName) {
    database = mongoClient.getDatabase(databaseName);
    logger.info("Database set successfully: " + databaseName);
    return this;
  }

  public MongoConnector setCollection(String collectionName) {
    collection = database.getCollection(collectionName);
    logger.info("Collection set successfully: " + collectionName);
    return this;
  }

  // Method to insert a User object into the collection
  public void insertUser(User user) {
    final Document document = new Document()
        .append("_id", user.getUsername())
        .append("email", user.getEmail())
        .append("password", generatePasswordHash(user.getPassword()));
    try {
      collection.insertOne(document);
    } catch (Exception e) {
      close();
      throw new RuntimeException("Error inserting user: " + e.getMessage());
    }
    close();
    logger.info("User inserted successfully: " + user);
  }

  public Document getUser(String username) {
    final Document document;
    try {
      document = collection.find(new Document("_id", username)).first();
    } catch (Exception e) {
      close();
      throw new RuntimeException("Error getting user: " + e.getMessage());
    }
    close();
    if (document == null) {
      throw new RuntimeException("User not found.");
    }
    return document;
  }

  // Method to close the MongoDB connection
  private void close() {
    mongoClient.close();
    logger.info("MongoDB connection closed.");
  }

  // Method to generate a password hash
  private String generatePasswordHash(String password) {
    try {
      return hashUtility.hashPassword(password);
    } catch (Exception e) {
      throw new RuntimeException("Error generating password hash: " + e.getMessage());
    }
  }
}
