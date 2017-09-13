package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Heartbeat field class
 *
 * This file implements the Evel Heartbeat Event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send Agent status.
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

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;


public class EvelHeartbeatField extends EvelHeader {
	
	//version of Heartbeat field format revisions
	int major_version = 1;
	int minor_version = 2;
	
	/**************************************************************************//**
	 * Alert types.
	 * JSON equivalent fields: newState, oldState
	 *****************************************************************************/
	
	/***************************************************************************/
	/* Mandatory fields                                                        */
	/***************************************************************************/
	int    heartbeat_interval;


	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/
	  ArrayList<String[]> additional_info;
	
	  private static final Logger LOGGER = Logger.getLogger( EvelHeartbeatField.class.getName() );

	  /**************************************************************************//**
	   * Construct Heartbeat field event.
	   *
	   * @param interval     The Heartbeat interval at which messages are sent.
	   * 
	   *****************************************************************************/
	public EvelHeartbeatField(int interval,String evname,String evid)
	{
		super(evname,evid);
		event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_HEARTBEAT_FIELD;
		assert( interval > 0 );
		
		heartbeat_interval = interval;

		additional_info = null;		
	}
	
	/**************************************************************************//**
	 * Add an additional value name/value pair to the Fault.
	 *
	 * The name and value are null delimited ASCII strings.  The library takes
	 * a copy so the caller does not have to preserve values after the function
	 * returns.
	 *
	 * @param name      ASCIIZ string with the attribute's name.  The caller
	 *                  does not need to preserve the value once the function
	 *                  returns.
	 * @param value     ASCIIZ string with the attribute's value.  The caller
	 *                  does not need to preserve the value once the function
	 *                  returns.
	 *****************************************************************************/
	public void evel_hrtbt_field_addl_info_add(String name, String value)
	{
	  String[] addl_info = null;
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_STATE_CHANGE);
	  assert(name != null);
	  assert(value != null);
	  
	  if( additional_info == null )
	  {
		  additional_info = new ArrayList<String[]>();
	  }

	  LOGGER.debug(MessageFormat.format("Adding name={0} value={1}", name, value));
	  addl_info = new String[2];
	  assert(addl_info != null);
	  addl_info[0] = name;
	  addl_info[1] = value;

	  additional_info.add(addl_info);

	  EVEL_EXIT();
	}
	
	/**************************************************************************//**
	 * Set the Interval property of the Heartbeat fields event.
	 *
	 * @note  The property is treated as immutable: it is only valid to call
	 *        the setter once.  However, we don't assert if the caller tries to
	 *        overwrite, just ignoring the update instead.
	 *
	 * @param interval      Heartbeat interval.
	 *****************************************************************************/
	public void evel_hrtbt_interval_set( int interval)
	{
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions and call evel_set_option_string.                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_HEARTBEAT_FIELD);
	  assert(interval > 0);

	  heartbeat_interval = interval;
	  EVEL_EXIT();
	}


	/**************************************************************************//**
	 * Encode the Heartbeat field in JSON according to AT&T's schema.
	 *
	 * @retval JsonObjectBuilder of Heartbeat field body portion of message   
	 *****************************************************************************/
	 JsonObjectBuilder evelHeartbeatFieldObject()
	 {
	  double version = major_version+(double)minor_version/10;

	  EVEL_ENTER();
	  

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_HEARTBEAT_FIELD);

	  /***************************************************************************/
	  /* Mandatory fields.                                                       */
	  /***************************************************************************/
	  
	  JsonObjectBuilder evelstate = Json.createObjectBuilder()
	   	         .add("heartbeatInterval", heartbeat_interval);
	  
	  evelstate.add( "heartbeatFieldsVersion", version);

	  /***************************************************************************/
	  /* Checkpoint, so that we can wind back if all fields are suppressed.      */
	  /***************************************************************************/
	  if( additional_info != null )
	  {
	    JsonArrayBuilder builder = Json.createArrayBuilder();
	    for(int i=0;i<additional_info.size();i++) {
		  String[] addl_info = additional_info.get(i);
		  JsonObject obj = Json.createObjectBuilder()
		    	     .add("name", addl_info[0])
		    	     .add("value", addl_info[1]).build();
		  builder.add(obj);
	    }
		evelstate.add("additionalFields", builder);
	  }

	  EVEL_EXIT();
	  
	  return evelstate;
	}
	
	
	  /**************************************************************************//**
	   * Encode the event as a JSON event object according to AT&T's schema.
	   * retval : String of JSON event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_HEARTBEAT_FIELD);
	    //encode common event header and body    
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "heartbeatFields",evelHeartbeatFieldObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }
	

}
