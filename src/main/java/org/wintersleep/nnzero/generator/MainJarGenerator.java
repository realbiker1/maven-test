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

import org.wintersleep.nnzero.FileId;

import java.io.IOException;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

class MainJarGenerator extends AbstractJarGenerator {

    private final FileId fileId;

    MainJarGenerator(FileId fileId) {
        this.fileId = fileId;
    }

    @Override
    protected void addContent(JarOutputStream jout) throws Exception {
        String mavenDir = "META-INF/maven/" + fileId.getGroupId() + "/" + fileId.getArtifactId() + "/";
        addPomProperties(jout, mavenDir);
        addPomXml(jout, mavenDir);
    }

    private void addPomProperties(JarOutputStream jout, String mavenDir) throws IOException {
        JarEntry je = new JarEntry(mavenDir + "pom.properties");
        jout.putNextEntry(je);
        Properties properties = new Properties();
        properties.put("groupId", fileId.getGroupId());
        properties.put("artifactId", fileId.getArtifactId());
        properties.put("version", fileId.getVersion());
        properties.store(jout, "Created by wintersleep-nnzero");
    }

    private void addPomXml(JarOutputStream jout, String mavenDir) throws Exception {
        JarEntry je = new JarEntry(mavenDir + "pom.xml");
        jout.putNextEntry(je);
        new PomGenerator(fileId).generate(jout);
    }

}
