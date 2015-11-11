/*
 * Copyright (c) 2015 SK PLANET. ALL Rights Reserved.
 *
 * Syrup Pay Jose Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skplanet.jose.commons.codec.digest;

import java.security.MessageDigest;

/**
 * Standard {@link MessageDigest} algorithm names from the <cite>Java Cryptography Architecture Standard Algorithm Name
 * Documentation</cite>.
 * <p>
 * This class is immutable and thread-safe.
 * </p>
 * TODO 2.0 This should be an enum.
 *
 * @see <a href="http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html">Java Cryptography
 *      Architecture Standard Algorithm Name Documentation</a>
 * @since 1.7
 * @version $Id: MessageDigestAlgorithms.java 1637936 2014-11-10 16:47:29Z ggregory $
 */
public class MessageDigestAlgorithms {

    private MessageDigestAlgorithms() {
        // cannot be instantiated.
    }

    /**
     * The MD2 message digest algorithm defined in RFC 1319.
     */
    public static final String MD2 = "MD2";

    /**
     * The MD5 message digest algorithm defined in RFC 1321.
     */
    public static final String MD5 = "MD5";

    /**
     * The SHA-1 hash algorithm defined in the FIPS PUB 180-2.
     */
    public static final String SHA_1 = "SHA-1";

    /**
     * The SHA-224 hash algorithm defined in the FIPS PUB 180-4. 
     * <p>
     * Java 8 only.
     * </p>
     * 
     * @since 1.11
     */
    public static final String SHA_224 = "SHA-224";

    /**
     * The SHA-256 hash algorithm defined in the FIPS PUB 180-2.
     */
    public static final String SHA_256 = "SHA-256";

    /**
     * The SHA-384 hash algorithm defined in the FIPS PUB 180-2.
     */
    public static final String SHA_384 = "SHA-384";

    /**
     * The SHA-512 hash algorithm defined in the FIPS PUB 180-2.
     */
    public static final String SHA_512 = "SHA-512";

}
