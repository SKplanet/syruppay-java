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

package com.skplanet.jose.commons.codec.language;

import com.skplanet.jose.commons.codec.EncoderException;
import com.skplanet.jose.commons.codec.StringEncoder;

/**
 * Encodes a string into a Caverphone value.
 *
 * This is an algorithm created by the Caversham Project at the University of Otago. It implements the Caverphone 2.0
 * algorithm:
 *
 * <p>This class is immutable and thread-safe.</p>
 *
 * @version $Id: Caverphone.java 1075947 2011-03-01 17:56:14Z ggregory $
 * @see <a href="http://en.wikipedia.org/wiki/Caverphone">Wikipedia - Caverphone</a>
 * @since 1.5
 */
public abstract class AbstractCaverphone implements StringEncoder {

    /**
     * Creates an instance of the Caverphone encoder
     */
    public AbstractCaverphone() {
        super();
    }

    /**
     * Encodes an Object using the caverphone algorithm. This method is provided in order to satisfy the requirements of
     * the Encoder interface, and will throw an EncoderException if the supplied object is not of type java.lang.String.
     *
     * @param source
     *            Object to encode
     * @return An object (or type java.lang.String) containing the caverphone code which corresponds to the String
     *         supplied.
     * @throws EncoderException
     *             if the parameter supplied is not of type java.lang.String
     */
    
    public Object encode(final Object source) throws EncoderException {
        if (!(source instanceof String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
        }
        return this.encode((String) source);
    }

    /**
     * Tests if the encodings of two strings are equal.
     *
     * This method might be promoted to a new AbstractStringEncoder superclass.
     *
     * @param str1
     *            First of two strings to compare
     * @param str2
     *            Second of two strings to compare
     * @return <code>true</code> if the encodings of these strings are identical, <code>false</code> otherwise.
     * @throws EncoderException
     *             thrown if there is an error condition during the encoding process.
     */
    public boolean isEncodeEqual(final String str1, final String str2) throws EncoderException {
        return this.encode(str1).equals(this.encode(str2));
    }

}
