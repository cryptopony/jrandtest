/*
 * Created on 31/01/2005
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

package com.fasteasytrade.jrandtest.tests;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.io.RandomStream;

/**
 * Base class for all test classes. <p> Contains common methods and handle
 * input/output list of listeners.
 * 
 * @author Zur Aougav
 */

public abstract class Base implements RandomnessTest {

    final Logger log2 = Logger.getLogger(Base.class.getName());

    RandomStream rs = null;

    /**
     * register RandomStream interface. <p> Supports only one random
     * stream.
     * 
     * @param rs register radom stream interface
     */
    public void registerInput(RandomStream rs) {
        this.rs = rs;
    }

    @Override
    public Result runTest(RandomStream rs) throws Exception {
        if (rs == null) {
            throw new IllegalArgumentException();
        }
        this.rs = rs;
        Map<String,String> details = new TreeMap<String,String>();
        ResultStatus status = test(details);
        return new Result(status, details);
    }

    /**
     * test method to be implemented by each test class.
     */
    protected abstract ResultStatus test(Map<String,String> details);

}