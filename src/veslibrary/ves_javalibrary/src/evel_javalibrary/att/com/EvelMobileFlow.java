package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Mobile Flow class
 *
 * This file implements the Evel Mobile Flow Event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send Mobile flow events.
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
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.log4j.Logger;



public class EvelMobileFlow extends EvelHeader {
	
	int major_version = 1;
	int minor_version = 2;
	
	static int EVEL_TOS_SUPPORTED    =  256;
	/**************************************************************************//**
	 * TCP flags.
	 * JSON equivalent fields: tcpFlagCountList, tcpFlagList
	 *****************************************************************************/
	public enum EVEL_TCP_FLAGS {
	  EVEL_TCP_NS,
	  EVEL_TCP_CWR,
	  EVEL_TCP_ECE,
	  EVEL_TCP_URG,
	  EVEL_TCP_ACK,
	  EVEL_TCP_PSH,
	  EVEL_TCP_RST,
	  EVEL_TCP_SYN,
	  EVEL_TCP_FIN,
	  EVEL_MAX_TCP_FLAGS
	}
	static int EVEL_MAX_TCP_FLAGS    =  10;
	/**************************************************************************//**
	 * Mobile QCI Classes of Service.
	 * JSON equivalent fields: mobileQciCosCountList, mobileQciCosList
	 *****************************************************************************/
	public enum EVEL_QCI_COS_TYPES {

	  /***************************************************************************/
	  /* UMTS Classes of Service.                                                */
	  /***************************************************************************/
	  EVEL_QCI_COS_UMTS_CONVERSATIONAL,
	  EVEL_QCI_COS_UMTS_STREAMING,
	  EVEL_QCI_COS_UMTS_INTERACTIVE,
	  EVEL_QCI_COS_UMTS_BACKGROUND,

	  /***************************************************************************/
	  /* LTE Classes of Service.                                                 */
	  /***************************************************************************/
	  EVEL_QCI_COS_LTE_1,
	  EVEL_QCI_COS_LTE_2,
	  EVEL_QCI_COS_LTE_3,
	  EVEL_QCI_COS_LTE_4,
	  EVEL_QCI_COS_LTE_65,
	  EVEL_QCI_COS_LTE_66,
	  EVEL_QCI_COS_LTE_5,
	  EVEL_QCI_COS_LTE_6,
	  EVEL_QCI_COS_LTE_7,
	  EVEL_QCI_COS_LTE_8,
	  EVEL_QCI_COS_LTE_9,
	  EVEL_QCI_COS_LTE_69,
	  EVEL_QCI_COS_LTE_70,
	  EVEL_MAX_QCI_COS_TYPES
	}
	static int EVEL_MAX_QCI_COS_TYPES = 18;
	
	  private static final Logger LOGGER = Logger.getLogger( EvelMobileFlow.class.getName() );

	  
	  /*****************************************************************************/
	  /* Array of strings to use when encoding TCP flags.                          */
	  /*****************************************************************************/
	  static final String[/*EVEL_MAX_TCP_FLAGS*/] evel_tcp_flag_strings = {
	    "NS",
	    "CWR",
	    "ECE",
	    "URG",
	    "ACK",
	    "PSH",
	    "RST",
	    "SYN",
	    "FIN"
	  };

	  /*****************************************************************************/
	  /* Array of strings to use when encoding QCI COS.                            */
	  /*****************************************************************************/
	  static final String[/*EVEL_MAX_QCI_COS_TYPES*/] evel_qci_cos_strings = {
	    "conversational",
	    "streaming",
	    "interactive",
	    "background",
	    "1",
	    "2",
	    "3",
	    "4",
	    "65",
	    "66",
	    "5",
	    "6",
	    "7",
	    "8",
	    "9",
	    "69",
	    "70"
	  };

	
	/**************************************************************************//**
	 * Vendor VNF Name fields.
	 * JSON equivalent field: vendorVnfNameFields
	 *****************************************************************************/
	/**************************************************************************//**
	 * Mobile GTP Per Flow Metrics.
	 * JSON equivalent field: gtpPerFlowMetrics
	 *****************************************************************************/
	public class MOBILE_GTP_PER_FLOW_METRICS {
		  double avg_bit_error_rate;
		  double avg_packet_delay_variation;
		  int avg_packet_latency;
		  int avg_receive_throughput;
		  int avg_transmit_throughput;
		  
		  int flow_activation_epoch;
		  int flow_activation_microsec;
		  
		  int flow_deactivation_epoch;
		  int flow_deactivation_microsec;
		  Date flow_deactivation_time;
		  String flow_status;
		  int max_packet_delay_variation;
		  int num_activation_failures;
		  int num_bit_errors;
		  int num_bytes_received;
		  int num_bytes_transmitted;
		  int num_dropped_packets;
		  int num_l7_bytes_received;
		  int num_l7_bytes_transmitted;
		  int num_lost_packets;
		  int num_out_of_order_packets;
		  int num_packet_errors;
		  int num_packets_received_excl_retrans;
		  int num_packets_received_incl_retrans;
		  int num_packets_transmitted_incl_retrans;
		  int num_retries;
		  int num_timeouts;
		  int num_tunneled_l7_bytes_received;
		  int round_trip_time;
		  int time_to_first_byte;

		  /***************************************************************************/
		  /* Optional fields                                                         */
		  /***************************************************************************/
		  EvelOptionInt ip_tos_counts[/*EVEL_TOS_SUPPORTED*/];
		  EvelOptionInt tcp_flag_counts[/*EVEL_MAX_TCP_FLAGS*/];
		  EvelOptionInt qci_cos_counts[/*EVEL_MAX_QCI_COS_TYPES*/];
		  
		  EvelOptionInt dur_connection_failed_status;
		  EvelOptionInt dur_tunnel_failed_status;
		  EvelOptionString flow_activated_by;
		  
		  EvelOptionTime flow_activation_time;
		  EvelOptionString flow_deactivated_by;
		  
		  EvelOptionString gtp_connection_status;
		  EvelOptionString gtp_tunnel_status;
		  EvelOptionInt large_packet_rtt;
		  EvelOptionDouble large_packet_threshold;
		  EvelOptionInt max_receive_bit_rate;
		  EvelOptionInt max_transmit_bit_rate;
		  EvelOptionInt num_gtp_echo_failures;
		  EvelOptionInt num_gtp_tunnel_errors;
		  EvelOptionInt num_http_errors;

	  /**************************************************************************//**
	   * Create a new Mobile GTP Per Flow Metrics.
	   *
	   * @note    The mandatory fields on the Mobile GTP Per Flow Metrics must be
	   *          supplied to this factory function and are immutable once set.
	   *          Optional fields have explicit setter functions, but again values
	   *          may only be set once so that the Mobile GTP Per Flow Metrics has
	   *          immutable properties.
	   *
	   * @param   avg_bit_error_rate          Average bit error rate.
	   * @param   avg_packet_delay_variation  Average delay or jitter in ms.
	   * @param   avg_packet_latency          Average delivery latency.
	   * @param   avg_receive_throughput      Average receive throughput.
	   * @param   avg_transmit_throughput     Average transmit throughput.
	   * @param   flow_activation_epoch       Time the connection is activated.
	   * @param   flow_activation_microsec    Microseconds for the start of the flow
	   *                                      connection.
	   * @param   flow_deactivation_epoch     Time for the end of the connection.
	   * @param   flow_deactivation_microsec  Microseconds for the end of the flow
	   *                                      connection.
	   * @param   flow_deactivation_time      Transmission time of the first packet.
	   * @param   flow_status                 Connection status.
	   * @param   max_packet_delay_variation  Maximum packet delay or jitter in ms.
	   * @param   num_activation_failures     Number of failed activation requests.
	   * @param   num_bit_errors              Number of errored bits.
	   * @param   num_bytes_received          Number of bytes received.
	   * @param   num_bytes_transmitted       Number of bytes transmitted.
	   * @param   num_dropped_packets         Number of received packets dropped.
	   * @param   num_l7_bytes_received       Number of tunneled Layer 7 bytes
	   *                                      received.
	   * @param   num_l7_bytes_transmitted    Number of tunneled Layer 7 bytes
	   *                                      transmitted.
	   * @param   num_lost_packets            Number of lost packets.
	   * @param   num_out_of_order_packets    Number of out-of-order packets.
	   * @param   num_packet_errors           Number of errored packets.
	   * @param   num_packets_received_excl_retrans  Number of packets received,
	   *                                             excluding retransmits.
	   * @param   num_packets_received_incl_retrans  Number of packets received.
	   * @param   num_packets_transmitted_incl_retrans  Number of packets
	   *                                                transmitted.
	   * @param   num_retries                 Number of packet retries.
	   * @param   num_timeouts                Number of packet timeouts.
	   * @param   num_tunneled_l7_bytes_received  Number of tunneled Layer 7 bytes
	   *                                          received, excluding retransmits.
	   * @param   round_trip_time             Round trip time.
	   * @param   time_to_first_byte          Time in ms between connection
	   *                                      activation and first byte received.
	   *
	   * @returns pointer to the newly manufactured ::MOBILE_GTP_PER_FLOW_METRICS.
	   *          If the structure is not used it must be released using
	   *          ::evel_free_mobile_gtp_flow_metrics.
	   * @retval  null  Failed to create the event.
	   *****************************************************************************/
	  public MOBILE_GTP_PER_FLOW_METRICS(
	                                        double tavg_bit_error_rate,
	                                        double tavg_packet_delay_variation,
	                                        int tavg_packet_latency,
	                                        int tavg_receive_throughput,
	                                        int tavg_transmit_throughput,
	                                        int tflow_activation_epoch,
	                                        int tflow_activation_microsec,
	                                        int tflow_deactivation_epoch,
	                                        int tflow_deactivation_microsec,
	                                        Date tflow_deactivation_time,
	                                        String tflow_status,
	                                        int tmax_packet_delay_variation,
	                                        int tnum_activation_failures,
	                                        int tnum_bit_errors,
	                                        int tnum_bytes_received,
	                                        int tnum_bytes_transmitted,
	                                        int tnum_dropped_packets,
	                                        int tnum_l7_bytes_received,
	                                        int tnum_l7_bytes_transmitted,
	                                        int tnum_lost_packets,
	                                        int tnum_out_of_order_packets,
	                                        int tnum_packet_errors,
	                                        int tnum_packets_received_excl_retrans,
	                                        int tnum_packets_received_incl_retrans,
	                                        int tnum_packets_transmitted_incl_retrans,
	                                        int tnum_retries,
	                                        int tnum_timeouts,
	                                        int tnum_tunneled_l7_bytes_received,
	                                        int tround_trip_time,
	                                        int ttime_to_first_byte)
	  {
	    int ii;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tavg_bit_error_rate >= 0.0);
	    assert(tavg_packet_delay_variation >= 0.0);
	    assert(tavg_packet_latency >= 0);
	    assert(tavg_receive_throughput >= 0);
	    assert(tavg_transmit_throughput >= 0);
	    assert(tflow_activation_epoch > 0);
	    assert(tflow_activation_microsec >= 0);
	    assert(tflow_deactivation_epoch > 0);
	    assert(tflow_deactivation_microsec >= 0);
	    assert(tflow_status != null);
	    assert(tmax_packet_delay_variation >= 0);
	    assert(tnum_activation_failures >= 0);
	    assert(tnum_bit_errors >= 0);
	    assert(tnum_bytes_received >= 0);
	    assert(tnum_bytes_transmitted >= 0);
	    assert(tnum_dropped_packets >= 0);
	    assert(tnum_l7_bytes_received >= 0);
	    assert(tnum_l7_bytes_transmitted >= 0);
	    assert(tnum_lost_packets >= 0);
	    assert(tnum_out_of_order_packets >= 0);
	    assert(tnum_packet_errors >= 0);
	    assert(tnum_packets_received_excl_retrans >= 0);
	    assert(tnum_packets_received_incl_retrans >= 0);
	    assert(tnum_packets_transmitted_incl_retrans >= 0);
	    assert(tnum_retries >= 0);
	    assert(tnum_timeouts >= 0);
	    assert(tnum_tunneled_l7_bytes_received >= 0);
	    assert(tround_trip_time >= 0);
	    assert(ttime_to_first_byte >= 0);

	    /***************************************************************************/
	    /* Allocate the Mobile Flow GTP Per Flow Metrics.                          */
	    /***************************************************************************/
	    LOGGER.debug("New Mobile Flow GTP Per Flow Metrics");

	    /***************************************************************************/
	    /* Initialize the Mobile Flow GTP Per Flow Metrics fields.  Optional       */
	    /* string values are uninitialized (null).                                 */
	    /***************************************************************************/
	    avg_bit_error_rate = tavg_bit_error_rate;
	    avg_packet_delay_variation = tavg_packet_delay_variation;
	    avg_packet_latency = tavg_packet_latency;
	    avg_receive_throughput = tavg_receive_throughput;
	    avg_transmit_throughput = tavg_transmit_throughput;
	    flow_activation_epoch = tflow_activation_epoch;
	    flow_activation_microsec = tflow_activation_microsec;
	    flow_deactivation_epoch = tflow_deactivation_epoch;
	    flow_deactivation_microsec = tflow_deactivation_microsec;
	    flow_deactivation_time = tflow_deactivation_time;
	    flow_status = tflow_status;
	    max_packet_delay_variation = tmax_packet_delay_variation;
	    num_activation_failures = tnum_activation_failures;
	    num_bit_errors = tnum_bit_errors;
	    num_bytes_received = tnum_bytes_received;
	    num_bytes_transmitted = tnum_bytes_transmitted;
	    num_dropped_packets = tnum_dropped_packets;
	    num_l7_bytes_received = tnum_l7_bytes_received;
	    num_l7_bytes_transmitted = tnum_l7_bytes_transmitted;
	    num_lost_packets = tnum_lost_packets;
	    num_out_of_order_packets = tnum_out_of_order_packets;
	    num_packet_errors = tnum_packet_errors;
	    num_packets_received_excl_retrans =
	                                               tnum_packets_received_excl_retrans;
	    num_packets_received_incl_retrans =
	                                               tnum_packets_received_incl_retrans;
	    num_packets_transmitted_incl_retrans =
	                                            tnum_packets_transmitted_incl_retrans;
	    num_retries = tnum_retries;
	    num_timeouts = tnum_timeouts;
	    num_tunneled_l7_bytes_received = tnum_tunneled_l7_bytes_received;
	    round_trip_time = tround_trip_time;
	    time_to_first_byte = ttime_to_first_byte;
	    ip_tos_counts = new EvelOptionInt[EVEL_TOS_SUPPORTED];
	    for (ii = 0; ii < EVEL_TOS_SUPPORTED; ii++)
	    {
	      ip_tos_counts[ii] = new EvelOptionInt();
	    }
	    tcp_flag_counts = new EvelOptionInt[EVEL_MAX_TCP_FLAGS];
	    for (ii = 0; ii < EVEL_MAX_TCP_FLAGS; ii++)
	    {
	      tcp_flag_counts[ii] = new EvelOptionInt();
	    }
	    qci_cos_counts = new EvelOptionInt[EVEL_MAX_QCI_COS_TYPES];
	    for (ii = 0; ii < EVEL_MAX_QCI_COS_TYPES; ii++)
	    {
	      qci_cos_counts[ii] = new EvelOptionInt();
	    }
	    dur_connection_failed_status = new EvelOptionInt();
	    dur_tunnel_failed_status = new EvelOptionInt();
	    flow_activated_by = new EvelOptionString();
	    flow_activation_time = new EvelOptionTime();
	    flow_deactivated_by = new EvelOptionString();
	    gtp_connection_status = new EvelOptionString();
	    gtp_tunnel_status = new EvelOptionString();
	    large_packet_rtt = new EvelOptionInt();
	    large_packet_threshold = new EvelOptionDouble();
	    max_receive_bit_rate = new EvelOptionInt();
	    max_transmit_bit_rate = new EvelOptionInt();
	    num_gtp_echo_failures = new EvelOptionInt();
	    num_gtp_tunnel_errors = new EvelOptionInt();
	    num_http_errors = new EvelOptionInt();

	    EVEL_EXIT();
	  }
	}
	
	/***************************************************************************/
	/* Mandatory fields                                                        */
	/***************************************************************************/	  
	  String flow_direction;
	  public MOBILE_GTP_PER_FLOW_METRICS gtp_per_flow_metrics;
	  String ip_protocol_type;
	  String ip_version;
	  String other_endpoint_ip_address;
	  int other_endpoint_port;
	  String reporting_endpoint_ip_addr;
	  int reporting_endpoint_port;
	  	  
	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  ArrayList<String[]> additional_info;
	  EvelOptionString application_type;
	  EvelOptionString app_protocol_type;
	  EvelOptionString app_protocol_version;
	  EvelOptionString cid;
	  EvelOptionString connection_type;
	  EvelOptionString ecgi;
	  EvelOptionString gtp_protocol_type;
	  EvelOptionString gtp_version;
	  EvelOptionString http_header;
	  EvelOptionString imei;
	  EvelOptionString imsi;
	  EvelOptionString lac;
	  EvelOptionString mcc;
	  EvelOptionString mnc;
	  EvelOptionString msisdn;
	  EvelOptionString other_functional_role;
	  EvelOptionString rac;
	  EvelOptionString radio_access_technology;
	  EvelOptionString sac;
	  EvelOptionInt    sampling_algorithm;
	  EvelOptionString tac;
	  EvelOptionString tunnel_id;
	  EvelOptionString vlan_id;

	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/


	  /*****************************************************************************/
	  /* Local prototypes                                                          */
	  /*****************************************************************************/
	  
	 
	  /**************************************************************************//**
	   * Create a new Mobile Flow event.
	   *
	   * @note    The mandatory fields on the Mobile Flow must be supplied to this
	   *          factory function and are immutable once set.  Optional fields have
	   *          explicit setter functions, but again values may only be set once so
	   *          that the Mobile Flow has immutable properties.
	   * @param   flow_direction              Flow direction.
	   * @param   gtp_per_flow_metrics        GTP per-flow metrics.
	   * @param   ip_protocol_type            IP protocol type.
	   * @param   ip_version                  IP protocol version.
	   * @param   other_endpoint_ip_address   IP address of the other endpoint.
	   * @param   other_endpoint_port         IP port of the other endpoint.
	   * @param   reporting_endpoint_ip_addr  IP address of the reporting endpoint.
	   
	   * @param   reporting_endpoint_port     IP port of the reporting endpoint.
	   *****************************************************************************/
	  public EvelMobileFlow(      String evname, String evid,
	                              String flow_dir,
	                              MOBILE_GTP_PER_FLOW_METRICS gtp_per_flow_metr,
	                              String ip_protocol_typ,
	                              String ip_vers,
	                              String other_endpoint_ip_addr,
	                              int other_endpoint_pt,
	                              String reporting_endpoint_ipaddr,
	                              int reporting_endpoint_pt)
	  {
	    super(evname,evid);

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(flow_dir != null);
	    assert(gtp_per_flow_metr != null);
	    assert(ip_protocol_typ != null);
	    assert(ip_vers != null);
	    assert(other_endpoint_ip_addr != null);
	    assert(other_endpoint_pt > 0);
	    assert(reporting_endpoint_ipaddr != null);
	    assert(reporting_endpoint_pt > 0);

	    /***************************************************************************/
	    /* Allocate the Mobile Flow.                                               */
	    /***************************************************************************/
	    LOGGER.debug("New Mobile Flow created");

	    /***************************************************************************/
	    /* Initialize the header & the Mobile Flow fields.  Optional string values */
	    /* are uninitialized (null).                                               */
	    /***************************************************************************/
	    event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW;
	    flow_direction = flow_dir;
	    gtp_per_flow_metrics = gtp_per_flow_metr;
	    ip_protocol_type = ip_protocol_typ;
	    ip_version = ip_vers;
	    other_endpoint_ip_address = other_endpoint_ip_addr;
	    other_endpoint_port = other_endpoint_pt;
	    reporting_endpoint_ip_addr = reporting_endpoint_ipaddr;
	    reporting_endpoint_port = reporting_endpoint_pt;
	    
	    application_type = new EvelOptionString();
	    app_protocol_type = new EvelOptionString();
	    app_protocol_version = new EvelOptionString();
	    cid = new EvelOptionString();
	    connection_type = new EvelOptionString();
	    ecgi = new EvelOptionString();
	    gtp_protocol_type = new EvelOptionString();
	    gtp_version = new EvelOptionString();
	    http_header = new EvelOptionString();
	    imei = new EvelOptionString();
	    imsi = new EvelOptionString();
	    lac = new EvelOptionString();
	    mcc = new EvelOptionString();
	    mnc = new EvelOptionString();
	    msisdn = new EvelOptionString();
	    other_functional_role = new EvelOptionString();
	    rac = new EvelOptionString();
	    radio_access_technology = new EvelOptionString();
	    sac = new EvelOptionString();
	    sampling_algorithm = new EvelOptionInt();
	    tac = new EvelOptionString();
	    tunnel_id = new EvelOptionString();
	    vlan_id = new EvelOptionString();
	    additional_info = null;

	    EVEL_EXIT();

	  }



	/**************************************************************************//**
	   * Add an additional value name/value pair to the Mobile flow.
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
	  public void evel_mobile_flow_addl_field_add(String name, String value)
		{
		  String[] addl_info = null;
		  EVEL_ENTER();

		  /***************************************************************************/
		  /* Check preconditions.                                                    */
		  /***************************************************************************/
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
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
	   * Set the Event Type property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param type        The Event Type to be set. ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_type_set(String typ)
	  {
		    EVEL_ENTER();
		    assert(typ != null);

		    /***************************************************************************/
		    /* Check preconditions and call evel_header_type_set.                      */
		    /***************************************************************************/
		    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
		    evel_header_type_set(typ);

		    EVEL_EXIT();
	  }
	  
	  /**************************************************************************//**
	   * Set the Application Type property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param type        The Application Type to be set. ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_app_type_set(String type)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(type != null);

	    application_type.SetValuePr(
	                           type,
	                           "Application Type");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Application Protocol Type property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param type        The Application Protocol Type to be set. ASCIIZ string.
	   *                    The caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_app_prot_type_set(String type)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(type != null);

	    app_protocol_type.SetValuePr(
	                           type,
	                           "Application Protocol Type");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Application Protocol Version property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param version     The Application Protocol Version to be set. ASCIIZ
	   *                    string.  The caller does not need to preserve the value
	   *                    once the function returns.
	   *****************************************************************************/
	  void evel_mobile_flow_app_prot_ver_set(String version)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(version != null);

	    app_protocol_version.SetValuePr(
	                           version,
	                           "Application Protocol Version");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the CID property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cid         The CID to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_cid_set(String cd)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(cid != null);

	    cid.SetValuePr(
	                           cd,
	                           "CID");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Connection Type property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param type        The Connection Type to be set. ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_con_type_set(String type)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(type != null);

	    connection_type.SetValuePr(
	                           type,
	                           "Connection Type");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the ECGI property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param ecgi        The ECGI to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_ecgi_set(String ecgit)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(ecgit != null);

	    ecgi.SetValuePr(
	                           ecgit,
	                           "ECGI");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the GTP Protocol Type property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param type        The GTP Protocol Type to be set. ASCIIZ string.  The
	   *                    caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_gtp_prot_type_set(String type)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(type != null);

	    gtp_protocol_type.SetValuePr(
	                           type,
	                           "GTP Protocol Type");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the GTP Protocol Version property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param version     The GTP Protocol Version to be set. ASCIIZ string.  The
	   *                    caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_gtp_prot_ver_set(String version)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(version != null);

	    gtp_version.SetValuePr(
	                           version,
	                           "GTP Protocol Version");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the HTTP Header property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param header      The HTTP header to be set. ASCIIZ string. The caller does
	   *                    not need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_http_header_set(String header)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(header != null);

	    http_header.SetValuePr(
	                           header,
	                           "HTTP Header");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the IMEI property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param imei        The IMEI to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_imei_set(String imeit)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(imeit != null);

	    imei.SetValuePr(
	                           imeit,
	                           "IMEI");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the IMSI property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param imsi        The IMSI to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_imsi_set(String imsit)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(imsit != null);

	    imsi.SetValuePr(
	                           imsit,
	                           "IMSI");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the LAC property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param lac         The LAC to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_lac_set(String lact)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/

	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    assert(lact != null);

	    lac.SetValuePr(
	                           lact,
	                           "LAC");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the MCC property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param mcc         The MCC to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_mcc_set(String mcct)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(mcct != null);

	    mcc.SetValuePr(
	                           mcct,
	                           "MCC");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the MNC property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param mnc         The MNC to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_mnc_set(String mnct)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(mnct != null);

	    mnc.SetValuePr(
	                           mnct,
	                           "MNC");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the MSISDN property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param msisdn      The MSISDN to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_msisdn_set(String msisdnt)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(msisdnt != null);

	    msisdn.SetValuePr(
	                           msisdnt,
	                           "MSISDN");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Other Functional Role property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param role        The Other Functional Role to be set. ASCIIZ string. The
	   *                    caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_other_func_role_set(String role)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(role != null);

	    other_functional_role.SetValuePr(
	                           role,
	                           "Other Functional Role");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the RAC property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param rac         The RAC to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_rac_set(String ract)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(ract != null);

	    rac.SetValuePr(
	                           ract,
	                           "RAC");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Radio Access Technology property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param tech        The Radio Access Technology to be set. ASCIIZ string. The
	   *                    caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_radio_acc_tech_set(String tech)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(tech != null);

	    radio_access_technology.SetValuePr(
	                           tech,
	                           "Radio Access Technology");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the SAC property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param sac         The SAC to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_sac_set(String sact)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(sact != null);

	    sac.SetValuePr(
	                           sact,
	                           "SAC");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Sampling Algorithm property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param algorithm   The Sampling Algorithm to be set.
	   *****************************************************************************/
	  public void evel_mobile_flow_samp_alg_set(
	                                     int algorithm)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(algorithm >= 0);

	    sampling_algorithm.SetValuePr(
	                        algorithm,
	                        "Sampling Algorithm");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the TAC property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param tac         The TAC to be set. ASCIIZ string.  The caller does not
	   *                    need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_tac_set(String tact)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(tact != null);

	    tac.SetValuePr(
	                           tact,
	                           "TAC");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Tunnel ID property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param tunnel_id   The Tunnel ID to be set. ASCIIZ string.  The caller does
	   *                    not need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_tunnel_id_set(String tunnel_idt)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(tunnel_idt != null);

	    tunnel_id.SetValuePr(
	                           tunnel_idt,
	                           "Tunnel ID");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the VLAN ID property of the Mobile Flow.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *

	   * @param vlan_id     The VLAN ID to be set. ASCIIZ string.  The caller does
	   *                    not need to preserve the value once the function returns.
	   *****************************************************************************/
	  public void evel_mobile_flow_vlan_id_set(String vlan_idt)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);
	    
	    assert(vlan_idt != null);

	    vlan_id.SetValuePr(
	                           vlan_idt,
	                           "VLAN ID");
	    EVEL_EXIT();
	  }



	  /**************************************************************************//**
	   * Set the Duration of Connection Failed Status property of the Mobile GTP Per
	   * Flow Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param duration    The Duration of Connection Failed Status to be set.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_dur_con_fail_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                           int duration)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(duration >= 0);

	    metrics.dur_connection_failed_status.SetValuePr(
	                        duration,
	                        "Duration of Connection Failed Status");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Duration of Tunnel Failed Status property of the Mobile GTP Per Flow
	   * Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param duration    The Duration of Tunnel Failed Status to be set.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_dur_tun_fail_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                           int duration)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(duration >= 0);

	    metrics.dur_tunnel_failed_status.SetValuePr(
	                        duration,
	                        "Duration of Tunnel Failed Status");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Activated By property of the Mobile GTP Per Flow metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param act_by      The Activated By to be set.  ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_act_by_set(MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                          String act_by)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(act_by != null);

	    metrics.flow_activated_by.SetValuePr(
	                           act_by,
	                           "Activated By");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Activation Time property of the Mobile GTP Per Flow metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param act_time    The Activation Time to be set.  ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public  void evel_mobile_gtp_metrics_act_time_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           Date act_time)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);

	    metrics.flow_activation_time.SetValuePr(
	                         act_time,
	                         "Activation Time");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Deactivated By property of the Mobile GTP Per Flow metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param deact_by    The Deactivated By to be set.  ASCIIZ string. The caller
	   *                    does not need to preserve the value once the function
	   *                    returns.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_deact_by_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           String deact_by)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(deact_by != null);

	    metrics.flow_deactivated_by.SetValuePr(
	                           deact_by,
	                           "Deactivated By");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the GTP Connection Status property of the Mobile GTP Per Flow metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param status      The GTP Connection Status to be set.  ASCIIZ string. The
	   *                    caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_con_status_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           String status)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(status != null);

	    metrics.gtp_connection_status.SetValuePr(
	                           status,
	                           "GTP Connection Status");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the GTP Tunnel Status property of the Mobile GTP Per Flow metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param status      The GTP Tunnel Status to be set.  ASCIIZ string. The
	   *                    caller does not need to preserve the value once the
	   *                    function returns.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_tun_status_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           String status)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(status != null);

	    metrics.gtp_tunnel_status.SetValuePr(
	                           status,
	                           "GTP Tunnel Status");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set an IP Type-of-Service count property of the Mobile GTP Per Flow metrics.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param index       The index of the IP Type-of-Service.
	   * @param count       The count.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_iptos_set(MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                         int index,
	                                         int count)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(index >= 0);
	    assert(index < EVEL_TOS_SUPPORTED);
	    assert(count >= 0);
	    assert(count <= 255);

	    LOGGER.debug("IP Type-of-Service "+index);
	    metrics.ip_tos_counts[index].SetValuePr(
	                        count,
	                        "IP Type-of-Service");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Large Packet Round-Trip Time property of the Mobile GTP Per Flow
	   * Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param rtt         The Large Packet Round-Trip Time to be set.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_large_pkt_rtt_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           int rtt)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(rtt >= 0);

	    metrics.large_packet_rtt.SetValuePr(
	                        rtt,
	                        "Large Packet Round-Trip Time");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Large Packet Threshold property of the Mobile GTP Per Flow Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param threshold   The Large Packet Threshold to be set.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_large_pkt_thresh_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                           double threshold)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(threshold >= 0.0);

	    metrics.large_packet_threshold.SetValuePr(
	                           threshold,
	                           "Large Packet Threshold");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Max Receive Bit Rate property of the Mobile GTP Per Flow Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param rate        The Max Receive Bit Rate to be set.
	   *****************************************************************************/
	  public void evel_mobile_gtp_metrics_max_rcv_bit_rate_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                           int rate)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(rate >= 0);

	    metrics.max_receive_bit_rate.SetValuePr(
	                        rate,
	                        "Max Receive Bit Rate");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Max Transmit Bit Rate property of the Mobile GTP Per Flow Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param rate        The Max Transmit Bit Rate to be set.
	   *****************************************************************************/
	  public  void evel_mobile_gtp_metrics_max_trx_bit_rate_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                           int rate)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(rate >= 0);

	    metrics.max_transmit_bit_rate.SetValuePr(
	                        rate,
	                        "Max Transmit Bit Rate");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Number of GTP Echo Failures property of the Mobile GTP Per Flow
	   * Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param num         The Number of GTP Echo Failures to be set.
	   *****************************************************************************/
	  public  void evel_mobile_gtp_metrics_num_echo_fail_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS metrics,
	                                           int num)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(num >= 0);

	    metrics.num_gtp_echo_failures.SetValuePr(
	                        num,
	                        "Number of GTP Echo Failures");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Number of GTP Tunnel Errors property of the Mobile GTP Per Flow
	   * Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param num         The Number of GTP Tunnel Errors to be set.
	   *****************************************************************************/
	  public   void evel_mobile_gtp_metrics_num_tun_fail_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           int num)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(num >= 0);

	    metrics.num_gtp_tunnel_errors.SetValuePr(
	                        num,
	                        "Number of GTP Tunnel Errors");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Number of HTTP Errors property of the Mobile GTP Per Flow Metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics     Pointer to the Mobile GTP Per Flow Metrics.
	   * @param num         The Number of HTTP Errors to be set.
	   *****************************************************************************/
	  public  void evel_mobile_gtp_metrics_num_http_errors_set(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           int num)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(num >= 0);

	    metrics.num_http_errors.SetValuePr(
	                        num,
	                        "Number of HTTP Errors");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add a TCP flag count to the metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics       Pointer to the Mobile GTP Per Flow Metrics.
	   * @param tcp_flag      The TCP flag to be updated.
	   * @param count         The associated flag count, which must be nonzero.
	   *****************************************************************************/
	  public  void evel_mobile_gtp_metrics_tcp_flag_count_add(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           int tcp_flag,
	                                           int count)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(tcp_flag >= 0 && tcp_flag < EVEL_MAX_TCP_FLAGS);
	    assert(count >= 0);

	    LOGGER.debug("TCP Flag: "+tcp_flag);
	    metrics.tcp_flag_counts[tcp_flag].SetValuePr(
	                        count,
	                        "TCP flag");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add a QCI COS count to the metrics.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param metrics       Pointer to the Mobile GTP Per Flow Metrics.
	   * @param qci_cos       The QCI COS count to be updated.
	   * @param count         The associated QCI COS count.
	   *****************************************************************************/
	  public  void evel_mobile_gtp_metrics_qci_cos_count_add(
	                                           MOBILE_GTP_PER_FLOW_METRICS  metrics,
	                                           int qci_cos,
	                                           int count)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(metrics != null);
	    assert(qci_cos >= 0);
	    assert(qci_cos < EVEL_MAX_QCI_COS_TYPES);
	    assert(count >= 0);

	    LOGGER.debug("QCI COS: "+ qci_cos);
	    metrics.qci_cos_counts[qci_cos].SetValuePr(
	                        count,
	                        "QCI COS");
	    EVEL_EXIT();
	  }


		/**************************************************************************//**
		 * Encode the GTP Per Flow Object in JSON according to AT&T's schema.
		 *
		 * @retval JsonObjectBuilder of GTP Flow body portion of message   
		 *****************************************************************************/
	  JsonObjectBuilder evelGtpPerFlowObject()
	  {
		    int index;
		    boolean found_ip_tos;
		    boolean found_tcp_flag;
		    boolean found_qci_cos;

		    EVEL_ENTER();
		    
    	    /***************************************************************************/
    	    /* Mandatory parameters.                                                   */
    	    /***************************************************************************/
	        MOBILE_GTP_PER_FLOW_METRICS metrics = gtp_per_flow_metrics;
		    
	        JsonObjectBuilder obj =  Json.createObjectBuilder()
	        		                  .add("avgBitErrorRate", metrics.avg_bit_error_rate)
	        		                  .add("avgPacketDelayVariation", metrics.avg_packet_delay_variation)
	        		                  .add("avgPacketLatency", metrics.avg_packet_latency)
	        		                  .add("avgReceiveThroughput", metrics.avg_receive_throughput)
	        		                  .add("avgTransmitThroughput", metrics.avg_transmit_throughput)
	        		                  .add("flowActivationEpoch", metrics.flow_activation_epoch)
	        		                  .add("flowActivationMicrosec", metrics.flow_activation_microsec)
	        		                  .add("flowDeactivationEpoch", metrics.flow_deactivation_epoch)
	        		                  .add("flowDeactivationMicrosec", metrics.flow_deactivation_microsec)
	        		                  .add("flowDeactivationTime", metrics.flow_deactivation_time.toString())
	        		                  .add("flowStatus", metrics.flow_status)
	        		                  .add("maxPacketDelayVariation", metrics.max_packet_delay_variation)
	        		                  .add("numActivationFailures", metrics.num_activation_failures)
	    	.add( "numBitErrors", metrics.num_bit_errors)
	        .add( "numBytesReceived", metrics.num_bytes_received)
	        .add( "numBytesTransmitted", metrics.num_bytes_transmitted)
	        .add( "numDroppedPackets", metrics.num_dropped_packets)
	        .add( "numL7BytesReceived", metrics.num_l7_bytes_received)
	        .add( "numL7BytesTransmitted", metrics.num_l7_bytes_transmitted)
	        .add( "numLostPackets", metrics.num_lost_packets)
	        .add( "numOutOfOrderPackets", metrics.num_out_of_order_packets)
	        .add( "numPacketErrors", metrics.num_packet_errors)
	        .add( "numPacketsReceivedExclRetrans",
	    	                    metrics.num_packets_received_excl_retrans)
	        .add(
	    	                    "numPacketsReceivedInclRetrans",
	    	                    metrics.num_packets_received_incl_retrans)
	        .add(
	    	                    "numPacketsTransmittedInclRetrans",
	    	                    metrics.num_packets_transmitted_incl_retrans)
	        .add( "numRetries", metrics.num_retries)
	        .add( "numTimeouts", metrics.num_timeouts)
	        .add(
	    	                    "numTunneledL7BytesReceived",
	    	                    metrics.num_tunneled_l7_bytes_received)
	        .add( "roundTripTime", metrics.round_trip_time)
	        .add( "timeToFirstByte", metrics.time_to_first_byte);

	    	    /***************************************************************************/
	    	    /* Optional parameters.                                                    */
	    	    /***************************************************************************/
	    	    found_ip_tos = false;
	    	    for (index = 0; index < EVEL_TOS_SUPPORTED; index++)
	    	    {
	    	      if (metrics.ip_tos_counts[index].is_set)
	    	      {
	    	        found_ip_tos = true;
	    	        break;
	    	      }
	    	    }

	    	    if (found_ip_tos)
	    	    {
	    	      JsonArrayBuilder builder = Json.createArrayBuilder();
	    	      for (index = 0; index < EVEL_TOS_SUPPORTED; index++)
	    	      {
	    	        if (metrics.ip_tos_counts[index].is_set)
	    	        {
	    			  JsonObjectBuilder obj2 = Json.createObjectBuilder()
	 			    	     .add(Integer.toString(index), metrics.ip_tos_counts[index].value);
	 			      builder.add(obj2);
	    	        }
	    	      }
	    	      obj.add("ipTosCountList", builder);
	    	    }


	    	    /***************************************************************************/
	    	    /* Make some compile-time assertions about EVEL_TCP_FLAGS.  If you update  */
	    	    /* these, make sure you update evel_tcp_flag_strings to match the enum.    */
	    	    /***************************************************************************/

	    	    found_tcp_flag = false;
	    	    for (index = 0; index < EVEL_MAX_TCP_FLAGS; index++)
	    	    {
	    	      if (metrics.tcp_flag_counts[index].is_set)
	    	      {
	    	        found_tcp_flag = true;
	    	        break;
	    	      }
	    	    }

	    	    if (found_tcp_flag)
	    	    {
	    	      JsonArrayBuilder builder = Json.createArrayBuilder();
	    	      for (index = 0; index < EVEL_MAX_TCP_FLAGS; index++)
	    	      {
	    	        if (metrics.tcp_flag_counts[index].is_set)
	    	        {
	    			  JsonObjectBuilder obj2 = Json.createObjectBuilder()
		 			    	     .add(Integer.toString(index), evel_tcp_flag_strings[index]);
		 			  builder.add(obj2);
	    	        }
	    	      }
	    	      obj.add("tcpFlagList", builder);
	    	    }

	    	    if (found_tcp_flag)
	    	    {
	    	      JsonArrayBuilder builder = Json.createArrayBuilder();
	    	      for (index = 0; index < EVEL_MAX_TCP_FLAGS; index++)
	    	      {
	    	        if (metrics.tcp_flag_counts[index].is_set)
	    	        {
		    		   JsonObjectBuilder obj2 = Json.createObjectBuilder()
			 			    	     .add(evel_tcp_flag_strings[index], metrics.tcp_flag_counts[index].value);
			 		   builder.add(obj2);
	    	        }
	    	      }
	    	      obj.add("tcpFlagCountList", builder);
	    	    }

	    	    /***************************************************************************/
	    	    /* Make some compile-time assertions about EVEL_QCI_COS_TYPES.  If you     */
	    	    /* update these, make sure you update evel_qci_cos_strings to match the    */
	    	    /* enum.                                                                   */
	    	    /***************************************************************************/

	    	    found_qci_cos = false;
	    	    for (index = 0; index < EVEL_MAX_QCI_COS_TYPES; index++)
	    	    {
	    	      if (metrics.qci_cos_counts[index].is_set)
	    	      {
	    	        found_qci_cos = true;
	    	        break;
	    	      }
	    	    }

	    	    if (found_qci_cos)
	    	    {
	    	      JsonArrayBuilder builder = Json.createArrayBuilder();
	    	      for (index = 0; index < EVEL_MAX_QCI_COS_TYPES; index++)
	    	      {
	    	        if (metrics.qci_cos_counts[index].is_set)
	    	        {
			    	  JsonObjectBuilder obj2 = Json.createObjectBuilder()
			 			    	     .add(Integer.toString(index), evel_qci_cos_strings[index]);
			 		  builder.add(obj2);
	    	        }
	    	      }
	    	      obj.add("mobileQciCosList", builder);
	    	    }

	    	    if (found_qci_cos)
	    	    {
	    	    	JsonArrayBuilder builder = Json.createArrayBuilder();
	    	      for (index = 0; index < EVEL_MAX_QCI_COS_TYPES; index++)
	    	      {
	    	        if (metrics.qci_cos_counts[index].is_set)
	    	        {
				    	  JsonObjectBuilder obj2 = Json.createObjectBuilder()
			 			    	     .add(evel_qci_cos_strings[index], metrics.qci_cos_counts[index].value);
			 		      builder.add(obj2);
	    	        }
	    	      }
	    	      obj.add("mobileQciCosCountList", builder);
	    	    }

	    	    metrics.dur_connection_failed_status.encJsonValue(obj, "durConnectionFailedStatus");
	    	    metrics.dur_tunnel_failed_status.encJsonValue(obj, "durTunnelFailedStatus");
	    	    metrics.flow_activated_by.encJsonValue(obj, "flowActivatedBy");
	    	    metrics.flow_activation_time.encJsonValue(obj,"flowActivationTime");
	    	    metrics.flow_deactivated_by.encJsonValue(obj, "flowDeactivatedBy");
	    	    metrics.gtp_connection_status.encJsonValue(obj, "gtpConnectionStatus");
	    	    metrics.gtp_tunnel_status.encJsonValue(obj, "gtpTunnelStatus");
	    	    metrics.large_packet_rtt.encJsonValue(obj, "largePacketRtt");
	    	    metrics.large_packet_threshold.encJsonValue(obj, "largePacketThreshold");
	    	    metrics.max_receive_bit_rate.encJsonValue(obj, "maxReceiveBitRate");
	    	    metrics.max_transmit_bit_rate.encJsonValue(obj, "maxTransmitBitRate");
	    	    metrics.num_gtp_echo_failures.encJsonValue(obj, "numGtpEchoFailures");
	    	    metrics.num_gtp_tunnel_errors.encJsonValue(obj, "numGtpTunnelErrors");
	    	    metrics.num_http_errors.encJsonValue(obj, "numHttpErrors");
	    	    
	    	    return obj;       
	            
	  }
	  

	  /**************************************************************************//**
	   * Encode Mobile Flow Object according to VES schema
	   *
	   * @retval JSON Object of Mobile Flow event
	   *****************************************************************************/
	  JsonObjectBuilder evelMobileFlowObject()
	  {

	    double version = major_version+(double)minor_version/10;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MOBILE_FLOW);

	    /***************************************************************************/
	    /* Mandatory fields.                                                       */
	    /***************************************************************************/
	    JsonObjectBuilder evelmf = Json.createObjectBuilder()
	   	                          .add("flowDirection", flow_direction)
	   	                          .add("ipProtocolType", ip_protocol_type)
	   	                          .add("ipVersion", ip_version)
                                  .add("otherEndpointIpAddress", other_endpoint_ip_address)
                                  .add("otherEndpointPort", other_endpoint_port)
	   	                          .add("reportingEndpointIpAddr", reporting_endpoint_ip_addr)
	   	                          .add("reportingEndpointPort", reporting_endpoint_port);
	    
	    //call gtp per flow object encoding function
	    if(gtp_per_flow_metrics != null)
	   	                  evelmf.add("gtpPerFlowMetrics", evelGtpPerFlowObject());
                                  


	    /***************************************************************************/
	    /* Optional fields.                                                        */
	    /***************************************************************************/
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
			evelmf.add("additionalFields", builder);
		  }
		  

		    /***************************************************************************/
		    /* Optional parameters.                                                    */
		    /***************************************************************************/
		    application_type.encJsonValue(evelmf, "applicationType");
		    app_protocol_type.encJsonValue(evelmf, "appProtocolType");
		    app_protocol_version.encJsonValue(evelmf, "appProtocolVersion");
		    cid.encJsonValue(evelmf,"cid");
		    connection_type.encJsonValue(evelmf, "connectionType");
		    ecgi.encJsonValue(evelmf, "ecgi");
		    gtp_protocol_type.encJsonValue(evelmf, "gtpProtocolType");
		    gtp_version.encJsonValue(evelmf, "gtpVersion");
		    http_header.encJsonValue(evelmf, "httpHeader");
		    imei.encJsonValue(evelmf, "imei");
		    imsi.encJsonValue(evelmf, "imsi");
		    lac.encJsonValue(evelmf, "lac");
		    mcc.encJsonValue(evelmf, "mcc");
		    mnc.encJsonValue(evelmf, "mnc");
		    msisdn.encJsonValue(evelmf, "msisdn");
		    other_functional_role.encJsonValue(evelmf,"otherFunctionalRole");
		    rac.encJsonValue(evelmf, "rac");
		    radio_access_technology.encJsonValue(evelmf, "radioAccessTechnology");
		    sac.encJsonValue(evelmf, "sac");
		    sampling_algorithm.encJsonValue(evelmf, "samplingAlgorithm");
		    tac.encJsonValue(evelmf, "tac");
		    tunnel_id.encJsonValue(evelmf,"tunnelId");
		    vlan_id.encJsonValue(evelmf,"vlanId");


	    /***************************************************************************/
	    /* Although optional, we always generate the version.  Note that this      */
	    /* closes the object, too.                                                 */
	    /***************************************************************************/
	    evelmf.add("mobileFlowFieldsVersion", version);

	    EVEL_EXIT();
	    
	    return evelmf;
	  }
	  
	  /**************************************************************************//**
	   * Encode the event as a JSON event object according to AT&T's schema.
	   * retval : String of JSON event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_STATE_CHANGE);
		//encode common event header and mobile flow body    
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "mobileFlowFields",evelMobileFlowObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }


}
