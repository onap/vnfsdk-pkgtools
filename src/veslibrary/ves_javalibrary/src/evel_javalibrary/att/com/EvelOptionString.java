package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Option String class
 *
 * This file implements the Evel Option class to handle optional String fields.
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
 * Handles Optional String fields
 */
public class EvelOptionString extends EvelOption {

	/**************************************************************************//**
	 * Optional parameter holder for string.
	 *****************************************************************************/
     String value;
     
     private static final Logger LOGGER = Logger.getLogger( EvelOptionString.class.getName() );
     
	 public EvelOptionString()
	 {
		 super(false);
		 value = null;
	 }
	 
	 public EvelOptionString(boolean val, String str)
	 {
		 super(val);
		 value = str;
	 }
	 
	 public void InitValue()
	 {
		 is_set = false;
		 value = null;
	 }
	 //Setter
	 public void SetValue(String str)
	 {
		 is_set = true;
		 value = str;
	 }
	 
	//Sets String value outputting debugging message
	 public void SetValuePr(String str, String mstr)
	 {
		 
		 is_set = true;
		 value = str;	
		 LOGGER.debug("Setting "+mstr+" to "+str);
	 }
	 //Getter
	 public String GetValue()
	 {
		 return value;
	 }
	 /*
	  * Encoding JSON function
	  * @retval boolean returns option true if object is encoded
	  * with String value
	  */
	 public boolean encJsonValue(JsonObjectBuilder obj, String name)
	 {
		 if( is_set ) obj.add(name, value);
		 return is_set;
	 }

}

	
