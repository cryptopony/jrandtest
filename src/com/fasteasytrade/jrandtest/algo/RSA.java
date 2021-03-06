/*
 * Created on 16/03/2005
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
package com.fasteasytrade.jrandtest.algo;

import java.math.BigInteger;

import com.fasteasytrade.jrandtest.io.FileAlgoRandomStream;

/**
 * RSA algorithm as a random stream. Use RSACrypt class.
 * <p>
 * 
 * @author Zur Aougav
 */
public class RSA extends FileAlgoRandomStream {

    RSACrypt algo = null;

    int bitlen = 256;

    final int size = bitlen / 8;

    byte[] algoBuffer = null;

    byte[] outAlgoBuffer = null;

    int outAlgoBufferIx = -1;

    public RSA() {
        super();
    }

    public RSA(String keyFileName) {
        super(keyFileName);
    }

    
    @Override
    public void setupKeys() {
        publicKeyLength = size;
        privateKeyLength = size;
        super.setupKeys();
    }

    @Override
    public void setup() {
        algo = new RSACrypt();
        algo.generateKeys(bitlen);
        if (algoBuffer == null) {
            algoBuffer = new byte[algo.getMessageLength() - 5];
        }
        if (outAlgoBuffer == null) {
            outAlgoBuffer = new byte[algo.getMessageLength()];
        }
        outAlgoBufferIx = algo.getMessageLength();
    }

    @Override
    public boolean openInputStream() {
        if (filename != null) {
            super.openInputStream();
        }

        setup(); // make sure algorithm and keys/states are reset
        count = 0;
        countLastRead = size;
        actualSize = size;
        open = true;
        return open;
    }

    @Override
    public byte readByte() {
        if (!isOpen()) {
            return -1;
        }

        if (filename == null && count > maxCount) {
            open = false;
            return -1;
        }

        if (outAlgoBufferIx == outAlgoBuffer.length) {
            BigInteger z = algo.encrypt(algoBuffer).elementAt(0);
            outAlgoBuffer = z.toByteArray();
            outAlgoBufferIx = 0;
        }

        byte prng = outAlgoBuffer[outAlgoBufferIx++];

        if (filename == null) {
            count++;
            return prng;
        }

        /*
         * we have a real filename to encrypt
         */
        byte data = super.readByte();
        return (byte)(prng ^ data);
    }

    @Override
    public int readInt() {
        if (!isOpen()) {
            return -1;
        }

        if (filename == null && count > maxCount) {
            open = false;
            return -1;
        }

        int prng = 0xff & readByte();
        prng = (prng << 8) | (0xff & readByte());
        prng = (prng << 8) | (0xff & readByte());
        prng = (prng << 8) | (0xff & readByte());

        return prng;
    }

    @Override
    public long readLong() {
        if (!isOpen()) {
            return -1;
        }

        if (filename == null && count > maxCount) {
            open = false;
            return -1;
        }

        long prng = 0xff & readByte();
        for (int i = 0; i < 7; i++) {
            prng = (prng << 8) | (0xff & readByte());
        }

        return prng;
    }

    public static void main(String[] args) {
        if (args != null && args.length > 0 && args[0] != null) {
            RSA algo = new RSA();
            algo.setup();
            try {
                algo.openInputStream();
                for (int i = 0; i < 100; i++) {
                    System.out.print(algo.readByte());
                    System.out.print(",");
                }
                System.out.println();
            } catch (Exception e) {
                System.out.println("" + e);
            }
        }
    }
}
