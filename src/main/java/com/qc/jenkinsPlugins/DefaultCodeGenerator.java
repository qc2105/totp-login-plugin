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

package com.qc.jenkinsPlugins;

import org.apache.commons.codec.binary.Base32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

public class DefaultCodeGenerator implements CodeGenerator {

    private final HashingAlgorithm algorithm;
    private final int digits;

    public DefaultCodeGenerator() {
        this(HashingAlgorithm.SHA1, 6);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm) {
        this(algorithm, 6);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm, int digits) {
        if (algorithm == null) {
            throw new InvalidParameterException("HashingAlgorithm must not be null.");
        }
        if (digits < 1) {
            throw new InvalidParameterException("Number of digits must be higher than 0.");
        }

        this.algorithm = algorithm;
        this.digits = digits;
    }

    @Override
    public String generate(String key, long counter) throws CodeGenerationException {
        try {
            byte[] hash = generateHash(key, counter);
            return getDigitsFromHash(hash);
        } catch (Exception e) {
            throw new CodeGenerationException("Failed to generate code. See nested exception.", e);
        }
    }

    /**
     * Generate a HMAC-SHA1 hash of the counter number.
     */
    private byte[] generateHash(String key, long counter) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] data = new byte[8];
        long value = counter;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        // Create a HMAC-SHA1 signing key from the shared key
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(key);
        SecretKeySpec signKey = new SecretKeySpec(decodedKey, algorithm.getHmacAlgorithm());
        Mac mac = Mac.getInstance(algorithm.getHmacAlgorithm());
        mac.init(signKey);

        // Create a hash of the counter value
        return mac.doFinal(data);
    }

    /**
     * Get the n-digit code for a given hash.
     */
    private String getDigitsFromHash(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xF;

        long truncatedHash = 0;

        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= Math.pow(10, digits);

        // Left pad with 0s for a n-digit code
        return String.format("%0" + digits + "d", truncatedHash);
    }
}
