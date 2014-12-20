/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
package org.intelligentsia.restexpress.sample.benchmark;

import junit.framework.Test;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

public class BenchmarkResponseTimeUnderLoadTest {

    public static Test suite(int maxUsers, long maxElapsedTime ) {
        Test testCase = new BenchmarkTestCase("testEcho");
        Test timedTest = new TimedTest(testCase, maxElapsedTime);
        Test loadTest = new LoadTest(timedTest, maxUsers);

        return loadTest;
    }
    
    public static void run(int maxUsers, long maxElapsedTime ) {
    	for (int i = 0 ; i < 10; i++) {
    		 junit.textui.TestRunner.run(suite(maxUsers, maxElapsedTime));
    	}
    }
    
    public static void main(String[] args) {
        //run(10, 2000);
        //run(100, 2000);
    	run(1000, 2000);
    }
}
