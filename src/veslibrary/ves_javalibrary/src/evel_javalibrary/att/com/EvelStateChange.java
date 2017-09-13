package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel State Change class
 *
  * This file implements the Evel State Change Event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send Agent State change events.
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
import org.slf4j.helpers.MessageFormatter;

import evel_javalibrary.att.com.EvelFault.EVEL_SEVERITIES;


public class EvelStateChange extends EvelHeader {
	
	int major_version = 1;
	int minor_version = 2;
	
	/**************************************************************************//**
	 * Alert types.
	 * JSON equivalent fields: newState, oldState
	 *****************************************************************************/
	public enum EVEL_ENTITY_STATE{
	  EVEL_ENTITY_STATE_IN_SERVICE,
	  EVEL_ENTITY_STATE_MAINTENANCE,
	  EVEL_ENTITY_STATE_OUT_OF_SERVICE,
	  EVEL_MAX_ENTITY_STATES
	}
	
	/***************************************************************************/
	/* Mandatory fields                                                        */
	/***************************************************************************/
	  EVEL_ENTITY_STATE new_state;
	  EVEL_ENTITY_STATE old_state;
	  String state_interface;

	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/
	  ArrayList<String[]> additional_info;
	
	  private static final Logger LOGGER = Logger.getLogger( EvelStateChange.class.getName() );

	  /**************************************************************************//**
	   * Create a new State Change event.
	   *
	   * @note    The mandatory fields on the State Change must be supplied to this
	   *          factory function and are immutable once set.  Optional fields have
	   *          explicit setter functions, but again values may only be set once
	   *          so that the State Change has immutable properties.
	   *
	   * @param new_state     The new state of the reporting entity.
	   * @param old_state     The old state of the reporting entity.
	   * @param interface     The card or port name of the reporting entity.
	   *
	   *****************************************************************************/
	public EvelStateChange(String evname, String evid,
			               EVEL_ENTITY_STATE newstate,
                           EVEL_ENTITY_STATE oldstate,
                           String interfce)
	{
		super(evname,evid);
		event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_STATE_CHANGE;
		assert(EVEL_ENTITY_STATE.EVEL_MAX_ENTITY_STATES.compareTo(newstate) < 0 );
		assert(EVEL_ENTITY_STATE.EVEL_MAX_ENTITY_STATES.compareTo(oldstate) < 0 );
		assert( interfce != null);
		
		new_state = newstate;
		old_state = oldstate;
		state_interface = interfce;

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
	public void evel_statechange_addl_info_add(String name, String value)
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
	 * Convert a ::EVEL_ENTITY_STATE to it's string form for JSON encoding.
	 *
	 * @param state         The entity state to encode.
	 *
	 * @returns the corresponding string
	 *****************************************************************************/
	String evel_entity_state(EVEL_ENTITY_STATE state)
	{
	  String result=null;

	  EVEL_ENTER();

	  switch (state)
	  {
	    case EVEL_ENTITY_STATE_IN_SERVICE:
	      result = "inService";
	      break;

	    case EVEL_ENTITY_STATE_MAINTENANCE:
	      result = "maintenance";
	      break;

	    case EVEL_ENTITY_STATE_OUT_OF_SERVICE:
	      result = "outOfService";
	      break;

	    default:
	      LOGGER.error("Unexpected entity state "+state);
	      System.exit(1);
	  }

	  EVEL_EXIT();

	  return result;
	}


	/**************************************************************************//**
	 * Encode the State Change in JSON according to AT&T's schema.
	 *
	 *****************************************************************************/
	 JsonObjectBuilder evelStateChangeObject()
	 {
	  String nstate;
	  String ostate;
	  double version = major_version+(double)minor_version/10;

	  EVEL_ENTER();
	  

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_STATE_CHANGE);



	  /***************************************************************************/
	  /* Mandatory fields.                                                       */
	  /***************************************************************************/
      nstate = evel_entity_state(new_state);
      ostate = evel_entity_state(old_state);
	  
	  JsonObjectBuilder evelstate = Json.createObjectBuilder()
	   	         .add("newState", nstate)
	   	         .add("oldState", ostate)
	   	         .add("stateInterface", state_interface);
	  
	  evelstate.add( "stateChangeFieldsVersion", version);

	  /***************************************************************************/
	  /* Optional additional information      */
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
	   * retval : String of JSON state change event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_STATE_CHANGE);
	    //encode header and state change fields    
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "stateChangeFields",evelStateChangeObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }
	

}
