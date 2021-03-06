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

package org.json;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class JSONObjectTest {

	@Test 
	public void test1() throws JSONException {
		 JSONObject obj1 = new JSONObject();
		 obj1.put("JSON", "Hello, World!");

		 String s = "{\"JSON\":\"Hello, World!\"}";
		 
		 assertEquals(obj1.toString(), s);
		 
		 JSONObject obj2 = new JSONObject(s);
		 
		 assertEquals(obj2.getString("JSON"), "Hello, World!");
	}

	@Test 
	public void test2() throws JSONException {
		 JSONObject obj = new JSONObject();

		 obj.put("firstName", "John");
		 obj.put("lastName", "Smith");
		 
		 JSONObject address = new JSONObject();
         address.put("streetAddress", "21 2nd Street");
         address.put("city", "New York");
       	 address.put("state", "NY");
         address.put("postalCode", 10021);
        
         JSONArray phoneNumbers = new JSONArray();
         phoneNumbers.put("212 555-1234");
         phoneNumbers.put("646 555-4567");
         
         obj.put("address", address);
         obj.put("phoneNumbers", phoneNumbers);
        						          
         String s = "{\"lastName\":\"Smith\",\"address\":{\"streetAddress\":\"21 2nd Street\",\"postalCode\":10021,\"state\":\"NY\",\"city\":\"New York\"},\"phoneNumbers\":[\"212 555-1234\",\"646 555-4567\"],\"firstName\":\"John\"}";

		 assertEquals(obj.toString(), s);
		 
		 JSONObject obj2 = new JSONObject(s);

		 assertEquals(obj2.getString("firstName"), "John");
		 assertEquals(obj2.getString("lastName"), "Smith");
		 assertEquals(obj2.getJSONObject("address").getString("city"), "New York");
		 assertEquals(obj2.getJSONArray("phoneNumbers").length(), 2);
		 assertEquals(obj2.getJSONArray("phoneNumbers").getString(0), "212 555-1234");
	 
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(JSONObjectTest.class);
	}

}
