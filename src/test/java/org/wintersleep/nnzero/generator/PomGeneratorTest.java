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

import org.junit.Test;
import org.wintersleep.nnzero.FileIds;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertTrue;

public class PomGeneratorTest {
    @Test
    public void generate() throws Exception {
        PomGenerator generator = new PomGenerator(FileIds.createPom("g", "a", "v"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.generate(out);
        String output = out.toString();
        assertTrue(output.contains("<project"));
        assertTrue(output.contains("<groupId>g</groupId>"));
        assertTrue(output.contains("<artifactId>a</artifactId>"));
        assertTrue(output.contains("<version>v</version>"));
        assertTrue(output.contains("announcement-version-99-does-not-exist"));
        assertTrue(output.contains("</project>"));
    }

}
