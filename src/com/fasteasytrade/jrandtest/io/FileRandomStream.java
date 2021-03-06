/*
 * Created on 04/02/2005
 *
 * Copyright (c) 2005, Zur Aougav, aougav@hotmail.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 
 * Neither the name of the Zur Aougav nor the names of its contributors may be 
 * used to endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.fasteasytrade.jrandtest.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class represents an encrypted or random source file. Data, bytes or
 * int32, are read from file.
 * 
 * @author Zur Aougav
 *  
 */
public class FileRandomStream implements RandomStream {

    public boolean open = false;

    //DataInputStream infile = null;
    BufferedInputStream infile = null;

    public String filename = null;

    public int count = 0; // count bytes, 8 bits, read from stream

    public int SIZE = 1024 * 64; // size of buffer. 64KB.

    public int countLastRead = SIZE;

    public int actualSize = SIZE; // actual number of bytes read from input file

    public byte[] buffer = new byte[SIZE];

    public FileRandomStream() {
    }

    public FileRandomStream(String s) {
        filename = s;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public void setFilename(String s) {
        filename = s;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean openInputStream() {
        open = false;
        if (filename == null) {
            return false;
        }
        /*
         * throw exception if error... we want to let the "caller" method
         * "knows" what's going on...
         */
        try {
            infile = new BufferedInputStream(new FileInputStream(filename), 1024 * 64);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        open = true;
        count = 0;
        actualSize = SIZE;
        countLastRead = SIZE;
        return true;
    }

    @Override
    public boolean closeInputStream() {
        try {
            if (infile != null) {
                infile.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        open = false;
        return true;
    }

    @Override
    public byte readByte() {

        if (!isOpen()) {
            return -1;
        }

        try {
            if (countLastRead == SIZE) {
                actualSize = infile.read(buffer);
                if (actualSize < 0) { // passed end of file ?
                    open = false;
                    return -1;
                }
                countLastRead = 0;
            }

            byte temp = buffer[countLastRead++];
            count++;
            return temp;
        } catch (Exception e) {
            open = false;
        }

        // passed end of file
        return -1;
    }

    @Override
    public int readInt() {
        if (!isOpen()) {
            return -1;
        }

        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) | (0xff & readByte());
            if (!isOpen()) {
                return -1;
            }
        }
        return result;
    }

    @Override
    public long readLong() {
        if (!isOpen()) {
            return -1;
        }

        int result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | (0xff & readByte());
            if (!isOpen()) {
                return -1;
            }
        }
        return result;
    }

    /**
     * Be sure to close input file at end of processing.
     *  
     */
    @Override
    public void finalize() {
        if (infile != null) {
            try {
                infile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read one int from registered input random stream and divide it by
     * (2^32 - 1).
     * 
     * @return double number netween 0 and 1.
     */
    @Override
    public double read32BitsAsDouble() {
        return ((double)(0x00ffffffffL & readInt())) / (double)0x0ffffffffL;
    }

}