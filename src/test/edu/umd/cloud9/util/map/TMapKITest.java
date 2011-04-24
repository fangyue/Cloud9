/*
 * Cloud9: A MapReduce Library for Hadoop
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package edu.umd.cloud9.util.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import edu.umd.cloud9.util.array.ArrayListOfInts;

public class TMapKITest {

  @Test
  public void testBasic1() {

    int size = 10000;
    Random r = new Random();
    ArrayListOfInts keys = new ArrayListOfInts();
    ArrayListOfInts values = new ArrayListOfInts();

    TMapKI<Integer> map = new TMapKI<Integer>();
    for (int i = 0; i < size; i++) {
      int k = r.nextInt(size*10);
      int v = r.nextInt(size*10);

      if (keys.contains(k)) {
        continue;
      }

      map.put(k, v);
      keys.add(k);
      values.add(v);
    }

    for (int i = 0; i < keys.size(); i++) {
      int k = keys.get(i);
      int v = map.get(k);

      assertEquals(values.get(i), v);
      assertTrue(map.containsKey(k));
    }

    int prev = -1;
    for ( MapKI.Entry<Integer> entry : map.entrySet() ) {
      if ( prev != -1) {
        assertTrue(entry.getKey() > prev); 
      }
      prev = entry.getKey();
    }

    prev = -1;
    for ( Integer key : map.descendingKeySet() ) {
      if ( prev != -1) {
        assertTrue(key < prev); 
      }
      prev = key;
    }
  }

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(TMapKITest.class);
  }
}