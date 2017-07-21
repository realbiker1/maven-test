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

import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneratorFactory {

    private final static Logger log = Logger.getLogger(GeneratorFactory.class.getName());

    private static final String NNZERO_DOES_NOT_EXIST = "99.0-does-not-exist";

    public Generator create(FileId fileId) {
        if (NNZERO_DOES_NOT_EXIST.equals(fileId.getVersion())) {
            switch (fileId.getExtension()) {
                case "sha1":
                    return new ChecksumGenerator(create(fileId.withFileName(fileId.getBaseName())),
                            "SHA-1");
                case "md5":
                    return new ChecksumGenerator(create(fileId.withFileName(fileId.getBaseName())),
                            "MD5");
                case "jar":
                    if (fileId.isMainJarFile()) {
                        return new MainJarGenerator(fileId);
                    }
                    return new EmptyJarGenerator();
                case "pom":
                    return new PomGenerator(fileId);
                default:
                    log.log(Level.WARNING, "Unsupported extension '" + fileId.getExtension() + "'");
            }
        } else {
            log.log(Level.WARNING, "Version '" + fileId.getVersion() + "' is not '" + NNZERO_DOES_NOT_EXIST + "'");
        }
        return null;
    }
}
