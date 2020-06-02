package com.qc.jenkinsPlugins;

/*
MIT License

Copyright (c) 2019 Sam Stevens

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

@SuppressWarnings("WeakerAccess")
public class DefaultSecretGenerator implements SecretGenerator {

    private final SecureRandom randomBytes = new SecureRandom();
    private final static Base32 encoder = new Base32();
    private final int numCharacters;

    public DefaultSecretGenerator() {
        this.numCharacters = 32;
    }

    /**
     * @param numCharacters The number of characters the secret should consist of.
     */
    public DefaultSecretGenerator(int numCharacters) {
        this.numCharacters = numCharacters;
    }

    @Override
    public String generate() {
        return new String(encoder.encode(getRandomBytes()));
    }

    private byte[] getRandomBytes() {
        // 5 bits per char in base32
        byte[] bytes = new byte[(numCharacters * 5) / 8];
        randomBytes.nextBytes(bytes);

        return bytes;
    }
}