/*
 * Created on 06/03/2005
 *
 * JRandTest package
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
 * Neither the name of the JRandTest nor the names of its contributors may be 
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

import java.io.FileInputStream;

/**
 * @author Zur Aougav
 *  
 */
public abstract class FileAlgoRandomStream extends FileRandomStream implements AlgoRandomStream {

    public int publicKeyLength = 256; // length in bytes, 8 bits each

    public byte[] publicKey = null;

    public int privateKeyLength = 256; // length in bytes, 8 bits each

    public byte[] privateKey = null;

    public int maxCount = 0xfffffff; // 2 ^ 28 - "length" of algorithm stream

    public FileAlgoRandomStream() {
        super();
        setupKeys();
    }

    public FileAlgoRandomStream(String s) {
        super(s);
        //filename = s;
        setupKeys();
    }

    @Override
    public void setupKeys() {
        publicKey = new byte[publicKeyLength];
        privateKey = new byte[privateKeyLength];
    }

    @Override
    public void setPublicKeyFromFile(String f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(publicKey);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void setPublicKey(byte[] k) {
        if (k == null) {
            return;
        }
        for (int i = 0; i < publicKeyLength; i++) {
            publicKey[i] = k[i % k.length];
        }
    }

    @Override
    public void setPrivateKeyFromFile(String f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            fis.read(privateKey);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void setPrivateKey(byte[] k) {
        if (k == null) {
            return;
        }
        for (int i = 0; i < privateKeyLength; i++) {
            privateKey[i] = k[i % k.length];
        }
    }

    /**
     * @param s
     *            set algorithm input filename file
     */
    @Override
    public void setFilename(String s) {
        filename = s;
    }

    /**
     * @return algorithm input filename file
     */
    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public byte readByte() {
        return super.readByte();
    }

    @Override
    public int readInt() {
        return super.readInt();
    }

    @Override
    public long readLong() {
        return super.readLong();
    }
}