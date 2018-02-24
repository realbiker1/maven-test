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


public final class FileIds {

    FileIds() {
        throw new NNZeroException("Static utility class is not intended to be instantiated.");
    }

    public static FileId parse(String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Path is empty.");
        }
        String[] parts = path.split("/");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Path '" + path + "' contains zero slash-separated parts.");
        }
        if (parts.length < 4) {
            throw new IllegalArgumentException("Path '" + path + "' has to contain at least 4 slash-separated parts.");
        }
        StringBuilder groupIdBuilder = new StringBuilder();
        for (int i = 0; i < parts.length - 3; i++) {
            if (i != 0) {
                groupIdBuilder.append('.');
            }
            groupIdBuilder.append(parts[i]);
        }
        return new FileId(groupIdBuilder.toString(),
                parts[parts.length - 3],
                parts[parts.length - 2],
                parts[parts.length - 1]);
    }

    public static FileId createPom(String groupId, String artifactId, String version) {
        String fileName = artifactId + "-" + version + ".pom";
        return new FileId(groupId, artifactId, version, fileName);
    }

    public static FileId createMainJar(String groupId, String artifactId, String version) {
        String fileName = artifactId + "-" + version + ".jar";
        return new FileId(groupId, artifactId, version, fileName);
    }

}
