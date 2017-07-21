package org.wintersleep.nnzero.generator;

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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wintersleep.nnzero.FileId;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;

class PomGenerator implements Generator {

    private final FileId fileId;

    PomGenerator(FileId fileId) {
        this.fileId = fileId;
    }

    @Override
    public void generate(OutputStream out) throws Exception {
        Document doc = createDoc();
        prettyPrint(doc, out);
    }

    private Document createDoc() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation domImpl = builder.getDOMImplementation();
        Document doc = domImpl.createDocument("http://maven.apache.org/POM/4.0.0", "project", null);
        Element de = doc.getDocumentElement();
        addTextElement(de, "modelVersion", "4.0.0");
        addTextElement(de, "groupId", fileId.getGroupId());
        addTextElement(de, "artifactId", fileId.getArtifactId());
        addTextElement(de, "version", fileId.getVersion());
        addTextElement(de, "description",
                "Deliberately empty jar file. See: http://day-to-day-stuff.blogspot.com/2007/10/announcement-version-99-does-not-exist.html");
        return doc;
    }

    private static void addTextElement(Element parent, String name, String value) {
        Element child = parent.getOwnerDocument().createElement(name);
        child.setTextContent(value);
        parent.appendChild(child);
    }

    private static void prettyPrint(Document xml, OutputStream out) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        tf.transform(new DOMSource(xml), new StreamResult(out));
    }
}
