package evel_javalibrary.att.com;

/**************************************************************************//**
 * @file
 * Evel SIP Signalling class
 *
  * This file implements the Evel SIP Signaling Event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send SIP events.
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


public class EvelSipSignaling extends EvelHeader {
	
	int major_version = 2;
	int minor_version = 1;
	
	/**************************************************************************//**
	 * Vendor VNF Name fields.
	 * JSON equivalent field: vendorVnfNameFields
	 *****************************************************************************/
	public class VENDOR_VNFNAME_FIELD {
	  String vendorname;
	  EvelOptionString vfmodule;
	  EvelOptionString vnfname;
	}
	
	/***************************************************************************/
	/* Mandatory fields                                                        */
	/***************************************************************************/
	  VENDOR_VNFNAME_FIELD vnfname_field;
	  String correlator;                         /* JSON: correlator */
	  String local_ip_address;               /* JSON: localIpAddress */
	  String local_port;                          /* JSON: localPort */
	  String remote_ip_address;             /* JSON: remoteIpAddress */
	  String remote_port;                        /* JSON: remotePort */
	  	  
	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  ArrayList<String[]> additional_info;
	  EvelOptionString compressed_sip;                  /* JSON: compressedSip */
	  EvelOptionString summary_sip;                        /* JSON: summarySip */


	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/

	  private static final Logger LOGGER = Logger.getLogger( EvelSipSignaling.class.getName() );


	  /**************************************************************************//**
	   * Create a new SIP Signaling event.
	   * @param corlator  Correlator value
	   * @param locip_address  Local IP address
	   * @param loc_port       Local Port
	   * @param remip_address   Remote IP address
	   * @param rem_port        Remote Port
	   *
	   *****************************************************************************/
	  public EvelSipSignaling(String evname,String evid,
			  String vendr_name,
              String corlator,
              String locip_address,
              String loc_port,
              String remip_address,
              String rem_port)
	  {//Init header
        super(evname,evid);

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(vendr_name != null);
	    assert(corlator != null);
	    assert(locip_address != null);
	    assert(loc_port != null);
	    
	    assert(remip_address != null);
	    assert(rem_port != null);

	    LOGGER.debug("New SipSignaling vendor "+vendr_name+" correlator"+corlator+"local_ip_address"+locip_address+"local port"+loc_port+"remote ip address"+remip_address+"remote port"+rem_port);

	    /***************************************************************************/
	    /* Initialize the header & the Domain                        */
	    /***************************************************************************/
	    event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING;
	    /***************************************************************************/
	    /* Initialize mandatory fields                       */
	    /***************************************************************************/
	    
	    correlator = corlator;
		local_ip_address = locip_address;
		local_port = loc_port;
		remote_ip_address = remip_address;
		remote_port = rem_port;
		
	    /***************************************************************************/
	    /* Optional fields.                                                    */
	    /***************************************************************************/
		
		vnfname_field = new VENDOR_VNFNAME_FIELD();
		vnfname_field.vendorname = vendr_name;
		vnfname_field.vfmodule = new EvelOptionString();
		vnfname_field.vnfname = new EvelOptionString();

	    additional_info = null;
	    compressed_sip = new EvelOptionString();
	    summary_sip = new EvelOptionString();

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Event Type property of the SIP signaling.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
       * @param type        The Event Type to be set. ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	   public void evel_signaling_type_set(String typ)
	  {
	    EVEL_ENTER();
	    assert(typ != null);

	    /***************************************************************************/
	    /* Check preconditions and call evel_header_type_set.                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    evel_header_type_set(typ);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Local Ip Address property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param local_ip_address
	   *                      The Local Ip Address to be set. ASCIIZ string. The
	   *                      caller does not need to preserve the value once the
	   *                      function returns.
	   *****************************************************************************/
	  public  void evel_signaling_local_ip_address_set(
	                                           String loc_ip_address)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(loc_ip_address != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Local IP "+loc_ip_address);

	    local_ip_address = loc_ip_address;


	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Local Port property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param local_port    The Local Port to be set. ASCIIZ string. The caller
	   *                      does not need to preserve the value once the function
	   *                      returns.
	   *****************************************************************************/
	  public  void evel_signaling_local_port_set(String loc_port)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(loc_port != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Local Port "+loc_port);

	    local_port = loc_port;

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Remote Ip Address property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param remote_ip_address
	   *                      The Remote Ip Address to be set. ASCIIZ string. The
	   *                      caller does not need to preserve the value once the
	   *                      function returns.
	   *****************************************************************************/
	  public  void evel_signaling_remote_ip_address_set(String remip_address)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(remip_address != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Remote IP Address "+remip_address);

	    remote_ip_address = remip_address;
	    
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Remote Port property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param remote_port   The Remote Port to be set. ASCIIZ string. The caller
	   *                      does not need to preserve the value once the function
	   *                      returns.
	   *****************************************************************************/
	  public  void evel_signaling_remote_port_set(String rem_port)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(rem_port != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Remote Port "+rem_port);

	    remote_port = rem_port;
	    
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Vendor module property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param modulename    The module name to be set. ASCIIZ string. The caller
	   *                      does not need to preserve the value once the function
	   *                      returns.
	   *****************************************************************************/
	  public  void evel_signaling_vnfmodule_name_set(String module_name)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(module_name != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Module Name "+module_name);
	    
	    vnfname_field.vfmodule.SetValue(module_name);
	    
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Vendor module property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param vnfname       The Virtual Network function to be set. ASCIIZ string.
	   *                      The caller does not need to preserve the value once
	   *			the function returns.
	   *****************************************************************************/
	  public void evel_signaling_vnfname_set(String vnfname)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(vnfname != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling VNF Name "+vnfname);
	    
	    vnfname_field.vnfname.SetValue(vnfname);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Compressed SIP property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param compressed_sip
	   *                      The Compressed SIP to be set. ASCIIZ string. The caller
	   *                      does not need to preserve the value once the function
	   *                      returns.
	   *****************************************************************************/
	  public  void evel_signaling_compressed_sip_set(String compr_sip)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(compr_sip != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Compressed SIP "+compr_sip);
	    
	    compressed_sip.SetValue(compr_sip);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Summary SIP property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param summary_sip   The Summary SIP to be set. ASCIIZ string. The caller
	   *                      does not need to preserve the value once the function
	   *                      returns.
	   *****************************************************************************/
	  public void evel_signaling_summary_sip_set(String summ_sip)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(summ_sip != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting SipSignaling Summary SIP "+summ_sip);
	    
	    summary_sip.SetValue(summ_sip);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Correlator property of the Signaling event.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param correlator    The correlator to be set. ASCIIZ string. The caller
	   *                      does not need to preserve the value once the function
	   *                      returns.
	   *****************************************************************************/
	  public void evel_signaling_correlator_set(String corlator)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions and call evel_header_type_set.                      */
	    /***************************************************************************/
	    assert(corlator != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    LOGGER.debug("Setting Correlator "+corlator);
	    
	    correlator = corlator;
	    
	    EVEL_EXIT();
	  }
	  
	  /**************************************************************************//**
	   * Add an additional value name/value pair to the Signaling.
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
	  public void evel_signaling_addl_info_add(String name, String value)
		{
		  String[] addl_info = null;
		  EVEL_ENTER();

		  /***************************************************************************/
		  /* Check preconditions.                                                    */
		  /***************************************************************************/
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
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
	   * Encode SIP Signaling Object according to VES schema
	   *
	   * @retval JSON Object of SIP event
	   *****************************************************************************/
	  JsonObjectBuilder evelSipSignalingObject()
	  {

	    double version = major_version+(double)minor_version/10;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);

	    /***************************************************************************/
	    /* Mandatory fields.                                                       */
	    /***************************************************************************/
	    
        JsonObjectBuilder vnfnamedobj =  Json.createObjectBuilder()
                .add( "vendorName",vnfname_field.vendorname);
            vnfname_field.vfmodule.encJsonValue(vnfnamedobj,"vfModuleName");
            vnfname_field.vfmodule.encJsonValue(vnfnamedobj,"vnfName");
	    
	    JsonObjectBuilder evelsip = Json.createObjectBuilder()
	   	                          .add("correlator", correlator)
	   	                          .add("localIpAddress", local_ip_address)
	   	                          .add("localPort", local_port)
                                  .add("remoteIpAddress", remote_ip_address)
                                  .add("remotePort", remote_port)
                                  .add("vendorVnfNamedFields", vnfnamedobj);
               	    	     
	    
	    /***************************************************************************/
	    /* Optional fields.                                                        */
	    /***************************************************************************/
	    compressed_sip.encJsonValue(evelsip, "compressedSip");
	    summary_sip.encJsonValue(evelsip, "summarySip");
	    
	    
	    // additional fields
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
			evelsip.add("additionalFields", builder);
		  }
	    

	    /***************************************************************************/
	    /* Although optional, we always generate the version.  Note that this      */
	    /* closes the object, too.                                                 */
	    /***************************************************************************/
	    evelsip.add("signalingFieldsVersion", version);

	    EVEL_EXIT();
	    
	    return evelsip;
	  }
	  
	  /**************************************************************************//**
	   * Encode the event as a JSON event object according to AT&T's schema.
	   *
	   * retval : String of JSON event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_SIPSIGNALING);
	    //encode commonheader and sip signaling body fields    
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "signalingFields",evelSipSignalingObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }


}
