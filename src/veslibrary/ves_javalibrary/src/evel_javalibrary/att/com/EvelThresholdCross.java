package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Threshold Crossing event class
 *
  * This file implements the Evel Threshold Crossing event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send Threshold Crossing Alert events.
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
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;

import evel_javalibrary.att.com.EvelMobileFlow.MOBILE_GTP_PER_FLOW_METRICS;


public class EvelThresholdCross extends EvelHeader {
	
	
	int major_version = 1;
	int minor_version = 1;
	
	/**************************************************************************//**
	 * Alert types.
	 * JSON equivalent fields: newState, oldState
	 *****************************************************************************/
	public enum EVEL_EVENT_ACTION {
		  EVEL_EVENT_ACTION_CLEAR,
		  EVEL_EVENT_ACTION_CONTINUE,
		  EVEL_EVENT_ACTION_SET,
		  EVEL_MAX_EVENT_ACTION
	}
	
	public enum EVEL_ALERT_TYPE {
		         EVEL_CARD_ANOMALY, 
    		     EVEL_ELEMENT_ANOMALY, 
    		     EVEL_INTERFACE_ANOMALY, 
    			 EVEL_SERVICE_ANOMALY,
                 EVEL_MAX_ANOMALY
	}
	
	public enum EVEL_SEVERITIES{
		  EVEL_SEVERITY_CRITICAL,
		  EVEL_SEVERITY_MAJOR,
		  EVEL_SEVERITY_MINOR,
		  EVEL_SEVERITY_WARNING,
		  EVEL_SEVERITY_NORMAL,
		  EVEL_MAX_SEVERITIES
		}

	
	/***************************************************************************/
	/* Mandatory fields                                                        */
	/***************************************************************************/

	public class PERF_COUNTER {
		String criticality;
		String name;
		String thresholdCrossed;
		String value;
	}
	
	PERF_COUNTER       additionalParameters;
	EVEL_EVENT_ACTION  alertAction;
    String             alertDescription; 
    EVEL_ALERT_TYPE    alertType;
    Date               collectionTimestamp; 
    EVEL_SEVERITIES    eventSeverity;
    Date               eventStartTimestamp;


	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/
    ArrayList<String[]> additional_info;
    EvelOptionString    alertValue;
    ArrayList<String>   alertidList;
    EvelOptionString    dataCollector;
    EvelOptionString    elementType;
    EvelOptionString    interfaceName;
    EvelOptionString    networkService;
    EvelOptionString    possibleRootCause;

	
	private static final Logger LOGGER = Logger.getLogger( EvelThresholdCross.class.getName() );

	  /**************************************************************************//**
	   * Create a new Threshold Crossing event.
	   *
	   *
	   * @param String tcriticality   Counter Criticality MAJ MIN,
	   * @param String tname          Counter Threshold name
	   * @param String tthresholdCrossed  Counter Threshold crossed value
	   * @param String tvalue             Counter actual value
	   * @param EVEL_EVENT_ACTION talertAction   Alert set continue or clear
	   * @param String  talertDescription
	   * @param EVEL_ALERT_TYPE    talertType    Kind of anamoly
	   * @param Date               tcollectionTimestamp time at which alert was collected
	   * @param EVEL_SEVERITIES    teventSeverity  Severity of Alert
	   * @param Date               teventStartTimestamp Time when this alert started
	   *****************************************************************************/
	public EvelThresholdCross( String evname,String evid,
			                   String tcriticality,
	                           String tname,
	                           String tthresholdCrossed,
	                           String tvalue,
                               EVEL_EVENT_ACTION  talertAction,
                               String             talertDescription, 
                               EVEL_ALERT_TYPE    talertType,
                               Date               tcollectionTimestamp, 
                               EVEL_SEVERITIES    teventSeverity,
                               Date               teventStartTimestamp)
	{
		super(evname,evid);
		event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING;
		assert( tcriticality!= null );
		assert( tname!= null );
		assert( tthresholdCrossed!= null );
		assert( tvalue!= null );
		assert( talertAction!= null );
		
		additionalParameters = new PERF_COUNTER();
		assert( additionalParameters != null);
		
		additionalParameters.criticality = tcriticality;
		additionalParameters.name = tname;
		additionalParameters.thresholdCrossed = tthresholdCrossed;
		additionalParameters.value = tvalue;
        alertAction      =  talertAction;
        alertDescription = talertDescription; 
        alertType        = talertType;
        collectionTimestamp =   tcollectionTimestamp; 
        eventSeverity       =    teventSeverity;
        eventStartTimestamp =    teventStartTimestamp;
		
        additional_info = null;
        alertValue = new EvelOptionString();
        alertidList = null;
        dataCollector = new EvelOptionString();
        elementType = new EvelOptionString();
        interfaceName = new EvelOptionString();
        networkService = new EvelOptionString();
        possibleRootCause = new EvelOptionString();
	}
	
	
	/**************************************************************************//**
	 * Set the Event Type property of the TC Alert.
	 *
	 * @note  The property is treated as immutable: it is only valid to call
	 *        the setter once.  However, we don't assert if the caller tries to
	 *        overwrite, just ignoring the update instead.
	 *
	 * @param type        The Event Type to be set. ASCIIZ string. The caller
	 *                    does not need to preserve the value once the function
	 *                    returns.
	 *****************************************************************************/
	public void evel_thresholdcross_type_set(String type)
	{
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions and call evel_header_type_set.                      */
	  /***************************************************************************/
	  assert(type!=null);
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
	  evel_header_type_set(type);

	  EVEL_EXIT();
	}

	/**************************************************************************//**
	 * Add an optional additional alertid value to Alert.
	 *
	 *****************************************************************************/
	public void evel_thresholdcross_alertid_add(String alertid)
	{
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
	  assert(alertid != null);
	  
	  if( alertidList == null )
	  {
		  alertidList = new ArrayList<String>();
	  }

	  LOGGER.debug(MessageFormat.format("Adding alertid={0}", alertid));

	  alertidList.add(new String(alertid));

	  EVEL_EXIT();
	}
	
	/**************************************************************************//**
	 * Add an optional additional value name/value pair to the Alert.
	 *
	 * The name and value are null delimited ASCII strings.  The library takes
	 * a copy so the caller does not have to preserve values after the function
	 * returns.
	 * @param name      ASCIIZ string with the attribute's name.  The caller
	 *                  does not need to preserve the value once the function
	 *                  returns.
	 * @param value     ASCIIZ string with the attribute's value.  The caller
	 *                  does not need to preserve the value once the function
	 *                  returns.
	 *****************************************************************************/
	public void evel_thresholdcross_addl_info_add(String name, String value)
	{
	  String[] addl_info = null;
	  EVEL_ENTER();

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
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
	   * Set the TCA probable Root cause.
	   *
	   * @param sheader     Possible root cause to Threshold
	   *****************************************************************************/
	  public void evel_threshold_cross_possible_rootcause_set(String sheader)
	  {
		    EVEL_ENTER();

		    /***************************************************************************/
		    /* Check preconditions.                                                    */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
		    
		    assert(sheader != null);

		    possibleRootCause.SetValuePr(
		                           sheader,
		                           "Rootcause value");
		    EVEL_EXIT();
	  }
    
	  /**************************************************************************//**
	   * Set the TCA networking cause.
	   *
	   * @param sheader     Possible networking service value to Threshold
	   *****************************************************************************/
	  public void evel_threshold_cross_networkservice_set(String sheader)
	  {
		    EVEL_ENTER();

		    /***************************************************************************/
		    /* Check preconditions.                                                    */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
		    
		    assert(sheader != null);

		    networkService.SetValuePr(
		                           sheader,
		                           "Networking service value");
		    EVEL_EXIT();
	  }
    
	  /**************************************************************************//**
	   * Set the TCA Interface name.
	   *
	   * @param sheader     Interface name to threshold
	   *****************************************************************************/
	  public void evel_threshold_cross_interfacename_set(String sheader)
	  {
		    EVEL_ENTER();

		    /***************************************************************************/
		    /* Check preconditions.                                                    */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
		    
		    assert(sheader != null);

		    interfaceName.SetValuePr(
		                           sheader,
		                           "TCA Interface name");
		    EVEL_EXIT();
	  }
    
	  /**************************************************************************//**
	   * Set the TCA Data element type.
	   *
	   * @param sheader     element type of Threshold
	   *****************************************************************************/
	  public void evel_threshold_cross_data_elementtype_set(String sheader)
	  {
		    EVEL_ENTER();

		    /***************************************************************************/
		    /* Check preconditions.                                                    */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
		    
		    assert(sheader != null);

		    elementType.SetValuePr(
		                           sheader,
		                           "TCA Element type value");
		    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the TCA Data collector value.
	   *
	   * @param sheader     Data collector value
	   *****************************************************************************/
	  public void evel_threshold_cross_data_collector_set(String sheader)
	  {
		    EVEL_ENTER();

		    /***************************************************************************/
		    /* Check preconditions.                                                    */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
		    
		    assert(sheader != null);

		    dataCollector.SetValuePr(
		                           sheader,
		                           "Datacollector value");
		    EVEL_EXIT();
	  }
    
    
    
	  /**************************************************************************//**
	   * Set the TCA alert value.
	   *
	   * @param sheader     Possible alert value
	   *****************************************************************************/
	  public void evel_threshold_cross_alertvalue_set(String sheader)
	  {
		    EVEL_ENTER();

		    /***************************************************************************/
		    /* Check preconditions.                                                    */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
		    
		    assert(sheader != null);

		    alertValue.SetValuePr(
		                           sheader,
		                           "Alert value");
		    EVEL_EXIT();
	  }

	
	/**************************************************************************//**
	 * Encode the fault in JSON according to AT&T's schema for the TC ALert type.
	 *
	 * @retvalue       JSON object of TC Alert encoding
	 *****************************************************************************/
	 JsonObjectBuilder evelThresholdCrossingObject()
	 {
	    double version = major_version+(double)minor_version/10;

	    EVEL_ENTER();
	  

	  /***************************************************************************/
	  /* Check preconditions.                                                    */
	  /***************************************************************************/
	  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);

	  /***************************************************************************/
	  /* Mandatory fields.                                                       */
	  /***************************************************************************/
	  //encode counter
      JsonObjectBuilder counterobj =  Json.createObjectBuilder()
              .add( "criticality",additionalParameters.criticality)
              .add( "name", additionalParameters.name)
              .add( "thresholdCrossed",additionalParameters.thresholdCrossed)
              .add( "value", additionalParameters.value);

	    
	  JsonObjectBuilder evelrep = Json.createObjectBuilder()
	    		               .add("additionalParameters", counterobj)
	   	                       .add("alertAction", alertAction.toString())
	   	                       .add("alertDescription", alertDescription)
                               .add("alertType", alertType.toString())
                               .add("collectionTimestamp", collectionTimestamp.toString())
                               .add("eventSeverity",eventSeverity.toString())
                               .add("eventStartTimestamp",eventStartTimestamp.toString());

	    /***************************************************************************/
	    /* Optional fields.                                                        */
	    /***************************************************************************/
	    alertValue.encJsonValue(evelrep, "alertValue");
	    if( alertidList != null && alertidList.size() > 0)
	    {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<alertidList.size();i++) {
			  String addl_info = alertidList.get(i);
			  JsonObject obj = Json.createObjectBuilder()
			    	     .add("item",addl_info).build();
			  builder.add(obj);
		    }
			evelrep.add("associatedAlertIdList", builder);
	    }

	    dataCollector.encJsonValue(evelrep, "dataCollector");
	    elementType.encJsonValue(evelrep, "elementType");
	    interfaceName.encJsonValue(evelrep, "interfaceName");
	    networkService.encJsonValue(evelrep, "networkService");
	    possibleRootCause.encJsonValue(evelrep, "possibleRootCause");

	  
	  evelrep.add( "thresholdCrossingFieldsVersion", version);

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
		evelrep.add("additionalFields", builder);
	  }


	  EVEL_EXIT();
	  
	  return evelrep;
	}
	
	
		/**************************************************************************//**
	     * Encode the event as a JSON event object according to AT&T's schema.
	     * retval : String of JSON TCA event message
	     *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_THRESHOLD_CROSSING);
	        
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "thresholdCrossingAlert",evelThresholdCrossingObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }
	

}
