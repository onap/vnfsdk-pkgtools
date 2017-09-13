package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Option Int class
 *
 * This file implements the Evel Option class to handle optional ont fields.
 *
 * License
 * -------
 *
  * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;
/*
 * Handles Optional Integer fields
 */
public class EvelOptionInt extends EvelOption {

	/**************************************************************************//**
	 * Optional parameter holder for string.
	 *****************************************************************************/
     int value;
     
     private static final Logger LOGGER = Logger.getLogger( EvelOptionInt.class.getName() );
     
	 public EvelOptionInt()
	 {
		 super(false);
		 value = 0;
	 }
	 
	 public EvelOptionInt(boolean val, int str)
	 {
		 super(val);
		 value = str;
	 }
	 
	 public void InitValue()
	 {
		 is_set = false;
		 value = 0;
	 }
	 //Sets int value
	 public void SetValue(int str)
	 {
		 is_set = true;
		 value = str;
	 }
	 //Sets Integer value outputting debugging message
	 public void SetValuePr(int str, String mstr)
	 {
		 
		 is_set = true;
		 value = str;	
		 LOGGER.debug("Setting "+mstr+" to "+str);
	 }
	 
	 public int GetValue()
	 {
		 return value;
	 }
	 /*
	  * Encoding JSON function
	  * @retval boolean returns option true if object is encoded
	  * with Integer value
	  */	 
	 public boolean encJsonValue(JsonObjectBuilder obj, String name)
	 {
		 //If option is set encodes int value into JSON object
		 // with name tag
		 if( is_set ) obj.add(name, value);
		 return is_set;
	 }

}

