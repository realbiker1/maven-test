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

import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

public class MainTest {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9998;

    private static HttpServer server;

    @BeforeClass
    public static void before() throws IOException {
        server = Main.createServer(new InetSocketAddress(HOST, PORT));
    }

    @AfterClass
    public static void after() {
        server.stop(0);
    }

    @Test
    public void testMainJar() throws IOException {
        URL url = new URL("http", HOST, PORT, "/maven2/org/wintersleep/wintersleep-nnzero/99.0-does-not-exist/wintersleep-nnzero-99.0-does-not-exist.jar");
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        assertJarFile(urlConnection.getInputStream(), true);
    }

    @Test
    public void testJavadocJar() throws IOException {
        assertEmptyJar("javadoc");
    }

    @Test
    public void testSourcesJar() throws IOException {
        assertEmptyJar("sources");
    }

    @Test
    public void testPomXml() throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL("http", HOST, PORT, "/maven2/org/wintersleep/wintersleep-nnzero/99.0-does-not-exist/wintersleep-nnzero-99.0-does-not-exist.pom");
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        try (InputStream is = urlConnection.getInputStream()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            assertTrue(builder.isNamespaceAware());
            Document doc = builder.parse(is);
            assertEquals("http://maven.apache.org/POM/4.0.0", doc.getDocumentElement().getNamespaceURI());
            assertEquals("project", doc.getDocumentElement().getTagName());
        }
    }

    @Test
    public void testMD5() throws IOException {
        assertChecksum("md5", 32);
    }

    @Test
    public void testSHA1() throws IOException {
        assertChecksum("sha1", 40);
    }

    @Test
    public void testWrongVersion() throws IOException {
        assertNotFound("1.0", "jar");
    }

    @Test
    public void testUnsupportedExtension() throws IOException {
        assertNotFound("99.0-does-not-exist", "zip");
    }

    private void assertNotFound(String version, String extension) throws IOException {
        URL url = new URL("http", HOST, PORT, "/maven2/org/wintersleep/wintersleep-nnzero/" + version + "/wintersleep-nnzero-" + version + "." + extension);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        int code = urlConnection.getResponseCode();
        assertEquals(404, code);
        assertEquals("Not Found", urlConnection.getResponseMessage());
    }


    private void assertEmptyJar(String type) throws IOException {
        URL url = new URL("http", HOST, PORT, "/maven2/org/wintersleep/wintersleep-nnzero/99.0-does-not-exist/wintersleep-nnzero-" + type + "-99.0-does-not-exist.jar");
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        assertJarFile(urlConnection.getInputStream(), false);
    }

    private void assertJarFile(InputStream inputStream, boolean allowPom) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry nextEntry = zis.getNextEntry();
            while (nextEntry != null) {
                assertJarEntry(nextEntry, allowPom);
                nextEntry = zis.getNextEntry();
            }
        }
    }

    private void assertJarEntry(ZipEntry entry, boolean allowPom) {
        switch (entry.getName()) {
            case "META-INF/MANIFEST.MF":
                return;
            case "META-INF/maven/org.wintersleep/wintersleep-nnzero/pom.properties":
            case "META-INF/maven/org.wintersleep/wintersleep-nnzero/pom.xml":
                if (allowPom) {
                    return;
                }
            default:
                fail("Unexpected entry in main jar file: " + entry.getName());
        }
    }

    private void assertChecksum(String algo, int expectedLength) throws IOException {
        URL url = new URL("http", HOST, PORT, "/maven2/org/wintersleep/wintersleep-nnzero/99.0-does-not-exist/wintersleep-nnzero-99.0-does-not-exist.jar." + algo);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String checksum = r.readLine();
            assertEquals(expectedLength, checksum.length());
        }
    }

}
