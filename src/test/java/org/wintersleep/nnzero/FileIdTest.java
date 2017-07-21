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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FileIdTest {

    @Test
    public void testBaseName() {
        assertEquals("", FileId.determineBaseName(""));
        assertEquals("name", FileId.determineBaseName("name"));
        assertEquals("name", FileId.determineBaseName("name."));
        assertEquals("name", FileId.determineBaseName("name.ext"));
        assertEquals("name.name", FileId.determineBaseName("name.name.ext"));
    }

    @Test
    public void testExtension() {
        assertEquals("", FileId.determineExtension(""));
        assertEquals("", FileId.determineExtension("name"));
        assertEquals("", FileId.determineExtension("name."));
        assertEquals("ext", FileId.determineExtension("name.ext"));
        assertEquals("ext", FileId.determineExtension("name.name.ext"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPath() {
        FileIds.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroParts() {
        FileIds.parse("/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonEnoughParts() {
        FileIds.parse("a/b/c");
    }

    @Test
    public void testMainJar() {
        FileId fi = FileIds.parse("g/r/o/u/p/artifact/version/artifact-version.jar");
        assertGAV(fi);
        assertEquals("artifact-version.jar", fi.getFileName());
        assertTrue(fi.isMainJarFile());
    }

    @Test
    public void testPom() {
        FileId fi = FileIds.parse("g/r/o/u/p/artifact/version/artifact-version.pom");
        assertGAV(fi);
        assertEquals("artifact-version.pom", fi.getFileName());
        assertEquals("artifact-version", fi.getBaseName());
        assertEquals("pom", fi.getExtension());
        assertTrue(fi.isPomFile());
    }

    private void assertGAV(FileId fi) {
        assertEquals("g.r.o.u.p", fi.getGroupId());
        assertEquals("artifact", fi.getArtifactId());
        assertEquals("version", fi.getVersion());
    }

    @Test
    public void testWithFileName() {
        FileId origFileId = FileIds.parse("g/r/o/u/p/artifact/version/artifact-version.pom");
        FileId newFileId = origFileId.withFileName("newname");
        assertGAV(newFileId);
        assertEquals("newname", newFileId.getFileName());
    }

}
