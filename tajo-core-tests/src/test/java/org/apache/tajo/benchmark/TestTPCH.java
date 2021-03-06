/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tajo.benchmark;

import org.apache.tajo.IntegrationTest;
import org.apache.tajo.QueryTestCaseBase;
import org.apache.tajo.TajoConstants;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public class TestTPCH extends QueryTestCaseBase {

  public TestTPCH() {
    super(TajoConstants.DEFAULT_DATABASE_NAME);
  }

  @Test
  @Option(withExplain = true, withExplainGlobal = true)
  @SimpleTest
  public void testQ1OrderBy() throws Exception {
    runSimpleTests();
  }

  @Test
  @Option(withExplain = true, withExplainGlobal = true)
  @SimpleTest
  public void testQ2FourJoins() throws Exception {
    runSimpleTests();
  }

  @Test
  @Option(withExplain = true, withExplainGlobal = true)
  @SimpleTest
  public void testTPCH14Expr() throws Exception {
    runSimpleTests();
  }

  @Test
  @Option(withExplain = true, withExplainGlobal = true)
  @SimpleTest
  public void testTPCHQ5() throws Exception {
    runSimpleTests();
  }

  @Test
  @Option(withExplain = true, withExplainGlobal = true)
  @SimpleTest
  public void testFirstJoinInQ7() throws Exception {
    runSimpleTests();
  }
}