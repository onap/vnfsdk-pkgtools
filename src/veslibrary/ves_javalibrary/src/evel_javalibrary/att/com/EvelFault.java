package evel_javalibrary.att.com;

/**************************************************************************//**
 * @file
 * Evel Fault Event class extends EvelHeader class
 *
 * This file implements the Evel Fault Event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send Fault events.
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


public class EvelFault extends EvelHeader {
	//version of EvelFault format revisions
	int major_version = 2;
	int minor_version = 1;
	
	/**************************************************************************//**
	 * Fault / Threshold severities.
	 * JSON equivalent field: eventSeverity
	 *****************************************************************************/
	public enum EVEL_SEVERITIES{
	  EVEL_SEVERITY_CRITICAL,
	  EVEL_SEVERITY_MAJOR,
	  EVEL_SEVERITY_MINOR,
	  EVEL_SEVERITY_WARNING,
	  EVEL_SEVERITY_NORMAL,
	  EVEL_MAX_SEVERITIES
	}

	/**************************************************************************//**
	 * Fault source types.
	 * JSON equivalent field: eventSourceType
	 *****************************************************************************/
	public enum EVEL_SOURCE_TYPES{
	  EVEL_SOURCE_OTHER,
	  EVEL_SOURCE_ROUTER,
	  EVEL_SOURCE_SWITCH,
	  EVEL_SOURCE_HOST,
	  EVEL_SOURCE_CARD,
	  EVEL_SOURCE_PORT,
	  EVEL_SOURCE_SLOT_THRESHOLD,
	  EVEL_SOURCE_PORT_THRESHOLD,
	  EVEL_SOURCE_VIRTUAL_MACHINE,
	  EVEL_SOURCE_VIRTUAL_NETWORK_FUNCTION,
	  /***************************************************************************/
	  /* START OF VENDOR-SPECIFIC VALUES                                         */
	  /*                                                                         */
	  /* Vendor-specific values should be added here, and handled appropriately  */
	  /* in evel_event.c.                                                        */
	  /***************************************************************************/

	  /***************************************************************************/
	  /* END OF VENDOR-SPECIFIC VALUES                                           */
	  /***************************************************************************/
	  EVEL_MAX_SOURCE_TYPES
	}

	/**************************************************************************//**
	 * Fault VNF Status.
	 * JSON equivalent field: vfStatus
	 *****************************************************************************/
	public enum EVEL_VF_STATUSES{
	  EVEL_VF_STATUS_ACTIVE,
	  EVEL_VF_STATUS_IDLE,
	  EVEL_VF_STATUS_PREP_TERMINATE,
	  EVEL_VF_STATUS_READY_TERMINATE,
	  EVEL_VF_STATUS_REQ_TERMINATE,
	  EVEL_MAX_VF_STATUSES
	}

	
	/***************************************************************************/
	/* Mandatory fields                                                        */
	/***************************************************************************/
	  EVEL_SEVERITIES event_severity;
	  EVEL_SOURCE_TYPES event_source_type;
	  String alarm_condition;
	  String specific_problem;
	  EVEL_VF_STATUSES vf_status;

	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/
	  EvelOptionString category;
	  EvelOptionString alarm_interface_a;
	  ArrayList<String[]> additional_info;
	
	  private static final Logger LOGGER = Logger.getLogger( EvelFault.class.getName() );

	  /**************************************************************************//**
	   * Create a new fault event.
	   *
	   * @note    The mandatory fields on the Fault must be supplied to this factory
	   *          function and are immutable once set.  Optional fields have explicit
	   *          setter functions, but again values may only be set once so that the
	   *          Fault has immutable properties.
	   * @param   condition   The condition indicated by the Fault.
	   * @param   specproblem  The specific problem triggering the fault.
	   * @param   priority    The priority of the event.
	   * @param   severity    The severity of the Fault.
	   * @param   ev_source_type    Source of Alarm event
	   * @param   status      status of Virtual Function
	   *****************************************************************************/
	public EvelFault(String evname, String ev_id,
			         String condition, String specproblem,
                     EvelHeader.PRIORITIES priority,
                     EVEL_SEVERITIES severity,
                     EVEL_SOURCE_TYPES ev_source_type,
                     EVEL_VF_STATUSES status)
	{
		//Initializes Evel Header and Domain
		super(evname,ev_id);		
		event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT;
		//Validate inputs
		assert( condition != null);
		assert( specific_problem != null);
		assert(EvelHeader.PRIORITIES.EVEL_MAX_PRIORITIES.compareTo(priority) < 0 );
		assert(EVEL_SEVERITIES.EVEL_MAX_SEVERITIES.compareTo(severity) < 0 );
		assert(EVEL_VF_STATUSES.EVEL_MAX_VF_STATUSES.compareTo(status) < 0 );
		//Init mandatory fields
		event_severity = severity;
		event_source_type = ev_source_type;
		alarm_condition = condition;
		specific_problem = specproblem;
		vf_status = status;
		//Init optional fields
		category = new EvelOptionString(false, null);
		alarm_interface_a = new EvelOptionString(false, null);
		additional_info = null;		
	}
	
	/**************************************************************************//**
	 * Add an additional value name/value pair to the Fault.
	 *
	 * The name and value are null delimited ASCII strings.  The library takes
	 * a copy so the caller does not have to preserve values after the function
	 * returns.
	 *
	 * @param fault     Pointer to the fault.
	 * @param name      ASCIIZ string with the attribute's name.  The caller
	 *                  does not need to preserve the value once the function
	 *                  returns.
	 * @param value     ASCIIZ string with the attribute's value.  The caller
	 *                  does not need to preserve the value once the function
	 *                  returns.
	 *****************************************************************************/
	public void evel_fault_addl_info_add(String name, String value)
	{
	  String[] addl_info = null;
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT);
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
	 * Set the Fault Category property of the Fault.
	 *
	 * @note  The property is treated as immutable: it is only valid to call
	 *        the setter once.  However, we don't assert if the caller tries to
	 *        overwrite, just ignoring the update instead.
	 *
	 * @param fault      Pointer to the fault.
	 * @param category   Category : license, link, routing, security, signaling.
	 *			 ASCIIZ string. The caller
	 *                   does not need to preserve the value once the function
	 *                   returns.
	 *****************************************************************************/
	public void evel_fault_category_set( String categ)
	{
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT);
	  assert(categ != null);
	  
	  category.SetValuePr(categ,"Fault Category set");

	  EVEL_EXIT();
	}

	/**************************************************************************//**
	 * Set the Alarm Interface A property of the Fault.
	 *
	 * @note  The property is treated as immutable: it is only valid to call
	 *        the setter once.  However, we don't assert if the caller tries to
	 *        overwrite, just ignoring the update instead.
	 *
	 * @param fault      Pointer to the fault.
	 * @param interface  The Alarm Interface A to be set. ASCIIZ string. The caller
	 *                   does not need to preserve the value once the function
	 *                   returns.
	 *****************************************************************************/
	public void evel_fault_interface_set(String intf)
	{
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT);
	  assert(intf != null);

	  alarm_interface_a.SetValuePr(intf,"Alarm Interface A");

	  EVEL_EXIT();
	}

	/**************************************************************************//**
	 * Set the Event Type property of the Fault.
	 *
	 * @note  The property is treated as immutable: it is only valid to call
	 *        the setter once.  However, we don't assert if the caller tries to
	 *        overwrite, just ignoring the update instead.
	 *
	 * @param fault      Pointer to the fault.
	 * @param type       The Event Type to be set. ASCIIZ string. The caller
	 *                   does not need to preserve the value once the function
	 *                   returns.
	 *****************************************************************************/
	public void evel_fault_type_set(String type)
	{
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions and call evel_header_type_set.                      */
	  /***************************************************************************/
	  assert(type != null);
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT);
	  
	  evel_header_type_set(type);

	  EVEL_EXIT();
	}

	
	/**************************************************************************//**
	 * Map an ::EVEL_SOURCE_TYPES enum value to the equivalent string.
	 *
	 * @param source_type   The source type to convert.
	 * @returns The equivalent string.
	 *****************************************************************************/
	String evel_source_type(EVEL_SOURCE_TYPES source_type)
	{
	  String result;

	  EVEL_ENTER();

	  switch (source_type)
	  {
	    case EVEL_SOURCE_OTHER:
	      result = "other";
	      break;

	    case EVEL_SOURCE_ROUTER:
	      result = "router";
	      break;

	    case EVEL_SOURCE_SWITCH:
	      result = "switch";
	      break;

	    case EVEL_SOURCE_HOST:
	      result = "host";
	      break;

	    case EVEL_SOURCE_CARD:
	      result = "card";
	      break;

	    case EVEL_SOURCE_PORT:
	      result = "port";
	      break;

	    case EVEL_SOURCE_SLOT_THRESHOLD:
	      result = "slotThreshold";
	      break;

	    case EVEL_SOURCE_PORT_THRESHOLD:
	      result = "portThreshold";
	      break;

	    case EVEL_SOURCE_VIRTUAL_MACHINE:
	      result = "virtualMachine";
	      break;

	    case EVEL_SOURCE_VIRTUAL_NETWORK_FUNCTION:
	      result = "virtualNetworkFunction";
	      break;

	    default:
	      result = null;
	      LOGGER.error(MessageFormatter.format("Unexpected Event Source Type {0}", source_type));
	      System.exit(1);
	  }

	  EVEL_EXIT();

	  return result;
	}
	
	/**************************************************************************//**
	 * Map an ::EVEL_SEVERITIES enum value to the equivalent string.
	 *
	 * @param severity      The severity to convert.
	 * @returns The equivalent string.
	 *****************************************************************************/
	String evel_severity(EVEL_SEVERITIES severity)
	{
	  String result = null;

	  EVEL_ENTER();

	  switch (severity)
	  {
	    case EVEL_SEVERITY_CRITICAL:
	      result = "CRITICAL";
	      break;

	    case EVEL_SEVERITY_MAJOR:
	      result = "MAJOR";
	      break;

	    case EVEL_SEVERITY_MINOR:
	      result = "MINOR";
	      break;

	    case EVEL_SEVERITY_WARNING:
	      result = "WARNING";
	      break;

	    case EVEL_SEVERITY_NORMAL:
	      result = "NORMAL";
	      break;

	    default:
	      LOGGER.error("Unexpected event severity "+severity);
	      System.exit(1);
	  }

	  EVEL_EXIT();

	  return result;
	}


	/**************************************************************************//**
	 * Map an ::EVEL_VF_STATUSES enum value to the equivalent string.
	 *
	 * @param vf_status     The vf_status to convert.
	 * @returns The equivalent string.
	 *****************************************************************************/
	String evel_vf_status(EVEL_VF_STATUSES vf_status)
	{
	  String result;

	  EVEL_ENTER();

	  switch (vf_status)
	  {
	    case EVEL_VF_STATUS_ACTIVE:
	      result = "Active";
	      break;

	    case EVEL_VF_STATUS_IDLE:
	      result = "Idle";
	      break;

	    case EVEL_VF_STATUS_PREP_TERMINATE:
	      result = "Preparing to terminate";
	      break;

	    case EVEL_VF_STATUS_READY_TERMINATE:
	      result = "Ready to terminate";
	      break;

	    case EVEL_VF_STATUS_REQ_TERMINATE:
	      result = "Requesting termination";
	      break;

	    default:
	      result = null;
	      LOGGER.error("Unexpected VF Status "+vf_status);
	      System.exit(1);
	  }

	  EVEL_EXIT();

	  return result;
	}

	/**************************************************************************//**
	 * Encode the fault in JSON according to AT&T's schema for the fault type.
	 *
	 * @retval JsonObjectBuilder of fault body portion of message   
	 *****************************************************************************/
	 JsonObjectBuilder evelFaultObject()
	 {
	  String fault_severity;
	  String fault_source_type;
	  String fault_vf_status;
	  double version = major_version+(double)minor_version/10;

	  EVEL_ENTER();
	  
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT);

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/

      fault_severity = evel_severity(event_severity);
	  fault_source_type = evel_source_type(event_source_type);
	  fault_vf_status = evel_vf_status(vf_status);
	  
	  JsonObjectBuilder evelfault = Json.createObjectBuilder()
	   	         .add("alarmCondition", alarm_condition);

	  /***************************************************************************/
	  /* Optional fields.                                                        */
	  /***************************************************************************/
	  
	  if( category.is_set )
		  evelfault.add("eventCategory", category.GetValue());
	  if( alarm_interface_a.is_set )
		  evelfault.add("eventCategory", alarm_interface_a.GetValue());
	  

	  /***************************************************************************/
	  /* Mandatory fields.                                                       */
	  /***************************************************************************/
	  evelfault.add( "eventSeverity", fault_severity);
	  evelfault.add( "eventSourceType", fault_source_type);
	  evelfault.add( "specificProblem", specific_problem);
	  evelfault.add( "vfStatus", fault_vf_status);
	  evelfault.add( "faultFieldsVersion", version);

	  /***************************************************************************/
	  /* Encode additional Name value pairs if any.      */
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
		evelfault.add("alarmAdditionalInformation", builder);
	  }

	  EVEL_EXIT();
	  
	  return evelfault;
	}
	
	
	  /**************************************************************************//**
	   * Encode the event as a JSON event object according to AT&T's schema.
	   * retval : String of JSON event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_FAULT);
		//encode common event header and body     
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "faultFields",evelFaultObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }
	

}
