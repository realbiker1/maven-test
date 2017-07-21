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

import java.io.*;

import static org.junit.Assert.*;

public class ChecksumGeneratorTest {

    @Test
    public void test() throws Exception {
        Generator generator = new ChecksumGenerator(new Generator() {
            @Override
            public void generate(OutputStream out) throws Exception {
                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
                w.write("Hello world!");
                w.flush();
            }
        }, "SHA-1");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.generate(out);
        // Every SHA-1 digest is 20 bytes, so 40 in hex representation, plus the newline)
        assertEquals(2 * 20 + 1, out.toByteArray().length);
    }

}
