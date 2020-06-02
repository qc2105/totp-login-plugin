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

import org.apache.commons.codec.binary.Base64;

public class Utils {
    private  static Base64 base64Codec = new Base64();

    // Class not meant to be instantiated
    private Utils() {
    }

    /**
     * Given the raw data of an image and the mime type, returns
     * a data URI string representing the image for embedding in
     * HTML/CSS.
     *
     * @param data The raw bytes of the image.
     * @param mimeType The mime type of the image.
     * @return The data URI string representing the image.
     */
    public static String getDataUriForImage(byte[] data, String mimeType) {
        String encodedData = new String(base64Codec.encode(data));

        return String.format("data:%s;base64,%s", mimeType, encodedData);
    }
}
