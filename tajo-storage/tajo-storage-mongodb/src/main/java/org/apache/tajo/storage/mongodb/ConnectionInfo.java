/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tajo.storage.mongodb;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tajo.exception.TajoInternalError;

public class ConnectionInfo {

    private static final Log LOG = LogFactory.getLog(MongoDBTableSpace.class);

    private MongoClientURI mongoDBURI;
    private String scheme;
    private String host;
    private String dbName;
    private String tableName;
    private String user;
    private String password;
    private int port;
    private Map<String, String> params;

    public static ConnectionInfo fromURI(String originalUri) {
        return fromURI(URI.create(originalUri));
    }

    public static ConnectionInfo fromURI(URI originalUri) {
        final String uriStr = originalUri.toASCIIString();
        URI uri = originalUri;

        final ConnectionInfo connInfo = new ConnectionInfo();
        connInfo.scheme = uriStr.substring(0, uriStr.indexOf("://"));

        connInfo.host = uri.getHost();
        connInfo.port = uri.getPort();

        //Set the db name
        String path = uri.getPath();
        if (path != null && !path.isEmpty()) {
            String [] pathElements = path.substring(1).split("/");
            if (pathElements.length != 1) {
                throw new TajoInternalError("Invalid JDBC path: " + path);
            }
            connInfo.dbName = pathElements[0];
        }

        //Convert parms into a Map
        Map<String, String> params = new HashMap<>();
        int paramIndex = uriStr.indexOf("?");
        if (paramIndex > 0) {
            String parameterPart = uriStr.substring(paramIndex+1, uriStr.length());

            String [] eachParam = parameterPart.split("&");

            for (String each: eachParam) {
                String [] keyValues = each.split("=");
                if (keyValues.length != 2) {
                    throw new TajoInternalError("Invalid URI Parameters: " + parameterPart);
                }
                params.put(keyValues[0], keyValues[1]);
            }
        }

        if (params.containsKey("table")) {
            connInfo.tableName = params.remove("table");
        }

        if (params.containsKey("user")) {
            connInfo.user = params.remove("user");
        }
        if (params.containsKey("password")) {
            connInfo.password = params.remove("password");
        }


        connInfo.params = params;

        String mongoDbURIStr = "";
        //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]

        //Generate the URI
        mongoDbURIStr+= connInfo.getScheme();
        mongoDbURIStr+="://";
        if(connInfo.getUser() !=null)
        {
            mongoDbURIStr+= connInfo.getUser();
            if(connInfo.getPassword() !=null)
                mongoDbURIStr+=":"+ connInfo.getPassword();
            mongoDbURIStr+="@";
        }
        mongoDbURIStr+= connInfo.getHost();
        mongoDbURIStr+=":";
        mongoDbURIStr+= connInfo.getPort();
        mongoDbURIStr+="/";
        mongoDbURIStr+= connInfo.getDbName();

        LOG.info(mongoDbURIStr);
        connInfo.mongoDBURI = new MongoClientURI(mongoDbURIStr);
        return connInfo;
    }

    public MongoClientURI getMongoDBURI()
    {
        return  mongoDBURI;
    }


    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getDbName() {
        return dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public Map<String, String> getParams() {
        return params;
    }
}

