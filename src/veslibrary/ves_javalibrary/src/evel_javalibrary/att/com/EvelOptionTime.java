package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Option Time class
 *
 * This file implements the Evel Option Time class to handle optional time fields.
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

import java.util.Date;

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.apache.log4j.Logger;
/*
 * Handles Optional Date/Time fields
 */
public class EvelOptionTime extends EvelOption {

	/**************************************************************************//**
	 * Optional parameter holder for string.
	 *****************************************************************************/
     Date value;
     
     private static final Logger LOGGER = Logger.getLogger( EvelOptionTime.class.getName() );
     
	 public EvelOptionTime()
	 {
		 super(false);
		 value = null;
	 }
     
	 public EvelOptionTime(boolean val, Date str)
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
	 public void SetValue(Date str)
	 {
		 is_set = true;
		 value = str;
	 }
	 
	//Sets Date value outputting debugging message
	 public void SetValuePr(Date str, String mstr)
	 {
		 
		 is_set = true;
		 value = str;	
		 LOGGER.debug("Setting "+mstr+" to "+str);
	 }
	 
	 public Date GetValue()
	 {
		 return value;
	 }
	 /*
	  * Encoding JSON function
	  * @retval boolean returns option true if object is encoded
	  * with Date value
	  */	 
	 public boolean encJsonValue(JsonObjectBuilder obj, String name)
	 {
		 if( is_set ) obj.add(name, (JsonValue) value);
		 return is_set;
	 }

}

	
