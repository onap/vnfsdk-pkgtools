package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Option Long class
 *
 * This file implements the Evel Option class to handle optional Long fields.
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

import org.apache.log4j.Logger;
/*
 * Handles Optional Long Integer fields
 */
public class EvelOptionLong extends EvelOption {

	/**************************************************************************//**
	 * Optional parameter holder for string.
	 *****************************************************************************/
     Long value;
     
     private static final Logger LOGGER = Logger.getLogger( EvelOptionLong.class.getName() );
     
	 public EvelOptionLong()
	 {
		 super(false);
		 value = 0L;
	 }
	 
	 public EvelOptionLong(boolean val, Long str)
	 {
		 super(val);
		 value = str;
	 }
	 
	 public void InitValue()
	 {
		 is_set = false;
		 value = 0L;
	 }
	 //Setter
	 public void SetValue(Long str)
	 {
		 is_set = true;
		 value = str;
	 }
	 
	//Sets Long Integer value outputting debugging message
	 public void SetValuePr(Long str, String mstr)
	 {
		 
		 is_set = true;
		 value = str;	
		 LOGGER.debug("Setting "+mstr+" to "+str);
	 }
	 
	 public Long GetValue()
	 {
		 return value;
	 }

}

