/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tajo.storage.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.tajo.storage.Tuple;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes tuples into a mongodb collection
 */
public class MongoDBCollectionWriter {
  MongoClient mongoClient;
  MongoCollection<Document> collection;
  MongoDBDocumentSerializer mongoDBDocumentSerializer;
  private ConnectionInfo connectionInfo;
  private List<Document> documentList = new ArrayList<>();

  public MongoDBCollectionWriter(ConnectionInfo connectionInfo, MongoDBDocumentSerializer serializer) {
    this.connectionInfo = connectionInfo;
    this.mongoDBDocumentSerializer = serializer;
  }

  public void init() {
    mongoClient = new MongoClient(connectionInfo.getMongoDBURI());
    MongoDatabase db = mongoClient.getDatabase(connectionInfo.getDbName());
    collection = db.getCollection(connectionInfo.getTableName());
  }

  public void addTuple(Tuple tuple) throws IOException {
    Document dc = new Document();
    mongoDBDocumentSerializer.serialize(tuple, dc);
    documentList.add(dc);
  }

  public void write() {
    collection.insertMany(documentList);
  }

  public void close() {
    mongoClient.close();
  }

}