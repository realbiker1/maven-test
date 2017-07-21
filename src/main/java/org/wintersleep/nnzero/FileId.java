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

public class FileId {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String fileName;

    FileId(String groupId, String artifactId, String version, String fileName) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.fileName = fileName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public String getBaseName() {
        return determineBaseName(fileName);
    }

    public String getExtension() {
        return determineExtension(fileName);
    }

    static String determineBaseName(String fn) {
        int index = fn.lastIndexOf('.');
        if (index < 0) {
            return fn;
        } else {
            return fn.substring(0, index);
        }
    }

    static String determineExtension(String fn) {
        int index = fn.lastIndexOf('.');
        if (index >= 0) {
            return fn.substring(index+1);
        }
        return "";
    }

    public boolean isPomFile() {
        String expectedFileName = FileIds.createPom(groupId, artifactId, version).getFileName();
        return expectedFileName.equals(fileName);
    }

    public boolean isMainJarFile() {
        String expectedFileName = FileIds.createMainJar(groupId, artifactId, version).getFileName();
        return expectedFileName.equals(fileName);
    }

    public FileId withFileName(String fileName) {
        return new FileId(groupId, artifactId, version, fileName);
    }

}
