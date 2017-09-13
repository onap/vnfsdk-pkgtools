package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Other class
 *
 * This file implements the Evel Other class to handle Other domain events.
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;
import org.slf4j.helpers.MessageFormatter;

/*
 * Handles Optional Other fields
 */
public class EvelOther extends EvelHeader {

	  int major_version = 1;
	  int minor_version = 1;

	  /***************************************************************************/
	  /* Mandatory fields                                                        */
	  /***************************************************************************/
	  ArrayList<javax.json.JsonObject> additional_objects;
	  
	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  Map<String,String> additional_info;
	  HashMap<String,Map<String,String>> evelmap;
	  

	  private static final Logger LOGGER = Logger.getLogger( EvelOther.class.getName() );

	  /**************************************************************************//**
	   * Create a new Other event.
	   *
	   *****************************************************************************/
	  public EvelOther(String evname,String evid)
	  {
		  //Init header and domain
        super(evname,evid);

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/

	    /***************************************************************************/
	    /* Allocate the measurement.                                               */
	    /***************************************************************************/
	    LOGGER.debug("New Evel Other Object");
	    
	    /***************************************************************************/
	    /* Initialize the header & the measurement fields.                         */
	    /***************************************************************************/
	    event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER;

	    /***************************************************************************/
	    /* Optional fields.                                                    */
	    /***************************************************************************/
	    additional_info = null;
	    additional_objects = null;
	    evelmap = null;

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Event Type property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param type        The Event Type to be set. ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public void evel_other_type_set(String typ)
	  {
	    EVEL_ENTER();
	    assert(typ != null);

	    /***************************************************************************/
	    /* Check preconditions and call evel_header_type_set.                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER);
	    evel_header_type_set(typ);

	    EVEL_EXIT();
	  }

	  
	  
	  
	  /**************************************************************************//**
	   * Adds name value pair under hash key
	   *
	   *
	   * @param hashname String         Hash name.
	   * @param name String             Name.
	   * @param value String            Value.
	   *****************************************************************************/
	  public void evel_other_field_add_namedarray(String hashname,  String name, String value)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER);
	    assert(hashname != null);
	    assert(name != null);
	    assert(value != null);
	    
	    if( evelmap == null)
	       evelmap = new HashMap<String,Map<String,String>>();
	        
	    LOGGER.debug("Adding hash : "+hashname+" name="+name+"value= "+value);
	    
	    Map<String,String> mymap = null;
	    try{
	     mymap = evelmap.get(hashname);
	    } catch( Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    if(mymap == null)
	    	mymap = new HashMap<String,String>();
	    try{
	    if( mymap.put(name, value) == null)
	    	LOGGER.debug("Unable to add map hash : "+hashname+" name="+name+"value= "+value);;
	    
	    if( evelmap.put(hashname, mymap) == null)
	    	LOGGER.debug("Unable to add hash entry : "+hashname+" name="+name+"value= "+value);;
	    } catch( Exception e)
	    {
	    	e.printStackTrace();
	    }

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Add a json object to optional jsonObject list.
	   *
	   * @param jsonobj   Pointer to json object
	   *****************************************************************************/
	  public void evel_other_field_add_jsonobj(javax.json.JsonObject  jsonobj)
	  {

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER);
	    assert(jsonobj != null);

	    LOGGER.debug("Adding jsonObject");
	    
	    if( additional_objects == null )
	    	additional_objects = new ArrayList<javax.json.JsonObject>();
	    
	    additional_objects.add(jsonobj);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add a field name/value pair to the Other.
	   *
	   * The name and value are null delimited ASCII strings.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   * @param name      ASCIIZ string with the field's name.  The caller does not
	   *                  need to preserve the value once the function returns.
	   * @param value     ASCIIZ string with the field's value.  The caller does not
	   *                  need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_other_field_add(String name, String value)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER);
	    assert(name != null);
	    assert(value != null);

	    LOGGER.debug("Adding name="+name+" value="+value);
	    
	    if(additional_info == null)
	    	additional_info = new HashMap<String,String>();
	    
	    if( additional_info.put(name, value) == null)
	    	LOGGER.debug("Unable to add map : name="+name+"value= "+value);

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Encode Other Object according to VES schema
	   *
	   * @retval JSON Object of Other event
	   *****************************************************************************/
	  JsonObjectBuilder evelOtherObject()
	  {

	    double version = major_version+(double)minor_version/10;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER);

	    /***************************************************************************/
	    /* Mandatory fields.                                                       */
	    /***************************************************************************/
	    
	    /***************************************************************************/
	    /* Additional Objects.                                                 */
	    /***************************************************************************/
	    
	    JsonObjectBuilder eveloth = Json.createObjectBuilder();
	    
	    /***************************************************************************/
	    /* Optional fields.                                                        */
	    /***************************************************************************/
	    // additional fields
		  if( additional_info != null )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
           for(Map.Entry<String, String> entry : additional_info.entrySet()){
              LOGGER.debug(MessageFormat.format("Key : {0} and Value: {1}", entry.getKey(), entry.getValue()));
			  JsonObject obj = Json.createObjectBuilder()
			    	     .add("name", entry.getKey())
			    	     .add("value", entry.getValue()).build();
			  builder.add(obj);
		    }
			eveloth.add("nameValuePairs", builder);
		  }
	    		
        if( additional_objects != null && additional_objects.size() > 0 )
	    {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<additional_objects.size();i++) {
			  JsonObject jobj = additional_objects.get(i);
			  builder.add(jobj);
		    }
			eveloth.add("jsonObjects",builder);
	    } 
        
		   if( evelmap != null && evelmap.size() > 0)
		   {
			    JsonArrayBuilder builder = Json.createArrayBuilder();
		        for(Map.Entry<String, Map<String,String>> entry : evelmap.entrySet()){
		              LOGGER.debug(MessageFormat.format("Key : {0} and Value: {1}", entry.getKey(), entry.getValue()));
		              Map<String,String> item = entry.getValue();   
		              JsonArrayBuilder builder2 = Json.createArrayBuilder();              
		              for(Map.Entry<String, String> entry2 : item.entrySet()){
		                  LOGGER.debug(MessageFormat.format("Key : {0} and Value: {1}", entry2.getKey(), entry2.getValue()));
		    			  JsonObject obj = Json.createObjectBuilder()
		    			    	     .add("name", entry2.getKey())
		    			    	     .add("value", entry2.getValue()).build();
		    			  builder2.add(obj);
		    		   }
		              
		              
					  JsonObjectBuilder obj = Json.createObjectBuilder()
					    	     .add(entry.getKey(),builder2);
					  
					  builder.add(obj);
				}
				eveloth.add("hashOfNameValuePairArrays", builder);
		   }

		  
	    

	    /***************************************************************************/
	    /* Although optional, we always generate the version.  Note that this      */
	    /* closes the object, too.                                                 */
	    /***************************************************************************/
	    eveloth.add("otherFieldsVersion", version);

	    EVEL_EXIT();
	    
	    return eveloth;
	  }
	  
	  /**************************************************************************//**
	   * Encode the event as a JSON event object according to AT&T's schema.
	   * retval : String of JSON event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_OTHER);
		//encode common event header and body for other     
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "otherFields",evelOtherObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }


}
