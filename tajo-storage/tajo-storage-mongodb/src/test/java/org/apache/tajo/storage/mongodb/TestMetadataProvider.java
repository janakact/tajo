/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tajo.storage.mongodb;

import com.google.common.collect.Sets;
import org.apache.tajo.catalog.MetadataProvider;
import org.apache.tajo.catalog.TableDesc;
import org.apache.tajo.storage.Tablespace;
import org.apache.tajo.storage.TablespaceManager;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMetadataProvider {
  static MongoDBTestServer server = MongoDBTestServer.getInstance();


  @AfterClass
  public static void tearDownClass() throws Exception {
    // server.stop();
  }

  @Test
  public void testGetTablespaceName() throws Exception {
    //check for Space Name validity
    Tablespace tablespace = TablespaceManager.get(server.getURI());
    MetadataProvider provider = tablespace.getMetadataProvider();
    assertEquals(server.SPACE_NAME, provider.getTablespaceName());
  }

  @Test
  public void testGetDatabaseName() throws Exception {
    Tablespace tablespace = TablespaceManager.get(server.getURI());
    MetadataProvider provider = tablespace.getMetadataProvider();
    assertEquals(MongoDBTestServer.MAPPEDDBNAME, provider.getDatabaseName());
  }


  @Test
  public void testGetSchemas() throws Exception {
    Tablespace tablespace = TablespaceManager.get(server.getURI());
    MetadataProvider provider = tablespace.getMetadataProvider();
    assertTrue(provider.getSchemas().isEmpty());
  }

  @Test
  public void testGetTables() throws Exception {
    Tablespace tablespace = TablespaceManager.get(server.getURI());
    MetadataProvider provider = tablespace.getMetadataProvider();

    final Set<String> expected = Sets.newHashSet(server.collectionNames);
    expected.add("system.indexes");
    final Set<String> found = Sets.newHashSet(provider.getTables(null, null));

    assertEquals(expected, found);
  }

  @Test
  public void testGetTableDescription() throws Exception {
    Tablespace tablespace = TablespaceManager.get(server.getURI());
    MetadataProvider provider = tablespace.getMetadataProvider();

    for (String tableName : server.collectionNames) {
      TableDesc table = provider.getTableDesc(null, tableName);
      assertEquals(server.MAPPEDDBNAME + "." + tableName, table.getName());
      assertEquals(server.getURI() + "?table=" + tableName, table.getUri().toASCIIString());
    }
  }


}
