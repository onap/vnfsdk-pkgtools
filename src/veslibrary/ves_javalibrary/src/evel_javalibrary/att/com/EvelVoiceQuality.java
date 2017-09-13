package evel_javalibrary.att.com;

/**************************************************************************//**
 * @file
 * Evel Voice Quality event class
 *
  * This file implements the Evel TVoice Quality event event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send Voice Quality event reports.
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



public class EvelVoiceQuality extends EvelHeader {
	
	int major_version = 1;
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
	
	/**************************************************************************//**
	 * Service Event endpoint description
	 * JSON equivalent field: endpointDesc
	 *****************************************************************************/
	public enum EVEL_SERVICE_ENDPOINT_DESC {
	  EVEL_SERVICE_ENDPOINT_CALLEE,
	  EVEL_SERVICE_ENDPOINT_CALLER,
	  EVEL_MAX_SERVICE_ENDPOINT_DESC
	}
	
	/**************************************************************************//**
	 * End of Call Voice Quality Metrices
	 * JSON equivalent field: endOfCallVqmSummaries
	 *****************************************************************************/
	public class END_OF_CALL_VOICE_QUALITY_METRICS {
		/***************************************************************************/
		/* Mandatory fields                                                        */
		/***************************************************************************/
		String adjacencyName;
		String endpointDescription;

		/***************************************************************************/
		/* Optional fields                                                         */
		/***************************************************************************/
		EvelOptionDouble endpointJitter;
		EvelOptionDouble endpointRtpOctetsDiscarded;
		EvelOptionDouble endpointRtpOctetsReceived;
		EvelOptionDouble endpointRtpOctetsSent;
		EvelOptionDouble endpointRtpPacketsDiscarded;
		EvelOptionDouble endpointRtpPacketsReceived;
		EvelOptionDouble endpointRtpPacketsSent;
		EvelOptionDouble localJitter;
		EvelOptionDouble localRtpOctetsDiscarded;
		EvelOptionDouble localRtpOctetsReceived;
		EvelOptionDouble localRtpOctetsSent;
		EvelOptionDouble localRtpPacketsDiscarded;
		EvelOptionDouble localRtpPacketsReceived;
		EvelOptionDouble localRtpPacketsSent;
		EvelOptionDouble mosCqe;
		EvelOptionDouble packetsLost;
		EvelOptionDouble packetLossPercent;
		EvelOptionDouble rFactor;
		EvelOptionDouble roundTripDelay;
	}

	/***************************************************************************/
	  /* Mandatory fields                                                        */
	  /***************************************************************************/
	  VENDOR_VNFNAME_FIELD vnfname_field;
	  String calleeSideCodec;
	  String callerSideCodec;
	  String correlator;
	  String midCallRtcp;

	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  ArrayList<String[]> additional_info;
	  END_OF_CALL_VOICE_QUALITY_METRICS endOfCallVqmSummaries;
	  EvelOptionString evphoneNumber;

	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/

	  private static final Logger LOGGER = Logger.getLogger( EvelVoiceQuality.class.getName() );


	  /**************************************************************************//**
	   * Create a new Voice Quality event.
	   *
	   * @note    The mandatory fields on the Voice Quality must be supplied to this
	   *          factory function and are immutable once set.  Optional fields have
	   *          explicit setter functions, but again values may only be set once so
	   *          that the event has immutable properties.
	   *
	   * @param   calleeSideCodec    callee codec for voice call
	   * @param   callerSideCodec    caller codec
	   * @param   corlator           Correlator
	   * @param   midCallRtcp        Midcall RTCP value
	   * @param   vendr_name         Vendor name
	   *
	   * @returns pointer to the newly manufactured ::EVENT_MEASUREMENT.  If the
	   *          event is not used (i.e. posted) it must be released using
	   *          ::evel_free_event.
	   * @retval  null  Failed to create the event.
	   *****************************************************************************/
	  public EvelVoiceQuality(String evname, String evid,
			    String calleeSideCodc,
			    String callerSideCodc, String corlator,
			    String midCllRtcp, String vendr_name)
	  {
        super(evname,evid);

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(vendr_name != null);
	    assert(calleeSideCodc != null);
	    assert(callerSideCodc != null);
	    assert(midCllRtcp != null);
	    assert(corlator != null);
	 

	    /***************************************************************************/
	    /* Allocate the measurement.                                               */
	    /***************************************************************************/
	    LOGGER.debug("New Voice Quality vendor "+vendr_name+" correlator"+corlator+"calleeSideCodec"+calleeSideCodc+"callerSideCodec"+callerSideCodc+"midCallRtcp"+midCllRtcp);

	    /***************************************************************************/
	    /* Initialize the header & the measurement fields.                         */
	    /***************************************************************************/
	    event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY;
	    correlator = corlator;
		calleeSideCodec = calleeSideCodc;
		callerSideCodec = callerSideCodc;
		midCallRtcp = midCllRtcp;
		
		vnfname_field = new VENDOR_VNFNAME_FIELD();
		vnfname_field.vendorname = vendr_name;
		vnfname_field.vfmodule = new EvelOptionString();
		vnfname_field.vnfname = new EvelOptionString();

	    /***************************************************************************/
	    /* Optional fields.                                                    */
	    /***************************************************************************/
	    additional_info = null;
	    endOfCallVqmSummaries = null;
	    evphoneNumber = new EvelOptionString();

	    EVEL_EXIT();
	  }
	  
	  /**************************************************************************//**
	   * Add an additional value name/value pair to the Voice Quality.
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
	  public void evel_voice_quality_addl_info_add(String name, String value)
		{
		  String[] addl_info = null;
		  EVEL_ENTER();

		  /***************************************************************************/
		  /* Check preconditions.                                                    */
		  /***************************************************************************/
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
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
	   * Set the Callee side codec for Call for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param calleeCodecForCall        The Callee Side Codec to be set.  ASCIIZ 
	   *                                  string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_callee_codec_set(String calleeCodecForCall) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
		  assert(calleeCodecForCall != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  LOGGER.debug("Setting Correlator "+calleeCodecForCall);

	      calleeSideCodec = calleeCodecForCall;

	      EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Caller side codec for Call for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param callerCodecForCall        The Caller Side Codec to be set.  ASCIIZ 
	   *                                  string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_caller_codec_set(String callerCodecForCall) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
	      assert(callerCodecForCall != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  LOGGER.debug("Setting CallerCodecForCall "+callerCodecForCall);

	      callerSideCodec = callerCodecForCall;

	      EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the correlator for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param correlator                The correlator value to be set.  ASCIIZ 
	   *                                  string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_correlator_set(String vCorrelator) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
	      assert(vCorrelator != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  LOGGER.debug("Setting Correlator "+vCorrelator);
		  
	      correlator = vCorrelator;

	      EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the RTCP Call Data for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param rtcpCallData              The RTCP Call Data to be set.  ASCIIZ 
	   *                                  string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_rtcp_data_set(String rtcpCallData) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
	      assert(rtcpCallData != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  LOGGER.debug("Setting RTCP Data "+rtcpCallData);

	      midCallRtcp = rtcpCallData;

	      EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Vendor VNF Name fields for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param modulename                The Vendor, VNF and VfModule names to be set.   
	   *                                  ASCIIZ string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_vnfmodule_name_set(String module_name) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
		  assert(module_name != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  LOGGER.debug("Setting VoiceQuality Module Name "+module_name);
		    
		  vnfname_field.vfmodule.SetValue(module_name);

	      EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Vendor VNF Name fields for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param modulename                The Vendor, VNF and VfModule names to be set.   
	   *                                  ASCIIZ string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_vnfname_set(String vnfname) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
		  assert(vnfname != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  LOGGER.debug("Setting VoiceQuality VNF Name "+vnfname);
		    
		  vnfname_field.vnfname.SetValue(vnfname);
		  
	      EVEL_EXIT();
	  }
	  

	  /**************************************************************************//**
	   * Set the Phone Number associated with the Correlator for domain Voice Quality
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param calleeCodecForCall        The Phone Number to be set.  ASCIIZ 
	   *                                  string. The caller does not need to 
	   *                                  preserve the value once the function
	   *                                  returns.
	   *****************************************************************************/
	  public void evel_voice_quality_phone_number_set(String phoneNumber) {
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check preconditions.                                                    */
	      /***************************************************************************/
	      assert(phoneNumber != null);
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
		  
		  evphoneNumber.SetValuePr(phoneNumber,"Phone_Number");
		  
	      EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an End of Call Voice Quality Metrices

	   * The adjacencyName and endpointDescription is null delimited ASCII string.  
	   * The library takes a copy so the caller does not have to preserve values
	   * after the function returns.
	   *
	   * @param voiceQuality     Pointer to the measurement.
	   * @param adjacencyName                     Adjacency name
	   * @param endpointDescription               Enumeration: ‘Caller’, ‘Callee’.
	   * @param endpointJitter                    Endpoint jitter
	   * @param endpointRtpOctetsDiscarded        Endpoint RTP octets discarded.
	   * @param endpointRtpOctetsReceived         Endpoint RTP octets received.
	   * @param endpointRtpOctetsSent             Endpoint RTP octets sent
	   * @param endpointRtpPacketsDiscarded       Endpoint RTP packets discarded.
	   * @param endpointRtpPacketsReceived        Endpoint RTP packets received.
	   * @param endpointRtpPacketsSent            Endpoint RTP packets sent.
	   * @param localJitter                       Local jitter.
	   * @param localRtpOctetsDiscarded           Local RTP octets discarded.
	   * @param localRtpOctetsReceived            Local RTP octets received.
	   * @param localRtpOctetsSent                Local RTP octets sent.
	   * @param localRtpPacketsDiscarded          Local RTP packets discarded.
	   * @param localRtpPacketsReceived           Local RTP packets received.
	   * @param localRtpPacketsSent               Local RTP packets sent.
	   * @param mosCqe                            Decimal range from 1 to 5
	   *                                          (1 decimal place)
	   * @param packetsLost                       No  Packets lost
	   * @param packetLossPercent                 Calculated percentage packet loss 
	   * @param rFactor                           rFactor from 0 to 100
	   * @param roundTripDelay                    Round trip delay in milliseconds
	   *****************************************************************************/
	  public void evel_voice_quality_end_metrics_set(
	      String adjacencyName, String endpointDescr,
	      double endpointJitter,
	      double endpointRtpOctetsDiscarded,
	      double endpointRtpOctetsReceived,
	      double endpointRtpOctetsSent,
	      double endpointRtpPacketsDiscarded,
	      double endpointRtpPacketsReceived,
	      double endpointRtpPacketsSent,
	      double localJitter,
	      double localRtpOctetsDiscarded,
	      double localRtpOctetsReceived,
	      double localRtpOctetsSent,
	      double localRtpPacketsDiscarded,
	      double localRtpPacketsReceived,
	      double localRtpPacketsSent,
	      double mosCqe,
	      double packetsLost,
	      double packetLossPercent,
	      double rFactor,
	      double roundTripDelay) {
	      
	      END_OF_CALL_VOICE_QUALITY_METRICS vQMetrices = null;
	      EVEL_ENTER();

	      /***************************************************************************/
	      /* Check assumptions.                                                      */
	      /***************************************************************************/
	      assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
	      assert(adjacencyName != null);
	      assert(mosCqe >= 1 && mosCqe <= 5);
	      assert(rFactor >= 0 && rFactor <= 100);
	      assert(endpointDescr != null && (endpointDescr.equals("Caller")||endpointDescr.equals("Callee")) );
	      
	      /***************************************************************************/
	      /* Allocate a container for the value and push onto the list.              */
	      /***************************************************************************/
	      LOGGER.debug(MessageFormat.format("Adding adjacencyName={0} endpointDescription={1}", adjacencyName, endpointDescr));
	      vQMetrices = new END_OF_CALL_VOICE_QUALITY_METRICS();
	      assert(vQMetrices != null);

	      vQMetrices.adjacencyName = adjacencyName;
	      vQMetrices.endpointDescription = endpointDescr;
	      
	      vQMetrices.endpointJitter = new EvelOptionDouble();
	      vQMetrices.endpointRtpOctetsDiscarded= new EvelOptionDouble();
	      vQMetrices.endpointRtpOctetsReceived= new EvelOptionDouble();
	      vQMetrices.endpointRtpOctetsSent= new EvelOptionDouble();
	      vQMetrices.endpointRtpPacketsDiscarded= new EvelOptionDouble();
	      vQMetrices.endpointRtpPacketsReceived= new EvelOptionDouble();
	      vQMetrices.endpointRtpPacketsSent= new EvelOptionDouble();
	      vQMetrices.localJitter= new EvelOptionDouble();
	      vQMetrices.localRtpOctetsDiscarded= new EvelOptionDouble();
	      vQMetrices.localRtpOctetsReceived= new EvelOptionDouble();
	      vQMetrices.localRtpOctetsSent= new EvelOptionDouble();
	      vQMetrices.localRtpPacketsDiscarded= new EvelOptionDouble();
	      vQMetrices.localRtpPacketsReceived= new EvelOptionDouble();
	      vQMetrices.localRtpPacketsSent= new EvelOptionDouble();
	      vQMetrices.mosCqe= new EvelOptionDouble();
	      vQMetrices.packetsLost= new EvelOptionDouble();
	      vQMetrices.packetLossPercent= new EvelOptionDouble();
	      vQMetrices.rFactor= new EvelOptionDouble();
	      vQMetrices.roundTripDelay= new EvelOptionDouble();

	      vQMetrices.endpointJitter.SetValuePr(endpointJitter, "Endpoint jitter");
	      vQMetrices.endpointRtpOctetsDiscarded.SetValuePr(endpointRtpOctetsDiscarded, "Endpoint RTP octets discarded");
	      vQMetrices.endpointRtpOctetsReceived.SetValuePr(endpointRtpOctetsReceived, "Endpoint RTP octets received");
	      vQMetrices.endpointRtpOctetsSent.SetValuePr(endpointRtpOctetsSent, "Endpoint RTP octets sent");
	      vQMetrices.endpointRtpPacketsDiscarded.SetValuePr(endpointRtpPacketsDiscarded, "Endpoint RTP packets discarded");
	      vQMetrices.endpointRtpPacketsReceived.SetValuePr(endpointRtpPacketsReceived, "Endpoint RTP packets received");
	      vQMetrices.endpointRtpPacketsSent.SetValuePr(endpointRtpPacketsSent, "Endpoint RTP packets sent");
	      vQMetrices.localJitter.SetValuePr( localJitter, "Local jitter");
	      vQMetrices.localRtpOctetsDiscarded.SetValuePr(localRtpOctetsDiscarded, "Local RTP octets discarded");
	      vQMetrices.localRtpOctetsReceived.SetValuePr(localRtpOctetsReceived, "Local RTP octets received");
	      vQMetrices.localRtpOctetsSent.SetValuePr(localRtpOctetsSent, "Local RTP octets sent");
	      vQMetrices.localRtpPacketsDiscarded.SetValuePr(localRtpPacketsDiscarded, "Local RTP packets discarded");
	      vQMetrices.localRtpPacketsReceived.SetValuePr(localRtpPacketsReceived, "Local RTP packets received");
	      vQMetrices.localRtpPacketsSent.SetValuePr(localRtpPacketsSent, "Local RTP packets sent");
	      vQMetrices.mosCqe.SetValuePr(mosCqe, "Decimal range from 1 to 5 (1 decimal place)");
	      vQMetrices.packetsLost.SetValuePr(packetsLost, "Packets lost");
	      vQMetrices.packetLossPercent.SetValuePr(packetLossPercent, "Calculated percentage packet loss");
	      vQMetrices.rFactor.SetValuePr(rFactor, "rFactor ");
	      vQMetrices.roundTripDelay.SetValuePr(roundTripDelay, "Round trip delay in milliseconds ");

	      endOfCallVqmSummaries = vQMetrices;

	      EVEL_EXIT();
	  }

		/**************************************************************************//**
		 * Encode the Voice Quality in JSON according to AT&T's schema
		 *
		 * @retvalue       JSON object of VoiceQuality body encoding
		 *****************************************************************************/
	  JsonObjectBuilder evelVoiceQualityObject()
	  {

	    double version = major_version+(double)minor_version/10;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);

	    /***************************************************************************/
	    /* Mandatory fields.                                                       */
	    /***************************************************************************/
	    
        JsonObjectBuilder vnfnamedobj =  Json.createObjectBuilder()
                .add( "vendorName",vnfname_field.vendorname);
            vnfname_field.vfmodule.encJsonValue(vnfnamedobj,"vfModuleName");
            vnfname_field.vfmodule.encJsonValue(vnfnamedobj,"vnfName");
	    
	    JsonObjectBuilder evelvq = Json.createObjectBuilder()
	   	                          .add("correlator", correlator)
	   	                          .add("calleeSideCodec", calleeSideCodec)
	   	                          .add("callerSideCodec", callerSideCodec)
                                  .add("midCallRtcp", midCallRtcp)
                                  .add("voiceQualityFieldsVersion", version)
                                  .add("vendorVnfNameFields", vnfnamedobj);
	    
	    /***************************************************************************/
	    /* Optional fields.                                                        */
	    /***************************************************************************/
	    evphoneNumber.encJsonValue(evelvq, "phoneNumber");
	    
	    
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
			evelvq.add("additionalFields", builder);
		  }
		  
		  
		  if( endOfCallVqmSummaries != null )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();

			  END_OF_CALL_VOICE_QUALITY_METRICS vQMetrics = endOfCallVqmSummaries;
			  
			  JsonObjectBuilder obj = Json.createObjectBuilder()
					  .add("adjacencyName", vQMetrics.adjacencyName)
					  .add("endpointDescription", vQMetrics.endpointDescription);
					  
			  vQMetrics.endpointJitter.encJsonValue(obj,"endpointJitter");
			  vQMetrics.endpointRtpOctetsDiscarded.encJsonValue(obj,"endpointRtpOctetsDiscarded");
			  vQMetrics.endpointRtpOctetsReceived.encJsonValue(obj,"endpointRtpOctetsReceived");
			  vQMetrics.endpointRtpOctetsSent.encJsonValue(obj,"endpointRtpOctetsSent");
			  vQMetrics.endpointRtpPacketsDiscarded.encJsonValue(obj,"endpointRtpPacketsDiscarded");			  
			  vQMetrics.endpointRtpPacketsReceived.encJsonValue(obj,"endpointRtpPacketsReceived");
			  vQMetrics.endpointRtpPacketsSent.encJsonValue(obj,"endpointRtpPacketsSent");
			  vQMetrics.localJitter.encJsonValue(obj,"localJitter");
			  vQMetrics.localRtpOctetsDiscarded.encJsonValue(obj,"localRtpOctetsDiscarded");
			  vQMetrics.localRtpOctetsReceived.encJsonValue(obj,"localRtpOctetsReceived");
			  vQMetrics.localRtpOctetsSent.encJsonValue(obj,"localRtpOctetsSent");
			  vQMetrics.localRtpPacketsDiscarded.encJsonValue(obj,"localRtpPacketsDiscarded");			  
			  vQMetrics.localRtpPacketsReceived.encJsonValue(obj,"localRtpPacketsReceived");
			  vQMetrics.localRtpPacketsSent.encJsonValue(obj,"localRtpPacketsSent");
			  vQMetrics.mosCqe.encJsonValue(obj,"mosCqe");
			  vQMetrics.packetsLost.encJsonValue(obj,"packetsLost");
			  vQMetrics.packetLossPercent.encJsonValue(obj,"packetLossPercent");
			  vQMetrics.rFactor.encJsonValue(obj,"rFactor");
			  vQMetrics.roundTripDelay.encJsonValue(obj,"roundTripDelay");

			  evelvq.add("endOfCallVqmSummaries", obj);
		  }
	    

	    /***************************************************************************/
	    /* Although optional, we always generate the version.  Note that this      */
	    /* closes the object, too.                                                 */
	    /***************************************************************************/

	    EVEL_EXIT();
	    
	    return evelvq;
	  }
	  
		/**************************************************************************//**
	     * Encode the event as a JSON event object according to AT&T's schema.
	     * retval : String of JSON Voice Quality event message
	     *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_VOICE_QUALITY);
	        
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "voiceQualityFields",evelVoiceQualityObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }


}
