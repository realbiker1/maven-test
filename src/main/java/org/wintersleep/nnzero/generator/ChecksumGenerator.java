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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.MessageDigest;

class ChecksumGenerator implements Generator {

    private final Generator delegate;
    private final String algorithm;

    ChecksumGenerator(Generator delegate, String algorithm) {
        this.delegate = delegate;
        this.algorithm = algorithm;
    }

    @Override
    public void generate(OutputStream out) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        delegate.generate(bos);
        byte[] data = bos.toByteArray();
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] sha1 = md.digest(data);
        try (Writer w = new OutputStreamWriter(out)) {
            w.write(bytesToHex(sha1));
            w.write('\n');
        }
    }


    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
