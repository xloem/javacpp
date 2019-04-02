/*
 * Copyright (C) 2018-2019 Samuel Audet
 *
 * Licensed either under the Apache License, Version 2.0, or (at your option)
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation (subject to the "Classpath" exception),
 * either version 2, or any later version (collectively, the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     http://www.gnu.org/licenses/
 *     http://www.gnu.org/software/classpath/license.html
 *
 * or as provided in the LICENSE.txt file that accompanied this code.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bytedeco.javacpp.indexer;

import org.bytedeco.javacpp.BooleanPointer;
import org.bytedeco.javacpp.Pointer;

/**
 * An indexer for a {@link BooleanPointer} using the {@link Raw} instance.
 *
 * @author Samuel Audet
 */
public class BooleanRawIndexer extends BooleanIndexer {
    /** The instance for the raw memory interface. */
    protected static final Raw RAW = Raw.getInstance();
    /** The backing pointer. */
    protected BooleanPointer pointer;
    /** Base address and number of elements accessible. */
    final long base, size;

    /** Calls {@code BooleanRawIndexer(pointer, { pointer.limit() - pointer.position() }, { 1 })}. */
    public BooleanRawIndexer(BooleanPointer pointer) {
        this(pointer, new long[] { pointer.limit() - pointer.position() }, ONE_STRIDE);
    }

    /** Calls {@code BooleanRawIndexer(pointer, sizes, strides(sizes))}. */
    public BooleanRawIndexer(BooleanPointer pointer, long[] sizes) {
        this(pointer, sizes, strides(sizes));
    }

    /** Constructor to set the {@link #pointer}, {@link #sizes} and {@link #strides}. */
    public BooleanRawIndexer(BooleanPointer pointer, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.pointer = pointer;
        base = pointer.address() + pointer.position() * VALUE_BYTES;
        size = pointer.limit() - pointer.position();
    }

    @Override public Pointer pointer() {
        return pointer;
    }

    @Override public boolean get(long i) {
        return RAW.getBoolean(base + checkIndex(i, size) * VALUE_BYTES);
    }
    @Override public BooleanIndexer get(long i, boolean[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(i * strides[0] + n);
        }
        return this;
    }
    @Override public boolean get(long i, long j) {
        return get(i * strides[0] + j);
    }
    @Override public BooleanIndexer get(long i, long j, boolean[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(i * strides[0] + j * strides[1] + n);
        }
        return this;
    }
    @Override public boolean get(long i, long j, long k) {
        return get(i * strides[0] + j * strides[1] + k);
    }
    @Override public boolean get(long... indices) {
        return get(index(indices));
    }
    @Override public BooleanIndexer get(long[] indices, boolean[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = get(index(indices) + n);
        }
        return this;
    }

    @Override public BooleanIndexer put(long i, boolean b) {
        RAW.putBoolean(base + checkIndex(i, size) * VALUE_BYTES, b);
        return this;
    }
    @Override public BooleanIndexer put(long i, boolean[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(i * strides[0] + n, b[offset + n]);
        }
        return this;
    }
    @Override public BooleanIndexer put(long i, long j, boolean b) {
        put(i * strides[0] + j, b);
        return this;
    }
    @Override public BooleanIndexer put(long i, long j, boolean[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(i * strides[0] + j * strides[1] + n, b[offset + n]);
        }
        return this;
    }
    @Override public BooleanIndexer put(long i, long j, long k, boolean b) {
        put(i * strides[0] + j * strides[1] + k, b);
        return this;
    }
    @Override public BooleanIndexer put(long[] indices, boolean b) {
        put(index(indices), b);
        return this;
    }
    @Override public BooleanIndexer put(long[] indices, boolean[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            put(index(indices) + n, b[offset + n]);
        }
        return this;
    }

    @Override public void release() { pointer = null; }
}
