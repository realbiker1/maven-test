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

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {

    // Enable logging using:
    // -Djava.util.logging.config.file=config/logging.properties
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.err.println("Usage: " + Main.class + " [<bind_address> [bind_port]]");
            System.exit(-1);
        }
        createServer(createSocketAddress(args));
    }

    protected static HttpServer createServer(InetSocketAddress address) throws IOException {
        HttpServer server = HttpServer.create(address, 0);
        HttpContext context = server.createContext("/maven2/");
        context.setHandler(new NNZeroHttpHandler());
        server.start();
        return server;
    }

    private static InetSocketAddress createSocketAddress(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getByName("0.0.0.0");
        if (args.length >= 1) {
            address = InetAddress.getByName(args[0]);
        }
        int port = 9999;
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }
        return new InetSocketAddress(address, port);
    }

}
