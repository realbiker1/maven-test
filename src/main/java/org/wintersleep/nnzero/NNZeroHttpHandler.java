package org.wintersleep.nnzero;

/*-
 * #%L
 * Wintersleep 99.0-does-not-exist Maven Repo Server
 * %%
 * Copyright (C) 2017 Davy Verstappen
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.wintersleep.nnzero.generator.Generator;
import org.wintersleep.nnzero.generator.GeneratorFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

class NNZeroHttpHandler implements HttpHandler {

    private final static Logger log = Logger.getLogger(NNZeroHttpHandler.class.getName());

    private final GeneratorFactory generatorFactory = new GeneratorFactory();

    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            log.fine("Retrieving " + he.getRequestURI().getPath());
            FileId fileId = determineFileId(he);
            handle(he, fileId);
        } catch (IllegalArgumentException e) {
            log.log(Level.WARNING, e.getMessage(), e);
            he.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage(), e);
            he.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            he.close();
        }
    }

    private void handle(HttpExchange he, FileId fileId) throws Exception {
        Generator generator = generatorFactory.create(fileId);
        if (generator == null) {
            he.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        } else {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                generator.generate(out);
                byte[] contents = out.toByteArray();
                he.sendResponseHeaders(HttpURLConnection.HTTP_OK, contents.length);
                he.getResponseBody().write(contents);
            }
        }
    }

    private FileId determineFileId(HttpExchange he) {
        String requestPath = he.getRequestURI().getPath();
        String contextPath = he.getHttpContext().getPath();
        return FileIds.parse(requestPath.substring(contextPath.length()));
    }
}
