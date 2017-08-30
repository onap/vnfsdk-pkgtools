package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * Evel Scaling Measurement class
 *
  * This file implements the Evel Scaling Measurement Event class which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it to send CPU, Memory, Disk Measurements to Collector.
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


public class EvelScalingMeasurement extends EvelHeader {
	
	int major_version = 2;
	int minor_version = 1;
	
	/**************************************************************************//**
	 * CPU Usage.
	 * JSON equivalent field: cpuUsage
	 *****************************************************************************/
	public class MEASUREMENT_CPU_USE {
	  String id;
	  double usage;
	  public EvelOptionDouble idle;
	  public EvelOptionDouble intrpt;
	  public EvelOptionDouble nice;
	  public EvelOptionDouble softirq;
	  public EvelOptionDouble steal;
	  public EvelOptionDouble sys;
	  public EvelOptionDouble user;
	  public EvelOptionDouble wait;
	}


	/**************************************************************************//**
	 * Disk Usage.
	 * JSON equivalent field: diskUsage
	 *****************************************************************************/
	public class MEASUREMENT_DISK_USE {
	  String id;
	  public EvelOptionDouble iotimeavg;
	  public EvelOptionDouble iotimelast;
	  public EvelOptionDouble iotimemax;
	  public EvelOptionDouble iotimemin;
	  public EvelOptionDouble mergereadavg;
	  public EvelOptionDouble mergereadlast;
	  public EvelOptionDouble mergereadmax;
	  public EvelOptionDouble mergereadmin;
	  public EvelOptionDouble mergewriteavg;
	  public EvelOptionDouble mergewritelast;
	  public EvelOptionDouble mergewritemax;
	  public EvelOptionDouble mergewritemin;
	  public EvelOptionDouble octetsreadavg;
	  public EvelOptionDouble octetsreadlast;
	  public EvelOptionDouble octetsreadmax;
	  public EvelOptionDouble octetsreadmin;
	  public EvelOptionDouble octetswriteavg;
	  public EvelOptionDouble octetswritelast;
	  public EvelOptionDouble octetswritemax;
	  public EvelOptionDouble octetswritemin;
	  public EvelOptionDouble opsreadavg;
	  public EvelOptionDouble opsreadlast;
	  public EvelOptionDouble opsreadmax;
	  public EvelOptionDouble opsreadmin;
	  public EvelOptionDouble opswriteavg;
	  public EvelOptionDouble opswritelast;
	  public EvelOptionDouble opswritemax;
	  public EvelOptionDouble opswritemin;
	  public EvelOptionDouble pendingopsavg;
	  public EvelOptionDouble pendingopslast;
	  public EvelOptionDouble pendingopsmax;
	  public EvelOptionDouble pendingopsmin;
	  public EvelOptionDouble timereadavg;
	  public EvelOptionDouble timereadlast;
	  public EvelOptionDouble timereadmax;
	  public EvelOptionDouble timereadmin;
	  public EvelOptionDouble timewriteavg;
	  public EvelOptionDouble timewritelast;
	  public EvelOptionDouble timewritemax;
	  public EvelOptionDouble timewritemin;

	}

	/**************************************************************************//**
	 * Filesystem Usage.
	 * JSON equivalent field: filesystemUsage
	 *****************************************************************************/
	public class MEASUREMENT_FSYS_USE {
	  String filesystem_name;
	  double block_configured;
	  int block_iops;
	  double block_used;
	  double ephemeral_configured;
	  int ephemeral_iops;
	  double ephemeral_used;
	}

	/**************************************************************************//**
	 * Memory Usage.
	 * JSON equivalent field: memoryUsage
	 *****************************************************************************/
	public class MEASUREMENT_MEM_USE {
	  String id;
	  String vmid;
	  double membuffsz;
	  public EvelOptionDouble memcache;
	  public EvelOptionDouble memconfig;
	  public EvelOptionDouble memfree;
	  public EvelOptionDouble slabrecl;
	  public EvelOptionDouble slabunrecl;
	  public EvelOptionDouble memused;
	}

	/**************************************************************************//**
	 * myerrors.
	 * JSON equivalent field: myerrors
	 *****************************************************************************/
	public class MEASUREMENT_ERRORS {
	  int receive_discards;
	  int receive_myerrors;
	  int transmit_discards;
	  int transmit_myerrors;
	}
	
	/**************************************************************************//**
	 * Latency Bucket.
	 * JSON equivalent field: latencyBucketMeasure
	 *****************************************************************************/
	public class MEASUREMENT_LATENCY_BUCKET {
	  int count;

	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  public EvelOptionDouble high_end;
	  public EvelOptionDouble low_end;

	}

	/**************************************************************************//**
	 * Virtual NIC usage.
	 * JSON equivalent field: vNicUsage
	 *****************************************************************************/
	public class MEASUREMENT_VNIC_PERFORMANCE {
       String vnic_id;
       String valuesaresuspect;
	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  /*Cumulative count of broadcast packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_bcast_packets_acc;
	  /*Count of broadcast packets received within the measurement interval*/
	  public EvelOptionDouble recvd_bcast_packets_delta;
	  /*Cumulative count of discarded packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_discarded_packets_acc;
	  /*Count of discarded packets received within the measurement interval*/
	  public EvelOptionDouble recvd_discarded_packets_delta;
	  /*Cumulative count of error packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_error_packets_acc;
	  /*Count of error packets received within the measurement interval*/
	  public EvelOptionDouble recvd_error_packets_delta;
	  /*Cumulative count of multicast packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_mcast_packets_acc;
	  /*Count of mcast packets received within the measurement interval*/
	  public EvelOptionDouble recvd_mcast_packets_delta;
	  /*Cumulative count of octets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_octets_acc;
	  /*Count of octets received within the measurement interval*/
	  public EvelOptionDouble recvd_octets_delta;
	  /*Cumulative count of all packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_total_packets_acc;
	  /*Count of all packets received within the measurement interval*/
	  public EvelOptionDouble recvd_total_packets_delta;
	  /*Cumulative count of unicast packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_ucast_packets_acc;
	  /*Count of unicast packets received within the measurement interval*/
	  public EvelOptionDouble recvd_ucast_packets_delta;
	  /*Cumulative count of transmitted broadcast packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_bcast_packets_acc;
	  /*Count of transmitted broadcast packets within the measurement interval*/
	  public EvelOptionDouble tx_bcast_packets_delta;
	  /*Cumulative count of transmit discarded packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_discarded_packets_acc;
	  /*Count of transmit discarded packets within the measurement interval*/
	  public EvelOptionDouble tx_discarded_packets_delta;
	  /*Cumulative count of transmit error packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_error_packets_acc;
	  /*Count of transmit error packets within the measurement interval*/
	  public EvelOptionDouble tx_error_packets_delta;
	  /*Cumulative count of transmit multicast packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_mcast_packets_acc;
	  /*Count of transmit multicast packets within the measurement interval*/
	  public EvelOptionDouble tx_mcast_packets_delta;
	  /*Cumulative count of transmit octets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_octets_acc;
	  /*Count of transmit octets received within the measurement interval*/
	  public EvelOptionDouble tx_octets_delta;
	  /*Cumulative count of all transmit packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_total_packets_acc;
	  /*Count of transmit packets within the measurement interval*/
	  public EvelOptionDouble tx_total_packets_delta;
	  /*Cumulative count of all transmit unicast packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_ucast_packets_acc;
	  /*Count of transmit unicast packets within the measurement interval*/
	  public EvelOptionDouble tx_ucast_packets_delta;
	}

	/**************************************************************************//**
	 * Codec Usage.
	 * JSON equivalent field: codecsInUse
	 *****************************************************************************/
	public class MEASUREMENT_CODEC_USE {
	  String codec_id;
	  int number_in_use;
	}

	/**************************************************************************//**
	 * Feature Usage.
	 * JSON equivalent field: featuresInUse
	 *****************************************************************************/
	public class MEASUREMENT_FEATURE_USE {
	  String feature_id;
	  int feature_utilization;
	}


	/**************************************************************************//**
	 * Custom Defined Measurement.
	 * JSON equivalent field: measurements
	 *****************************************************************************/
	public class CUSTOM_MEASUREMENT {
	  String name;
	  String value;
	}

	/**************************************************************************//**
	 * Measurement Group.
	 * JSON equivalent field: additionalMeasurements
	 *****************************************************************************/
	public class MEASUREMENT_GROUP {
	  String name;
	  ArrayList<CUSTOM_MEASUREMENT> measurements;
	}

	
	/***************************************************************************/
	  /* Mandatory fields                                                        */
	  /***************************************************************************/
	  double measurement_interval;

	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  ArrayList<String[]> additional_info;
	  ArrayList<MEASUREMENT_GROUP> additional_measurements;
	  ArrayList<javax.json.JsonObject> additional_objects;
	  ArrayList<MEASUREMENT_CODEC_USE> codec_usage;
	  EvelOptionInt concurrent_sessions;
	  EvelOptionInt configured_entities;
	  ArrayList<MEASUREMENT_CPU_USE> cpu_usage;
	  ArrayList<MEASUREMENT_DISK_USE> disk_usage;
	  boolean errstat;
	  MEASUREMENT_ERRORS myerrors;
	  ArrayList<MEASUREMENT_FEATURE_USE> feature_usage;
	  ArrayList<MEASUREMENT_FSYS_USE> filesystem_usage;
	  ArrayList<MEASUREMENT_LATENCY_BUCKET> latency_distribution;
	  EvelOptionDouble mean_request_latency;
	  ArrayList<MEASUREMENT_MEM_USE> mem_usage;
	  EvelOptionInt media_ports_in_use;
	  EvelOptionInt request_rate;
	  EvelOptionInt vnfc_scaling_metric;
	  ArrayList<MEASUREMENT_VNIC_PERFORMANCE> vnic_usage;


	/***************************************************************************/
	/* Optional fields                                                         */
	/***************************************************************************/

	  private static final Logger LOGGER = Logger.getLogger( EvelScalingMeasurement.class.getName() );


	  /**************************************************************************//**
	   * Constructs a new Measurement event.
	   *
	   * @note    The mandatory fields on the Measurement must be supplied to this
	   *          factory function and are immutable once set.  Optional fields have
	   *          explicit setter functions, but again values may only be set once so
	   *          that the Measurement has immutable properties.
	   *
	   * @param   measurement_interval
	   *
	   *****************************************************************************/
	  public EvelScalingMeasurement(double meas_interval)
	  { //Init header
        super("vnfScalingMeasurement");

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(meas_interval >= 0.0);

	    LOGGER.debug("New measurement is at "+meas_interval);

	    /***************************************************************************/
	    /* Initialize the header & the measurement fields.                         */
	    /***************************************************************************/
	    event_domain = EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT;
        //initialize optional fields
	    measurement_interval = meas_interval;
	    additional_info = null;
	    additional_measurements = null;
	    additional_objects = null;
	    cpu_usage = null;
	    disk_usage = null;
	    mem_usage = null;
	    filesystem_usage = null;
	    latency_distribution = null;
	    vnic_usage = null;
	    codec_usage = null;
	    feature_usage = null;
	    errstat = false;
	    
	    mean_request_latency = new EvelOptionDouble(false, 0.0);
	    vnfc_scaling_metric = new EvelOptionInt(false, 0);
	    concurrent_sessions = new EvelOptionInt(false, 0);
	    configured_entities = new EvelOptionInt(false, 0);
	    media_ports_in_use = new EvelOptionInt(false, 0);
	    request_rate = new EvelOptionInt(false, 0);
	    errstat = false;

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
	  public void evel_measurement_type_set(String typ)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions and call evel_header_type_set.                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    evel_header_type_set(typ);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional value name/value pair to the Measurement.
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
	  public void evel_measurement_addl_info_add(String name, String value)
		{
		  String[] addl_info = null;
		  EVEL_ENTER();

		  /***************************************************************************/
		  /* Check preconditions.                                                    */
		  /***************************************************************************/
		  assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
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
	   * Set the Concurrent Sessions property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   
	   * @param concurrent_sessions The Concurrent Sessions to be set.
	   *****************************************************************************/
	  public void evel_measurement_conc_sess_set(int conc_sessions)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(conc_sessions >= 0);

		concurrent_sessions.SetValuePr(conc_sessions,"Concurrent Sessions");
		  
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Configured Entities property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   
	   * @param configured_entities The Configured Entities to be set.
	   *****************************************************************************/
	  public void evel_measurement_cfg_ents_set(EvelScalingMeasurement measurement,
	                                     int config_entities)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(config_entities >= 0);

	    configured_entities.SetValuePr(config_entities,"Configured Entities");
		
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional set of myerrors to the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param measurement       Pointer to the measurement.
	   * @param receive_discard  The number of receive discards.
	   * @param receive_error    The number of receive myerrors.
	   * @param transmit_discard The number of transmit discards.
	   * @param transmit_error   The number of transmit myerrors.
	   *****************************************************************************/
	  public void evel_measurement_myerrors_set( 
	                                   int receive_discard,
	                                   int receive_error,
	                                   int transmit_discard,
	                                   int transmit_error)
	  {

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(receive_discard >= 0);
	    assert(receive_error >= 0);
	    assert(transmit_discard >= 0);
	    assert(transmit_error >= 0);

	    if (errstat == false)
	    {
	      errstat = true;
	      LOGGER.debug(MessageFormat.format("Adding myerrors: {0}, {1}, {2}, {3}",
	                 receive_discard,
	                 receive_error,
	                 transmit_discard,
	                 transmit_error));
	      if( myerrors == null )myerrors = new MEASUREMENT_ERRORS();
	      myerrors.receive_discards = receive_discard;
	      myerrors.receive_myerrors = receive_error;
	      myerrors.transmit_discards = transmit_discard;
	      myerrors.transmit_myerrors = transmit_error;
	    }
	    else
	    {
	      LOGGER.debug(MessageFormat.format("Adding myerrors: {0}, {1}; {2}, {3} myerrors already set: {4}, {5}; {6}, {7}",
	                 receive_discard,
	                 receive_error,
	                 transmit_discard,
	                 transmit_error,
	                 myerrors.receive_discards,
	                 myerrors.receive_myerrors,
	                 myerrors.transmit_discards,
	                 myerrors.transmit_myerrors));
	    }

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Mean Request Latency property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   
	   * @param mean_request_latency The Mean Request Latency to be set.
	   *****************************************************************************/
	  public void evel_measurement_mean_req_lat_set(
	                                         double mean_req_latency)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(mean_req_latency >= 0.0);

	    mean_request_latency.SetValuePr(mean_req_latency,"Mean Request Latency");
	    
	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the Request Rate property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   
	   * @param request_rate The Request Rate to be set.
	   *****************************************************************************/
	  public void evel_measurement_request_rate_set(int req_rate)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(req_rate >= 0);

	    request_rate.SetValuePr(req_rate,"Request Rate");
	    
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional CPU usage value name/value pair to the Measurement.
	   *
	   * The name and value are null delimited ASCII strings.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * 
	   * @param id            ASCIIZ string with the CPU's identifier.
	   * @param usage         CPU utilization.
	   *****************************************************************************/
	  public MEASUREMENT_CPU_USE evel_measurement_new_cpu_use_add(String id, double usage)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(id != null);
	    assert(usage >= 0.0);

	    /***************************************************************************/
	    /* Allocate a container for the value and push onto the list.              */
	    /***************************************************************************/
	    LOGGER.debug(MessageFormatter.format("Adding id={0} usage={1}", id, usage));
	    MEASUREMENT_CPU_USE cpu_use = new MEASUREMENT_CPU_USE();
	    assert(cpu_use != null);
	    cpu_use.id    = id;
	    cpu_use.usage = usage;
	    cpu_use.idle = new EvelOptionDouble();
	    cpu_use.intrpt = new EvelOptionDouble();
	    cpu_use.nice = new EvelOptionDouble();
	    cpu_use.softirq = new EvelOptionDouble();
	    cpu_use.steal = new EvelOptionDouble();
	    cpu_use.sys = new EvelOptionDouble();
	    cpu_use.user = new EvelOptionDouble();
	    cpu_use.wait = new EvelOptionDouble();
	    
	    if( cpu_usage == null ){
	    	cpu_usage = new ArrayList<MEASUREMENT_CPU_USE>();
	    	if( cpu_usage == null)LOGGER.error("Unable to allocate new cpu usage");
	    }

	    cpu_usage.add(cpu_use);

	    EVEL_EXIT();
	    return cpu_use;
	  }

	  /**************************************************************************//**
	   * Set the CPU Idle value in measurement interval
	   *   percentage of CPU time spent in the idle task
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_idle_set(MEASUREMENT_CPU_USE cpu_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    cpu_use.idle.SetValuePr(val,"CPU idle time");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the percentage of time spent servicing interrupts
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_interrupt_set(MEASUREMENT_CPU_USE cpu_use,
	                                              double val)
	  {
	    EVEL_ENTER();
	    cpu_use.intrpt.SetValuePr(val,"CPU interrupt value");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the percentage of time spent running user space processes that have been niced
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_nice_set(MEASUREMENT_CPU_USE cpu_use,
	                                         double val)
	  {
	    EVEL_ENTER();
	    cpu_use.nice.SetValuePr(val, "CPU nice value");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the percentage of time spent handling soft irq interrupts
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_softirq_set(MEASUREMENT_CPU_USE cpu_use,
	                                            double val)
	  {
	    EVEL_ENTER();
	    cpu_use.softirq.SetValuePr(val, "CPU Soft IRQ value");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the percentage of time spent in involuntary wait
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_steal_set(MEASUREMENT_CPU_USE cpu_use,
	                                          double val)
	  {
	    EVEL_ENTER();
	    cpu_use.steal.SetValuePr(val,"CPU involuntary wait");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the percentage of time spent on system tasks running the kernel
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_system_set(MEASUREMENT_CPU_USE cpu_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    cpu_use.sys.SetValuePr(val,"CPU System load");
	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the percentage of time spent running un-niced user space processes
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_usageuser_set(MEASUREMENT_CPU_USE cpu_use,
	                                              double val)
	  {
	    EVEL_ENTER();
	    cpu_use.user.SetValuePr(val,"CPU User load value");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the percentage of CPU time spent waiting for I/O operations to complete
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param cpu_use      Pointer to the CPU Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_cpu_use_wait_set(MEASUREMENT_CPU_USE cpu_use,
	                                         double val)
	  {
	    EVEL_ENTER();
	    cpu_use.wait.SetValuePr(val, "CPU Wait IO value");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Add an additional Memory usage value name/value pair to the Measurement.
	   *
	   * The name and value are null delimited ASCII strings.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * 
	   * @param id            ASCIIZ string with the Memory identifier.
	   * @param vmidentifier  ASCIIZ string with the VM's identifier.
	   * @param membuffsz     Memory Size.
	   *
	   * @return  Returns pointer to memory use structure in measurements
	   *****************************************************************************/
	  public MEASUREMENT_MEM_USE evel_measurement_new_mem_use_add(
	                                   String id,  String vmidentifier,  double membuffsz)
	  {
	    MEASUREMENT_MEM_USE mem_use = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(id != null);
	    assert(membuffsz >= 0.0);

	    /***************************************************************************/
	    /* Allocate a container for the value and push onto the list.              */
	    /***************************************************************************/
	    LOGGER.debug(MessageFormatter.format("Adding id={0} buffer size={1}", id, membuffsz));
	    mem_use = new MEASUREMENT_MEM_USE();
	    assert(mem_use != null);
	    mem_use.id    = id;
	    mem_use.vmid  = vmidentifier;
	    mem_use.membuffsz = membuffsz;
	    mem_use.memcache = new EvelOptionDouble();
	    mem_use.memconfig= new EvelOptionDouble();
	    mem_use.memfree= new EvelOptionDouble();
	    mem_use.slabrecl= new EvelOptionDouble();
	    mem_use.slabunrecl= new EvelOptionDouble();
	    mem_use.memused= new EvelOptionDouble();

	    assert(mem_use.id != null);
	    
	    if( mem_usage == null ){
	    	mem_usage = new ArrayList<MEASUREMENT_MEM_USE>();
	    	if( mem_usage == null )LOGGER.error("Unable to allocate new memory usage");
	    }


	    mem_usage.add(mem_use);

	    EVEL_EXIT();
	    return mem_use;
	  }

	  /**************************************************************************//**
	   * Set kilobytes of memory used for cache
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param mem_use      Pointer to the Memory Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_mem_use_memcache_set(MEASUREMENT_MEM_USE mem_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    mem_use.memcache.SetValuePr(val,"Memory cache value");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set kilobytes of memory configured in the virtual machine on which the VNFC reporting
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param mem_use      Pointer to the Memory Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_mem_use_memconfig_set(MEASUREMENT_MEM_USE mem_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    mem_use.memconfig.SetValuePr(val, "Memory configured value");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set kilobytes of physical RAM left unused by the system
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param mem_use      Pointer to the Memory Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_mem_use_memfree_set(MEASUREMENT_MEM_USE mem_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    mem_use.memfree.SetValuePr(val, "Memory freely available value");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the part of the slab that can be reclaimed such as caches measured in kilobytes
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param mem_use      Pointer to the Memory Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_mem_use_slab_reclaimed_set(MEASUREMENT_MEM_USE mem_use,
	                                       double val)
	  {
	    EVEL_ENTER();
	    mem_use.slabrecl.SetValuePr(val, "Memory reclaimable slab set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the part of the slab that cannot be reclaimed such as caches measured in kilobytes
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param mem_use      Pointer to the Memory Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_mem_use_slab_unreclaimable_set(MEASUREMENT_MEM_USE  mem_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    mem_use.slabunrecl.SetValuePr(val, "Memory unreclaimable slab set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the total memory minus the sum of free, buffered, cached and slab memory in kilobytes
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param mem_use      Pointer to the Memory Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_mem_use_usedup_set(MEASUREMENT_MEM_USE mem_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    mem_use.memused.SetValuePr(val, "Memory usedup total set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional Disk usage value name/value pair to the Measurement.
	   *
	   * The name and value are null delimited ASCII strings.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * 
	   * @param id            ASCIIZ string with the CPU's identifier.
	   * @param usage         Disk utilization.
	   *****************************************************************************/
	  public MEASUREMENT_DISK_USE evel_measurement_new_disk_use_add(String id)
	  {
	    MEASUREMENT_DISK_USE disk_use = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(id != null);

	    /***************************************************************************/
	    /* Allocate a container for the value and push onto the list.              */
	    /***************************************************************************/
	    LOGGER.debug(MessageFormatter.format("Adding id={0} disk usage", id));
	    disk_use = new MEASUREMENT_DISK_USE();
	    assert(disk_use != null);
	    disk_use.id    = id;
	    assert(disk_use.id != null);

	    disk_use.iotimeavg= new EvelOptionDouble();
	    disk_use.iotimelast= new EvelOptionDouble();
	    disk_use.iotimemax= new EvelOptionDouble();
	    disk_use.iotimemin= new EvelOptionDouble();
	    disk_use.mergereadavg= new EvelOptionDouble();
	    disk_use.mergereadlast= new EvelOptionDouble();
	    disk_use.mergereadmax= new EvelOptionDouble();
	    disk_use.mergereadmin= new EvelOptionDouble();
	    disk_use.mergewriteavg= new EvelOptionDouble();
	    disk_use.mergewritelast= new EvelOptionDouble();
	    disk_use.mergewritemax= new EvelOptionDouble();
	    disk_use.mergewritemin= new EvelOptionDouble();
	    disk_use.octetsreadavg= new EvelOptionDouble();
	    disk_use.octetsreadlast= new EvelOptionDouble();
	    disk_use.octetsreadmax= new EvelOptionDouble();
	    disk_use.octetsreadmin= new EvelOptionDouble();
	    disk_use.octetswriteavg= new EvelOptionDouble();
	    disk_use.octetswritelast= new EvelOptionDouble();
	    disk_use.octetswritemax= new EvelOptionDouble();
	    disk_use.octetswritemin= new EvelOptionDouble();
	    disk_use.opsreadavg= new EvelOptionDouble();
	    disk_use.opsreadlast= new EvelOptionDouble();
	    disk_use.opsreadmax= new EvelOptionDouble();
	    disk_use.opsreadmin= new EvelOptionDouble();
	    disk_use.opswriteavg= new EvelOptionDouble();
	    disk_use.opswritelast= new EvelOptionDouble();
	    disk_use.opswritemax= new EvelOptionDouble();
	    disk_use.opswritemin= new EvelOptionDouble();
	    disk_use.pendingopsavg= new EvelOptionDouble();
	    disk_use.pendingopslast= new EvelOptionDouble();
	    disk_use.pendingopsmax= new EvelOptionDouble();
	    disk_use.pendingopsmin= new EvelOptionDouble();
	    disk_use.timereadavg= new EvelOptionDouble();
	    disk_use.timereadlast= new EvelOptionDouble();
	    disk_use.timereadmax= new EvelOptionDouble();
	    disk_use.timereadmin= new EvelOptionDouble();
	    disk_use.timewriteavg= new EvelOptionDouble();
	    disk_use.timewritelast= new EvelOptionDouble();
	    disk_use.timewritemax= new EvelOptionDouble();
	    disk_use.timewritemin= new EvelOptionDouble();
	    
	    if( disk_usage == null ){
	    	disk_usage = new ArrayList<MEASUREMENT_DISK_USE>();
	    	if( disk_usage == null ) LOGGER.error("Unable to allocate new disk usage");
	    }

	    
	    disk_usage.add(disk_use);


	    EVEL_EXIT();
	    return disk_use;
	  }

	  /**************************************************************************//**
	   * Set milliseconds spent doing input/output operations over 1 sec; treat
	   * this metric as a device load percentage where 1000ms  matches 100% load;
	   * provide the average over the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_iotimeavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val) 
	  {
	    EVEL_ENTER();
	    disk_use.iotimeavg.SetValuePr(val,"Disk ioload set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set milliseconds spent doing input/output operations over 1 sec; treat
	   * this metric as a device load percentage where 1000ms  matches 100% load;
	   * provide the last value within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_iotimelast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.iotimelast.SetValuePr(val, "Disk ioloadlast set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set milliseconds spent doing input/output operations over 1 sec; treat
	   * this metric as a device load percentage where 1000ms  matches 100% load;
	   * provide the maximum value within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_iotimemax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.iotimemax.SetValuePr(val, "Disk ioloadmax set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set milliseconds spent doing input/output operations over 1 sec; treat
	   * this metric as a device load percentage where 1000ms  matches 100% load;
	   * provide the minimum value within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_iotimemin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.iotimemin.SetValuePr(val, "Disk ioloadmin set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of logical read operations that were merged into physical read
	   * operations, e.g., two logical reads were served by one physical disk access;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergereadavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergereadavg.SetValuePr(val, "Disk Merged read average set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of logical read operations that were merged into physical read
	   * operations, e.g., two logical reads were served by one physical disk access;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergereadlast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergereadlast.SetValuePr(val, "Disk mergedload last set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of logical read operations that were merged into physical read
	   * operations, e.g., two logical reads were served by one physical disk access;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergereadmax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergereadmax.SetValuePr(val, "Disk merged loadmax set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of logical read operations that were merged into physical read
	   * operations, e.g., two logical reads were served by one physical disk access;
	   * provide the minimum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergereadmin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergereadmin.SetValuePr(val, "Disk merged loadmin set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of logical write operations that were merged into physical read
	   * operations, e.g., two logical writes were served by one physical disk access;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergewritelast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergewritelast.SetValuePr(val, "Disk merged writelast set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of logical write operations that were merged into physical read
	   * operations, e.g., two logical writes were served by one physical disk access;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergewritemax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergewritemax.SetValuePr(val, "Disk writemax set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of logical write operations that were merged into physical read
	   * operations, e.g., two logical writes were served by one physical disk access;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_mergewritemin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.mergewritemin.SetValuePr(val, "Disk writemin set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of octets per second read from a disk or partition;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetsreadavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetsreadavg.SetValuePr(val, "Octets readavg set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of octets per second read from a disk or partition;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetsreadlast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetsreadlast.SetValuePr(val, "Octets readlast set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of octets per second read from a disk or partition;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetsreadmax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetsreadmax.SetValuePr(val, "Octets readmax set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of octets per second read from a disk or partition;
	   * provide the minimum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetsreadmin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetsreadmin.SetValuePr(val, "Octets readmin set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of octets per second written to a disk or partition;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetswriteavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetswriteavg.SetValuePr(val, "Octets writeavg set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of octets per second written to a disk or partition;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetswritelast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetswritelast.SetValuePr(val, "Octets writelast set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of octets per second written to a disk or partition;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetswritemax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetswritemax.SetValuePr(val, "Octets writemax set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of octets per second written to a disk or partition;
	   * provide the minimum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_octetswritemin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.octetswritemin.SetValuePr(val, "Octets writemin set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of read operations per second issued to the disk;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opsreadavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opsreadavg.SetValuePr(val, "Disk read operation average set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of read operations per second issued to the disk;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opsreadlast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opsreadlast.SetValuePr(val, "Disk read operation last set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of read operations per second issued to the disk;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opsreadmax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opsreadmax.SetValuePr(val, "Disk read operation maximum set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of read operations per second issued to the disk;
	   * provide the minimum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opsreadmin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opsreadmin.SetValuePr(val, "Disk read operation minimum set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of write operations per second issued to the disk;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opswriteavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opswriteavg.SetValuePr(val, "Disk write operation average set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of write operations per second issued to the disk;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opswritelast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opswritelast.SetValuePr(val, "Disk write operation last set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set number of write operations per second issued to the disk;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opswritemax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opswritemax.SetValuePr(val, "Disk write operation maximum set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set number of write operations per second issued to the disk;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_opswritemin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.opswritemin.SetValuePr(val, "Disk write operation minimum set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set queue size of pending I/O operations per second;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_pendingopsavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.pendingopsavg.SetValuePr(val, "Disk pending operation average set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set queue size of pending I/O operations per second;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_pendingopslast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.pendingopslast.SetValuePr(val, "Disk pending operation last set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set queue size of pending I/O operations per second;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_pendingopsmax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.pendingopsmax.SetValuePr(val, "Disk pending operation maximum set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set queue size of pending I/O operations per second;
	   * provide the minimum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_pendingopsmin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.pendingopsmin.SetValuePr(val, "Disk pending operation min set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set milliseconds a read operation took to complete;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timereadavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timereadavg.SetValuePr(val, "Disk read time average set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set milliseconds a read operation took to complete;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timereadlast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timereadlast.SetValuePr(val, "Disk read time last set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set milliseconds a read operation took to complete;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timereadmax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timereadmax.SetValuePr(val, "Disk read time maximum set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set milliseconds a read operation took to complete;
	   * provide the minimum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timereadmin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timereadmin.SetValuePr(val, "Disk read time minimum set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set milliseconds a write operation took to complete;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timewriteavg_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timewriteavg.SetValuePr(val, "Disk write time average set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set milliseconds a write operation took to complete;
	   * provide the last measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timewritelast_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timewritelast.SetValuePr(val, "Disk write time last set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set milliseconds a write operation took to complete;
	   * provide the maximum measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timewritemax_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timewritemax.SetValuePr(val, "Disk write time max set");
	    EVEL_EXIT();
	  }
	  /**************************************************************************//**
	   * Set milliseconds a write operation took to complete;
	   * provide the average measurement within the measurement interval
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param disk_use     Pointer to the Disk Use.
	   * @param val          double
	   *****************************************************************************/
	  public void evel_measurement_disk_use_timewritemin_set(MEASUREMENT_DISK_USE  disk_use,
	                                      double val)
	  {
	    EVEL_ENTER();
	    disk_use.timewritemin.SetValuePr(val, "Disk write time min set");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional File System usage value name/value pair to the
	   * Measurement.
	   *
	   * The filesystem_name is null delimited ASCII string.  The library takes a
	   * copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * 
	   * @param filesystem_name   ASCIIZ string with the file-system's UUID.
	   * @param block_configured  Block storage configured.
	   * @param block_used        Block storage in use.
	   * @param block_iops        Block storage IOPS.
	   * @param ephemeral_configured  Ephemeral storage configured.
	   * @param ephemeral_used        Ephemeral storage in use.
	   * @param ephemeral_iops        Ephemeral storage IOPS.
	   *****************************************************************************/
	  public void evel_measurement_fsys_use_add(
	                                     String filesystem_name,
	                                     double block_configured,
	                                     double block_used,
	                                     int block_iops,
	                                     double ephemeral_configured,
	                                     double ephemeral_used,
	                                     int ephemeral_iops)
	  {
	    MEASUREMENT_FSYS_USE fsys_use = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(filesystem_name != null);
	    assert(block_configured >= 0.0);
	    assert(block_used >= 0.0);
	    assert(block_iops >= 0);
	    assert(ephemeral_configured >= 0.0);
	    assert(ephemeral_used >= 0.0);
	    assert(ephemeral_iops >= 0);

	    /***************************************************************************/
	    /* Allocate a container for the value and push onto the list.              */
	    /***************************************************************************/
	    LOGGER.debug("Adding filesystem_name="+filesystem_name);
	    fsys_use = new MEASUREMENT_FSYS_USE();
	    assert(fsys_use != null);
	    fsys_use.filesystem_name = filesystem_name;
	    fsys_use.block_configured = block_configured;
	    fsys_use.block_used = block_used;
	    fsys_use.block_iops = block_iops;
	    fsys_use.ephemeral_configured = block_configured;
	    fsys_use.ephemeral_used = ephemeral_used;
	    fsys_use.ephemeral_iops = ephemeral_iops;
	    
	    if( filesystem_usage == null ){
	    	filesystem_usage = new ArrayList<MEASUREMENT_FSYS_USE>();
	    	if( filesystem_usage == null )LOGGER.error("Unable to allocate new file system usage");
	    }

	    filesystem_usage.add(fsys_use);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add a Feature usage value name/value pair to the Measurement.
	   *
	   * The name is null delimited ASCII string.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * 
	   * @param feature         ASCIIZ string with the feature's name.
	   * @param utilization     Utilization of the feature.
	   *****************************************************************************/
	  public void evel_measurement_feature_use_add(
	                                        String feature,
	                                        int utilization)
	  {
	    MEASUREMENT_FEATURE_USE feature_use = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(feature != null);
	    assert(utilization >= 0);

	    /***************************************************************************/
	    /* Allocate a container for the value and push onto the list.              */
	    /***************************************************************************/
	    LOGGER.debug(MessageFormatter.format("Adding Feature={0} Use={1}", feature, utilization));
	    feature_use = new MEASUREMENT_FEATURE_USE();
	    assert(feature_use != null);
	    feature_use.feature_id = feature;
	    assert(feature_use.feature_id != null);
	    feature_use.feature_utilization = utilization;
	    
	    if( feature_usage == null ){
	    	feature_usage = new ArrayList<MEASUREMENT_FEATURE_USE>();
	    	if( feature_usage == null )LOGGER.error("Unable to allocate new feature usage");
	    }

	    feature_usage.add(feature_use);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add a Additional Measurement value name/value pair to the Report.
	   *
	   * The name is null delimited ASCII string.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * @param measurement   Pointer to the Measaurement.
	   * @param group    ASCIIZ string with the measurement group's name.
	   * @param name     ASCIIZ string containing the measurement's name.
	   * @param value    ASCIIZ string containing the measurement's value.
	   *****************************************************************************/
	  public void evel_measurement_custom_measurement_add(
	                                               String  group,
	                                               String  name,
	                                               String  value)
	  {
	    MEASUREMENT_GROUP measurement_group = null;
	    CUSTOM_MEASUREMENT custom_measurement = null;
	    MEASUREMENT_GROUP item = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(group != null);
	    assert(name != null);
	    assert(value != null);

	    /***************************************************************************/
	    /* Allocate a container for the name/value pair.                           */
	    /***************************************************************************/
	    LOGGER.debug(MessageFormat.format("Adding Measurement Group={0} Name={1} Value={2}",
	                group, name, value));
	    custom_measurement = new CUSTOM_MEASUREMENT();
	    assert(custom_measurement != null);
	    custom_measurement.name = name;
	    assert(custom_measurement.name != null);
	    custom_measurement.value = value;
	    assert(custom_measurement.value != null);

	    /***************************************************************************/
	    /* See if we have that group already.                                      */
	    /***************************************************************************/
	    if (additional_measurements != null && additional_measurements.size()>0)
	    {
	      for(int i=0;i<additional_measurements.size();i++)
	      {
	    	  item = additional_measurements.get(i);
	    	  if( item.name.equals(name))
	    	  {
	    		  LOGGER.debug("Found existing Measurement Group");
	    		  measurement_group = item;
	    		  break;
	    	  }
	     }
	    }

	    /***************************************************************************/
	    /* If we didn't have the group already, create it.                         */
	    /***************************************************************************/
	    if (measurement_group == null)
	    {
	      LOGGER.debug("Creating new Measurement Group");
	      measurement_group = new MEASUREMENT_GROUP();
	      assert(measurement_group != null);
	      measurement_group.name = group;
	      assert(measurement_group.name != null);
	      if( additional_measurements == null){
	    	  additional_measurements = new ArrayList<MEASUREMENT_GROUP>();
	    	  if( additional_measurements == null ){
	    		  LOGGER.error("Unable to allocate additional measurements ");
	    	  }
	      }
	      additional_measurements.add(measurement_group);
	    }

	    /***************************************************************************/
	    /* If we didn't have the group already, create it.                         */
	    /***************************************************************************/
	    measurement_group.measurements.add(custom_measurement);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add a Codec usage value name/value pair to the Measurement.
	   *
	   * The name is null delimited ASCII string.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * 
	   * @param codec           ASCIIZ string with the codec's name.
	   * @param utilization     Number of codecs in use.
	   *****************************************************************************/
	  public void evel_measurement_codec_use_add( String codec,
	                                       int utilization )
	  {
	    MEASUREMENT_CODEC_USE codec_use = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check assumptions.                                                      */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(codec != null);
	    assert(utilization >= 0.0);

	    /***************************************************************************/
	    /* Allocate a container for the value and push onto the list.              */
	    /***************************************************************************/
	    LOGGER.debug(MessageFormatter.format("Adding Codec={0} Use={1}", codec, utilization));
	    codec_use = new MEASUREMENT_CODEC_USE();
	    assert(codec_use != null);
	    codec_use.codec_id = codec;
	    codec_use.number_in_use = utilization;
	    
	    if( codec_usage == null ){
	    	codec_usage = new ArrayList<MEASUREMENT_CODEC_USE>();
	    	if( codec_usage == null )LOGGER.error("Unable to allocate new codec usage");
	    }

	    codec_usage.add(codec_use);

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the Media Ports in Use property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   
	   * @param media_ports_in_use  The media port usage to set.
	   *****************************************************************************/
	  public void evel_measurement_media_port_use_set(
	                                           int media_portsuse)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(media_portsuse >= 0);

	    media_ports_in_use.SetValuePr(
	                        media_portsuse,
	                        "Media Ports In Use");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the VNFC Scaling Metric property of the Measurement.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * 
	   * @param scaling_metric  The scaling metric to set.
	   *****************************************************************************/
	  public void evel_measurement_vnfc_scaling_metric_set(EvelScalingMeasurement measurement,
	                                                int scaling_metric)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(measurement != null);
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(scaling_metric >= 0.0);

	    vnfc_scaling_metric.SetValuePr(
	                           scaling_metric,
	                           "VNFC Scaling Metric");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Create a new Latency Bucket to be added to a Measurement event.
	   *
	   * @note    The mandatory fields on the ::MEASUREMENT_LATENCY_BUCKET must be
	   *          supplied to this factory function and are immutable once set.
	   *          Optional fields have explicit setter functions, but again values
	   *          may only be set once so that the ::MEASUREMENT_LATENCY_BUCKET has
	   *          immutable properties.
	   *
	   * @param count         Count of events in this bucket.
	   *
	   * @returns pointer to the newly manufactured ::MEASUREMENT_LATENCY_BUCKET.
	   *          If the structure is not used it must be released using free.
	   * @retval  null  Failed to create the Latency Bucket.
	   *****************************************************************************/
	  public MEASUREMENT_LATENCY_BUCKET evel_new_meas_latency_bucket(int count)
	  {
	    MEASUREMENT_LATENCY_BUCKET bucket=null;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(count >= 0);

	    /***************************************************************************/
	    /* Allocate, then set Mandatory Parameters.                                */
	    /***************************************************************************/
	    LOGGER.debug("Creating bucket, count = "+count);
	    bucket = new MEASUREMENT_LATENCY_BUCKET();
	    assert(bucket != null);

	    /***************************************************************************/
	    /* Set Mandatory Parameters.                                               */
	    /***************************************************************************/
	    bucket.count = count;
	    
	    /***************************************************************************/
	    /* Initialize Optional Parameters.                                         */
	    /***************************************************************************/
        bucket.low_end = new EvelOptionDouble();
        bucket.high_end = new EvelOptionDouble();
        
	    EVEL_EXIT();

	    return bucket;
	  }

	  /**************************************************************************//**
	   * Set the High End property of the Measurement Latency Bucket.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param bucket        Pointer to the Measurement Latency Bucket.
	   * @param high_end      High end of the bucket's range.
	   *****************************************************************************/
	  public void evel_meas_latency_bucket_high_end_set(
	                                       MEASUREMENT_LATENCY_BUCKET  bucket,
	                                       double high_end)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(high_end >= 0.0);
	    bucket.high_end.SetValuePr(high_end, "High End");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Low End property of the Measurement Latency Bucket.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param bucket        Pointer to the Measurement Latency Bucket.
	   * @param low_end       Low end of the bucket's range.
	   *****************************************************************************/
	  public void evel_meas_latency_bucket_low_end_set(
	                                       MEASUREMENT_LATENCY_BUCKET  bucket,
	                                       double low_end)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(low_end >= 0.0);
	    bucket.low_end.SetValuePr(low_end, "Low End");
	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional Measurement Latency Bucket to the specified event.
	   *
	   * @param measurement   Pointer to the Measurement event.
	   * @param bucket        Pointer to the Measurement Latency Bucket to add.
	   *****************************************************************************/
	  public void evel_meas_latency_bucket_add(MEASUREMENT_LATENCY_BUCKET  bucket)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(bucket != null);
	    
	    if( latency_distribution == null ){
	    	latency_distribution = new ArrayList<MEASUREMENT_LATENCY_BUCKET>();
	    	if( latency_distribution == null )LOGGER.error("Unable to allocate new file system usage");
	    }
	    latency_distribution.add(bucket);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional Latency Distribution bucket to the Measurement.
	   *
	   * This function implements the previous API, purely for convenience.
	   *
	   * 
	   * @param low_end       Low end of the bucket's range.
	   * @param high_end      High end of the bucket's range.
	   * @param count         Count of events in this bucket.
	   *****************************************************************************/
	  public void evel_measurement_latency_add(
	                                    double low_end,
	                                    double high_end,
	                                    int count)
	  {
	    MEASUREMENT_LATENCY_BUCKET bucket = null;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Trust the assertions in the underlying methods.                         */
	    /***************************************************************************/
	    bucket = evel_new_meas_latency_bucket(count);
	    bucket.low_end.SetValue(low_end);
	    bucket.high_end.SetValue(high_end);
	    evel_meas_latency_bucket_add(bucket);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Create a new vNIC Use to be added to a Measurement event.
	   *
	   * @note    The mandatory fields on the ::MEASUREMENT_VNIC_PERFORMANCE must be supplied
	   *          to this factory function and are immutable once set. Optional
	   *          fields have explicit setter functions, but again values may only be
	   *          set once so that the ::MEASUREMENT_VNIC_PERFORMANCE has immutable
	   *          properties.
	   *
	   * @param vnic_id               ASCIIZ string with the vNIC's ID.
	   * @param val_suspect           True or false confidence in data.
	   *
	   * @returns pointer to the newly manufactured ::MEASUREMENT_VNIC_PERFORMANCE.
	   *          If the structure is not used it must be released using
	   *          ::evel_measurement_free_vnic_performance.
	   * @retval  null  Failed to create the vNIC Use.
	   *****************************************************************************/
	  public MEASUREMENT_VNIC_PERFORMANCE evel_measurement_new_vnic_performance(String vnic_id,
	                                                       String  val_suspect)
	  {
	    MEASUREMENT_VNIC_PERFORMANCE vnic_perf=null;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(vnic_id != null);
	    assert(val_suspect.equals("true") || val_suspect.equals("false"));

	    /***************************************************************************/
	    /* Allocate, then set Mandatory Parameters.                                */
	    /***************************************************************************/

	    vnic_perf = new MEASUREMENT_VNIC_PERFORMANCE();
	    assert(vnic_perf != null);
	    vnic_perf.vnic_id = vnic_id;
	    vnic_perf.valuesaresuspect = val_suspect;
	    
	    vnic_perf.recvd_bcast_packets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_bcast_packets_delta= new EvelOptionDouble();

	    vnic_perf.recvd_discarded_packets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_discarded_packets_delta= new EvelOptionDouble();
	    vnic_perf.recvd_error_packets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_error_packets_delta= new EvelOptionDouble();
	    vnic_perf.recvd_mcast_packets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_mcast_packets_delta= new EvelOptionDouble();
	    vnic_perf.recvd_octets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_octets_delta= new EvelOptionDouble();
	    vnic_perf.recvd_total_packets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_total_packets_delta= new EvelOptionDouble();
	    vnic_perf.recvd_ucast_packets_acc= new EvelOptionDouble();
	    vnic_perf.recvd_ucast_packets_delta= new EvelOptionDouble();
	    vnic_perf.tx_bcast_packets_acc= new EvelOptionDouble();
	    vnic_perf.tx_bcast_packets_delta= new EvelOptionDouble();
	    vnic_perf.tx_discarded_packets_acc= new EvelOptionDouble();
	    vnic_perf.tx_discarded_packets_delta= new EvelOptionDouble();
	    vnic_perf.tx_error_packets_acc= new EvelOptionDouble();
	    vnic_perf.tx_error_packets_delta= new EvelOptionDouble();
	    vnic_perf.tx_mcast_packets_acc= new EvelOptionDouble();
	    vnic_perf.tx_mcast_packets_delta= new EvelOptionDouble();
	    vnic_perf.tx_octets_acc= new EvelOptionDouble();
	    vnic_perf.tx_octets_delta= new EvelOptionDouble();
	    vnic_perf.tx_total_packets_acc= new EvelOptionDouble();
	    vnic_perf.tx_total_packets_delta= new EvelOptionDouble();
	    vnic_perf.tx_ucast_packets_acc= new EvelOptionDouble();
	    vnic_perf.tx_ucast_packets_delta= new EvelOptionDouble();
	    
	    LOGGER.debug("Adding "+vnic_perf+"VNIC ID="+ vnic_perf.vnic_id+"Value="+vnic_perf.valuesaresuspect);

	    /***************************************************************************/
	    /* Initialize Optional Parameters.                                         */
	    /***************************************************************************/
	    EVEL_EXIT();

	    return vnic_perf;
	  }



	  /**************************************************************************//**
	   * Set the Accumulated Broadcast Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_bcast_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_bcast_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_bcast_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_bcast_packets_acc >= 0.0);

	    vnic_performance.recvd_bcast_packets_acc.SetValuePr(
	                        recvd_bcast_packets_acc,
	                        "Broadcast Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Broadcast Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_bcast_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_bcast_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_bcast_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_bcast_packets_delta >= 0.0);

	    vnic_performance.recvd_bcast_packets_delta.SetValuePr(
	                        recvd_bcast_packets_delta,
	                        "Delta Broadcast Packets recieved");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the Discarded Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_discard_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_discard_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_discard_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_discard_packets_acc >= 0.0);

	    vnic_performance.recvd_discarded_packets_acc.SetValuePr(
	                        recvd_discard_packets_acc,
	                        "Discarded Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Discarded Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_discard_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_discard_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_discard_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_discard_packets_delta >= 0.0);

	    vnic_performance.recvd_discarded_packets_delta.SetValuePr(
	                        recvd_discard_packets_delta,
	                        "Delta Discarded Packets recieved");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the Error Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_error_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_error_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_error_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_error_packets_acc >= 0.0);

	    vnic_performance.recvd_error_packets_acc.SetValuePr(
	                        recvd_error_packets_acc,
	                        "Error Packets received accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Error Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_error_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_error_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_error_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_error_packets_delta >= 0.0);

	    vnic_performance.recvd_error_packets_delta.SetValuePr(
	                        recvd_error_packets_delta,
	                        "Delta Error Packets recieved");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Accumulated Multicast Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_mcast_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_mcast_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_mcast_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_mcast_packets_acc >= 0.0);

	    vnic_performance.recvd_mcast_packets_acc.SetValuePr(
	                        recvd_mcast_packets_acc,
	                        "Multicast Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Multicast Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_mcast_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_mcast_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_mcast_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_mcast_packets_delta >= 0.0);

	    vnic_performance.recvd_mcast_packets_delta.SetValuePr(
	                        recvd_mcast_packets_delta,
	                        "Delta Multicast Packets recieved");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Accumulated Octets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_octets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_octets_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_octets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_octets_acc >= 0.0);

	    vnic_performance.recvd_octets_acc.SetValuePr(
	                        recvd_octets_acc,
	                        "Octets received accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Octets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_octets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_octets_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_octets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_octets_delta >= 0.0);

	    vnic_performance.recvd_octets_delta.SetValuePr(
	                        recvd_octets_delta,
	                        "Delta Octets recieved");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Accumulated Total Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_total_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_total_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_total_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_total_packets_acc >= 0.0);

	    vnic_performance.recvd_total_packets_acc.SetValuePr(
	                        recvd_total_packets_acc,
	                        "Total Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Total Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_total_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_total_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_total_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_total_packets_delta >= 0.0);

	    vnic_performance.recvd_total_packets_delta.SetValuePr(
	                        recvd_total_packets_delta,
	                        "Delta Total Packets recieved");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Accumulated Unicast Packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_ucast_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_ucast_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_ucast_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_ucast_packets_acc >= 0.0);

	    vnic_performance.recvd_ucast_packets_acc.SetValuePr(
	                        recvd_ucast_packets_acc,
	                        "Unicast Packets received accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Unicast packets Received in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param recvd_ucast_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_rx_ucast_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double recvd_ucast_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(recvd_ucast_packets_delta >= 0.0);

	    vnic_performance.recvd_ucast_packets_delta.SetValuePr(
	                        recvd_ucast_packets_delta,
	                        "Delta Unicast packets recieved");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Transmitted Broadcast Packets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_bcast_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_bcast_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_bcast_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_bcast_packets_acc >= 0.0);

	    vnic_performance.tx_bcast_packets_acc.SetValuePr(
	                        tx_bcast_packets_acc,
	                        "Transmitted Broadcast Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Broadcast packets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_bcast_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_bcast_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_bcast_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_bcast_packets_delta >= 0.0);

	    vnic_performance.tx_bcast_packets_delta.SetValuePr(
	                        tx_bcast_packets_delta,
	                        "Delta Transmitted Broadcast packets ");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Transmitted Discarded Packets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_discarded_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_discarded_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_discarded_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_discarded_packets_acc >= 0.0);

	    vnic_performance.tx_discarded_packets_acc.SetValuePr(
	                        tx_discarded_packets_acc,
	                        "Transmitted Discarded Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Discarded packets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_discarded_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_discarded_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_discarded_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_discarded_packets_delta >= 0.0);

	    vnic_performance.tx_discarded_packets_delta.SetValuePr(
	                        tx_discarded_packets_delta,
	                        "Delta Transmitted Discarded packets ");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Transmitted Errored Packets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_error_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_error_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_error_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_error_packets_acc >= 0.0);

	    vnic_performance.tx_error_packets_acc.SetValuePr(
	                        tx_error_packets_acc,
	                        "Transmitted Error Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Errored packets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_error_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_error_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_error_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_error_packets_delta >= 0.0);

	    vnic_performance.tx_error_packets_delta.SetValuePr(
	                        tx_error_packets_delta,
	                        "Delta Transmitted Error packets ");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Transmitted Multicast Packets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_mcast_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_mcast_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_mcast_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_mcast_packets_acc >= 0.0);

	    vnic_performance.tx_mcast_packets_acc.SetValuePr(
	                        tx_mcast_packets_acc,
	                        "Transmitted Multicast Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Multicast packets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_mcast_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_mcast_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_mcast_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_mcast_packets_delta >= 0.0);

	    vnic_performance.tx_mcast_packets_delta.SetValuePr(
	                        tx_mcast_packets_delta,
	                        "Delta Transmitted Multicast packets ");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Transmitted Octets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_octets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_octets_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_octets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_octets_acc >= 0.0);

	    vnic_performance.tx_octets_acc.SetValuePr(
	                        tx_octets_acc,
	                        "Transmitted Octets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Octets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_octets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_octets_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_octets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_octets_delta >= 0.0);

	    vnic_performance.tx_octets_delta.SetValuePr(
	                        tx_octets_delta,
	                        "Delta Transmitted Octets ");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the Transmitted Total Packets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_total_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_total_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_total_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_total_packets_acc >= 0.0);

	    vnic_performance.tx_total_packets_acc.SetValuePr(
	                        tx_total_packets_acc,
	                        "Transmitted Total Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Total Packets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_total_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_total_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_total_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_total_packets_delta >= 0.0);

	    vnic_performance.tx_total_packets_delta.SetValuePr(
	                        tx_total_packets_delta,
	                        "Delta Transmitted Total Packets ");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Set the Transmitted Unicast Packets in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_ucast_packets_acc
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_ucast_pkt_acc_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_ucast_packets_acc)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_ucast_packets_acc >= 0.0);

	    vnic_performance.tx_ucast_packets_acc.SetValuePr(
	                        tx_ucast_packets_acc,
	                        "Transmitted Unicast Packets accumulated");

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Set the Delta Octets Transmitted in measurement interval
	   * property of the vNIC performance.
	   *
	   * @note  The property is treated as immutable: it is only valid to call
	   *        the setter once.  However, we don't assert if the caller tries to
	   *        overwrite, just ignoring the update instead.
	   *
	   * @param vnic_performance      Pointer to the vNIC Use.
	   * @param tx_ucast_packets_delta
	   *****************************************************************************/
	  public void evel_vnic_performance_tx_ucast_pkt_delta_set(MEASUREMENT_VNIC_PERFORMANCE  vnic_performance,
	                                      double tx_ucast_packets_delta)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(tx_ucast_packets_delta >= 0.0);

	    vnic_performance.tx_ucast_packets_delta.SetValuePr(
	                        tx_ucast_packets_delta,
	                        "Delta Transmitted Unicast Packets ");

	    EVEL_EXIT();
	  }


	  /**************************************************************************//**
	   * Add an additional vNIC Use to the specified Measurement event.
	   *
	   * 
	   * @param vnic_performance      Pointer to the vNIC Use to add.
	   *****************************************************************************/
	  public void evel_meas_vnic_performance_add(
	                              MEASUREMENT_VNIC_PERFORMANCE  vnic_performance)
	  {
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/

	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(vnic_performance != null);
	    
	    if( vnic_usage == null ){
	    	vnic_usage = new ArrayList<MEASUREMENT_VNIC_PERFORMANCE>();
	    	if( vnic_usage == null )LOGGER.error("Unable to allocate new file system usage");
	    }

	    vnic_usage.add(vnic_performance);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Add an additional vNIC usage record Measurement.
	   *
	   * This function implements the previous API, purely for convenience.
	   *
	   * The ID is null delimited ASCII string.  The library takes a copy so the
	   * caller does not have to preserve values after the function returns.
	   *
	   * @param measurement           Pointer to the measurement.
	   * @param vnic_id               ASCIIZ string with the vNIC's ID.
	   * @param valset                true or false confidence level
	   * @param recvd_bcast_packets_acc         Recieved broadcast packets
	   * @param recvd_bcast_packets_delta       Received delta broadcast packets
	   * @param recvd_discarded_packets_acc     Recieved discarded packets
	   * @param recvd_discarded_packets_delta   Received discarded delta packets
	   * @param recvd_error_packets_acc         Received error packets
	   * @param recvd_error_packets_delta,      Received delta error packets
	   * @param recvd_mcast_packets_acc         Received multicast packets
	   * @param recvd_mcast_packets_delta       Received delta multicast packets
	   * @param recvd_octets_acc                Received octets
	   * @param recvd_octets_delta              Received delta octets
	   * @param recvd_total_packets_acc         Received total packets
	   * @param recvd_total_packets_delta       Received delta total packets
	   * @param recvd_ucast_packets_acc         Received Unicast packets
	   * @param recvd_ucast_packets_delta       Received delta unicast packets
	   * @param tx_bcast_packets_acc            Transmitted broadcast packets
	   * @param tx_bcast_packets_delta          Transmitted delta broadcast packets
	   * @param tx_discarded_packets_acc        Transmitted packets discarded
	   * @param tx_discarded_packets_delta      Transmitted delta discarded packets
	   * @param tx_error_packets_acc            Transmitted error packets
	   * @param tx_error_packets_delta          Transmitted delta error packets
	   * @param tx_mcast_packets_acc            Transmitted multicast packets accumulated
	   * @param tx_mcast_packets_delta          Transmitted delta multicast packets
	   * @param tx_octets_acc                   Transmitted octets
	   * @param tx_octets_delta                 Transmitted delta octets
	   * @param tx_total_packets_acc            Transmitted total packets
	   * @param tx_total_packets_delta          Transmitted delta total packets
	   * @param tx_ucast_packets_acc            Transmitted Unicast packets
	   * @param tx_ucast_packets_delta          Transmitted delta Unicast packets
	   *****************************************************************************/
	  public void evel_measurement_vnic_performance_add(
	                                 String  vnic_id,
	                                 String valset,
	                                 double recvd_bcast_packets_acc,
	                                 double recvd_bcast_packets_delta,
	                                 double recvd_discarded_packets_acc,
	                                 double recvd_discarded_packets_delta,
	                                 double recvd_error_packets_acc,
	                                 double recvd_error_packets_delta,
	                                 double recvd_mcast_packets_acc,
	                                 double recvd_mcast_packets_delta,
	                                 double recvd_octets_acc,
	                                 double recvd_octets_delta,
	                                 double recvd_total_packets_acc,
	                                 double recvd_total_packets_delta,
	                                 double recvd_ucast_packets_acc,
	                                 double recvd_ucast_packets_delta,
	                                 double tx_bcast_packets_acc,
	                                 double tx_bcast_packets_delta,
	                                 double tx_discarded_packets_acc,
	                                 double tx_discarded_packets_delta,
	                                 double tx_error_packets_acc,
	                                 double tx_error_packets_delta,
	                                 double tx_mcast_packets_acc,
	                                 double tx_mcast_packets_delta,
	                                 double tx_octets_acc,
	                                 double tx_octets_delta,
	                                 double tx_total_packets_acc,
	                                 double tx_total_packets_delta,
	                                 double tx_ucast_packets_acc,
	                                 double tx_ucast_packets_delta)
	  {
	    MEASUREMENT_VNIC_PERFORMANCE vnic_performance = null;
	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Trust the assertions in the underlying methods.                         */
	    /***************************************************************************/
	    vnic_performance = evel_measurement_new_vnic_performance(vnic_id, valset);
	                                             
	    evel_vnic_performance_rx_bcast_pkt_acc_set(vnic_performance, recvd_bcast_packets_acc);
	    evel_vnic_performance_rx_bcast_pkt_delta_set(vnic_performance, recvd_bcast_packets_delta);
	    evel_vnic_performance_rx_discard_pkt_acc_set(vnic_performance, recvd_discarded_packets_acc);
	    evel_vnic_performance_rx_discard_pkt_delta_set(vnic_performance, recvd_discarded_packets_delta);
	    evel_vnic_performance_rx_error_pkt_acc_set(vnic_performance, recvd_error_packets_acc);
	    evel_vnic_performance_rx_error_pkt_delta_set(vnic_performance, recvd_error_packets_delta);
	    evel_vnic_performance_rx_mcast_pkt_acc_set(vnic_performance, recvd_mcast_packets_acc);
	    evel_vnic_performance_rx_mcast_pkt_delta_set(vnic_performance, recvd_mcast_packets_delta);
	    evel_vnic_performance_rx_octets_acc_set(vnic_performance, recvd_octets_acc);
	    evel_vnic_performance_rx_octets_delta_set(vnic_performance, recvd_octets_delta);
	    evel_vnic_performance_rx_total_pkt_acc_set(vnic_performance, recvd_total_packets_acc);
	    evel_vnic_performance_rx_total_pkt_delta_set(vnic_performance, recvd_total_packets_delta);
	    evel_vnic_performance_rx_ucast_pkt_acc_set(vnic_performance, recvd_ucast_packets_acc);
	    evel_vnic_performance_rx_ucast_pkt_delta_set(vnic_performance, recvd_ucast_packets_delta);
	    evel_vnic_performance_tx_bcast_pkt_acc_set(vnic_performance, tx_bcast_packets_acc);
	    evel_vnic_performance_tx_bcast_pkt_delta_set(vnic_performance, tx_bcast_packets_delta);
	    evel_vnic_performance_tx_discarded_pkt_acc_set(vnic_performance, tx_discarded_packets_acc);
	    evel_vnic_performance_tx_discarded_pkt_delta_set(vnic_performance, tx_discarded_packets_delta);
	    evel_vnic_performance_tx_error_pkt_acc_set(vnic_performance, tx_error_packets_acc);
	    evel_vnic_performance_tx_error_pkt_delta_set(vnic_performance, tx_error_packets_delta);
	    evel_vnic_performance_tx_mcast_pkt_acc_set(vnic_performance, tx_mcast_packets_acc);
	    evel_vnic_performance_tx_mcast_pkt_delta_set(vnic_performance, tx_mcast_packets_delta);
	    evel_vnic_performance_tx_octets_acc_set(vnic_performance, tx_octets_acc);
	    evel_vnic_performance_tx_octets_delta_set(vnic_performance, tx_octets_delta);
	    evel_vnic_performance_tx_total_pkt_acc_set(vnic_performance, tx_total_packets_acc);
	    evel_vnic_performance_tx_total_pkt_delta_set(vnic_performance, tx_total_packets_delta);
	    evel_vnic_performance_tx_ucast_pkt_acc_set(vnic_performance, tx_ucast_packets_acc);
	    evel_vnic_performance_tx_ucast_pkt_delta_set(vnic_performance, tx_ucast_packets_delta);
	    
	    if( vnic_usage == null ){
	    	vnic_usage = new ArrayList<MEASUREMENT_VNIC_PERFORMANCE>();
	    	if( vnic_usage == null )LOGGER.error("Unable to allocate new file system usage");
	    }
	    
	    vnic_usage.add(vnic_performance);
	  }
	  
	  /**************************************************************************//**
	   * Add a json object to jsonObject list.
	   *
	   * The name and value are null delimited ASCII strings.  The library takes
	   * a copy so the caller does not have to preserve values after the function
	   * returns.
	   *
	   * @param jsonobj   Pointer to json object
	   *****************************************************************************/
	  void evel_measurement_add_jsonobj(javax.json.JsonObject  jsonobj)
	  {

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);
	    assert(jsonobj != null);

	    LOGGER.debug("Adding jsonObject");
	    
	    if( additional_objects == null )
	    	additional_objects = new ArrayList<javax.json.JsonObject>();
	    
	    additional_objects.add(jsonobj);

	    EVEL_EXIT();
	  }

	  /**************************************************************************//**
	   * Encode Scaling Measurement Object according to VES schema
	   *
	   * @retval JSON Object of Scaling Measurement event
	   *****************************************************************************/
	JsonObjectBuilder evelScalingMeasurementObject()
	  {
	    MEASUREMENT_CPU_USE cpu_use = null;
	    MEASUREMENT_MEM_USE mem_use = null;
	    MEASUREMENT_DISK_USE disk_use = null;
	    MEASUREMENT_FSYS_USE fsys_use = null;
	    MEASUREMENT_LATENCY_BUCKET bucket = null;
	    MEASUREMENT_VNIC_PERFORMANCE vnic_use = null;
	    MEASUREMENT_FEATURE_USE feature_use = null;
	    MEASUREMENT_CODEC_USE codec_use = null;
	    MEASUREMENT_GROUP meas_group = null;
	    CUSTOM_MEASUREMENT custom_meas = null;
	    //DLIST_ITEM item = null;
	    //DLIST_ITEM nested_item = null;
	    //DLIST_ITEM addl_info_item = null;
	    //OTHER_FIELD addl_info = null;
	    double version = major_version+(double)minor_version/10;

	    EVEL_ENTER();

	    /***************************************************************************/
	    /* Check preconditions.                                                    */
	    /***************************************************************************/
	    assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_MEASUREMENT);

	    /***************************************************************************/
	    /* Mandatory fields.                                                       */
	    /***************************************************************************/
	    JsonObjectBuilder evelmeasmt = Json.createObjectBuilder()
	   	                          .add("measurementInterval", measurement_interval);

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
			evelmeasmt.add("additionalFields", builder);
		  }
	    

	    // TBD additional json objects
		if( concurrent_sessions.is_set )
			evelmeasmt.add("concurrentSessions", concurrent_sessions.GetValue());
		if( configured_entities.is_set )
			evelmeasmt.add("configuredEntities", configured_entities.GetValue());

	    /***************************************************************************/
	    /* CPU Use list.                                                           */
	    /***************************************************************************/
		  if( cpu_usage != null && cpu_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<cpu_usage.size();i++) {
			  cpu_use = cpu_usage.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "cpuIdentifier", cpu_use.id)
			                      .add( "percentUsage",cpu_use.usage);
			  
			  cpu_use.idle.encJsonValue(obj,"cpuIdle");
			  cpu_use.intrpt.encJsonValue(obj,"cpuUsageInterrupt");
			  cpu_use.nice.encJsonValue(obj,"cpuUsageNice");
			  cpu_use.softirq.encJsonValue(obj,"cpuUsageSoftIrq");
			  cpu_use.steal.encJsonValue(obj,"cpuUsageSteal");
			  cpu_use.sys.encJsonValue(obj,"cpuUsageSystem");
			  cpu_use.user.encJsonValue(obj,"cpuUsageUser");
			  cpu_use.wait.encJsonValue(obj,"cpuWait");					  
					  
			  builder.add(obj.build());
		    }
			evelmeasmt.add("cpuUsageArray", builder);
		  }
		


	    /***************************************************************************/
	    /* Disk Use list.                                                           */
	    /***************************************************************************/
		  if( disk_usage != null && disk_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<disk_usage.size();i++) {
			  disk_use = disk_usage.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "diskIdentifier", disk_use.id);
			  
			   disk_use.iotimeavg.encJsonValue(obj,"diskIoTimeAvg");
			   disk_use.iotimelast.encJsonValue(obj,"diskIoTimeLast");
			   disk_use.iotimemax.encJsonValue(obj,"diskIoTimeMax");
			   disk_use.iotimemin.encJsonValue(obj,"diskIoTimeMin");
			   disk_use.mergereadavg.encJsonValue(obj,"diskMergedReadAvg");
			   disk_use.mergereadlast.encJsonValue(obj,"diskMergedReadLast");
			   disk_use.mergereadmax.encJsonValue(obj,"diskMergedReadMax");
			   disk_use.mergereadmin.encJsonValue(obj,"diskMergedReadMin");
			   disk_use.mergewriteavg.encJsonValue(obj,"diskMergedWriteAvg");
			   disk_use.mergewritelast.encJsonValue(obj,"diskMergedWriteLast");
			   disk_use.mergewritemax.encJsonValue(obj,"diskMergedWriteMax");
			   disk_use.mergewritemin.encJsonValue(obj,"diskMergedWriteMin");
			   disk_use.octetsreadavg.encJsonValue(obj,"diskOctetsReadAvg");
			   disk_use.octetsreadlast.encJsonValue(obj,"diskOctetsReadLast");
			   disk_use.octetsreadmax.encJsonValue(obj,"diskOctetsReadMax");
			   disk_use.octetsreadmin.encJsonValue(obj,"diskOctetsReadMin");
			   disk_use.octetswriteavg.encJsonValue(obj,"diskOctetsWriteAvg");
			   disk_use.octetswritelast.encJsonValue(obj,"diskOctetsWriteLast");
			   disk_use.octetswritemax.encJsonValue(obj,"diskOctetsWriteMax");
			   disk_use.octetswritemin.encJsonValue(obj,"diskOctetsWriteMin");
			   disk_use.opsreadavg.encJsonValue(obj,"diskOpsReadAvg");
			   disk_use.opsreadlast.encJsonValue(obj,"diskOpsReadLast");
			   disk_use.opsreadmax.encJsonValue(obj,"diskOpsReadMax");
			   disk_use.opsreadmin.encJsonValue(obj,"diskOpsReadMin");
			   disk_use.opswriteavg.encJsonValue(obj,"diskOpsWriteAvg");
			   disk_use.opswritelast.encJsonValue(obj,"diskOpsWriteLast");
			   disk_use.opswritemax.encJsonValue(obj,"diskOpsWriteMax");
			   disk_use.opswritemin.encJsonValue(obj,"diskOpsWriteMin");
			   disk_use.pendingopsavg.encJsonValue(obj,"diskPendingOperationsAvg");
			   disk_use.pendingopslast.encJsonValue(obj,"diskPendingOperationsLast");
			   disk_use.pendingopsmax.encJsonValue(obj,"diskPendingOperationsMax");
			   disk_use.pendingopsmin.encJsonValue(obj,"diskPendingOperationsMin");
			   disk_use.timereadavg.encJsonValue(obj,"diskTimeReadAvg");
			   disk_use.timereadlast.encJsonValue(obj,"diskTimeReadLast");
			   disk_use.timereadmax.encJsonValue(obj,"diskTimeReadMax");
			   disk_use.timereadmin.encJsonValue(obj,"diskTimeReadMin");
			   disk_use.timewriteavg.encJsonValue(obj,"diskTimeWriteAvg");
			   disk_use.timewritelast.encJsonValue(obj,"diskTimeWriteLast");
			   disk_use.timewritemax.encJsonValue(obj,"diskTimeWriteMax");
			   disk_use.timewritemin.encJsonValue(obj,"diskTimeWriteMin");
			 
			  builder.add(obj.build());
		    }
			evelmeasmt.add("diskUsageArray", builder);
		  }


	    /***************************************************************************/
	    /* Filesystem Usage list.                                                  */
	    /***************************************************************************/
		  if( filesystem_usage != null &&  filesystem_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<filesystem_usage.size();i++) {
			  fsys_use = filesystem_usage.get(i);
			  JsonObject obj = Json.createObjectBuilder()
			    	     .add("blockConfigured", fsys_use.block_configured)
			    	     .add("blockIops", fsys_use.block_iops)
				         .add("blockUsed", fsys_use.block_used)
				         .add("ephemeralConfigured", fsys_use.ephemeral_configured)
				         .add("ephemeralIops", fsys_use.ephemeral_iops)
				         .add("ephemeralUsed", fsys_use.ephemeral_used)
				         .add("filesystemName", fsys_use.filesystem_name)			    	     
			    	     .build();
			  builder.add(obj);
		    }
			evelmeasmt.add("filesystemUsageArray", builder);
		  }

	    /***************************************************************************/
	    /* Latency distribution.                                                   */
	    /***************************************************************************/ 
		  if( latency_distribution != null && latency_distribution.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<latency_distribution.size();i++) {
			  bucket = latency_distribution.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "countsInTheBucket", bucket.count);
			  
			  bucket.low_end.encJsonValue(obj,"lowEndOfLatencyBucket");
			  bucket.high_end.encJsonValue(obj,"highEndOfLatencyBucket");				  
					  
			  builder.add(obj.build());
		    }
			evelmeasmt.add("latencyDistribution", builder);
		  }
		  
		  mean_request_latency.encJsonValue(evelmeasmt, "meanRequestLatency");
		  request_rate.encJsonValue(evelmeasmt, "requestRate");

	    /***************************************************************************/
	    /* vNIC Usage TBD Performance array                          */
	    /***************************************************************************/
		  if( vnic_usage!= null && vnic_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<vnic_usage.size();i++) {
			  vnic_use = vnic_usage.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "vNicIdentifier", vnic_use.vnic_id)
			                      .add( "valuesAreSuspect",vnic_use.valuesaresuspect);
			  
			  
	          /*********************************************************************/
	          /* Optional fields.                                                  */
	          /*********************************************************************/			  
			  vnic_use.recvd_bcast_packets_acc.encJsonValue(obj,"receivedBroadcastPacketsAccumulated");
			  vnic_use.recvd_bcast_packets_delta.encJsonValue(obj,"receivedBroadcastPacketsDelta");
			  vnic_use.recvd_discarded_packets_acc.encJsonValue(obj,"receivedDiscardedPacketsAccumulated");
			  vnic_use.recvd_discarded_packets_delta.encJsonValue(obj,"receivedDiscardedPacketsDelta");
			  vnic_use.recvd_error_packets_acc.encJsonValue(obj,"receivedErrorPacketsAccumulated");
			  vnic_use.recvd_error_packets_delta.encJsonValue(obj,"receivedErrorPacketsDelta");
			  
			  vnic_use.recvd_mcast_packets_acc.encJsonValue(obj,"receivedMulticastPacketsAccumulated");
			  vnic_use.recvd_mcast_packets_delta.encJsonValue(obj,"receivedMulticastPacketsDelta");
			  vnic_use.recvd_octets_acc.encJsonValue(obj,"receivedOctetsAccumulated");
			  vnic_use.recvd_octets_delta.encJsonValue(obj,"receivedOctetsDelta");
			  
			  vnic_use.recvd_total_packets_acc.encJsonValue(obj,"receivedTotalPacketsAccumulated");
			  vnic_use.recvd_total_packets_delta.encJsonValue(obj,"receivedTotalPacketsDelta");
			  vnic_use.recvd_ucast_packets_acc.encJsonValue(obj,"receivedUnicastPacketsAccumulated");
			  vnic_use.recvd_ucast_packets_delta.encJsonValue(obj,"receivedUnicastPacketsDelta");
			  
			  vnic_use.tx_bcast_packets_acc.encJsonValue(obj,"transmittedBroadcastPacketsAccumulated");
			  vnic_use.tx_bcast_packets_delta.encJsonValue(obj,"transmittedBroadcastPacketsDelta");
			  vnic_use.tx_discarded_packets_acc.encJsonValue(obj,"transmittedDiscardedPacketsAccumulated");
			  vnic_use.tx_discarded_packets_delta.encJsonValue(obj,"transmittedDiscardedPacketsDelta");			  

			  vnic_use.tx_error_packets_acc.encJsonValue(obj,"transmittedErrorPacketsAccumulated");
			  vnic_use.tx_error_packets_delta.encJsonValue(obj,"transmittedErrorPacketsDelta");
			  vnic_use.tx_mcast_packets_acc.encJsonValue(obj,"transmittedMulticastPacketsAccumulated");
			  vnic_use.tx_mcast_packets_delta.encJsonValue(obj,"transmittedMulticastPacketsDelta");
			  
			  vnic_use.tx_octets_acc.encJsonValue(obj,"transmittedOctetsAccumulated");
			  vnic_use.tx_octets_delta.encJsonValue(obj,"transmittedOctetsDelta");
			  vnic_use.tx_total_packets_acc.encJsonValue(obj,"transmittedTotalPacketsAccumulated");
			  vnic_use.tx_total_packets_delta.encJsonValue(obj,"transmittedTotalPacketsDelta");
			  vnic_use.tx_ucast_packets_acc.encJsonValue(obj,"transmittedUnicastPacketsAccumulated");
			  vnic_use.tx_ucast_packets_delta.encJsonValue(obj,"transmittedUnicastPacketsDelta");
					  
			  builder.add(obj.build());
		    }
			evelmeasmt.add("vNicUsageArray", builder);
		  }  
		  


	    /***************************************************************************/
	    /* Memory Use list.                                                           */
	    /***************************************************************************/ 
		  if( mem_usage != null && mem_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<mem_usage.size();i++) {
			  mem_use = mem_usage.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "memIdentifier", mem_use.id)
			                      .add( "vmIdentifier", mem_use.vmid)
			                      .add( "percentUsage",mem_use.membuffsz);
			  
			  mem_use.memcache.encJsonValue(obj,"memoryCached");
			  mem_use.memconfig.encJsonValue(obj,"memoryConfigured");
			  mem_use.memfree.encJsonValue(obj,"memoryFree");
			  mem_use.slabrecl.encJsonValue(obj,"memorySlabRecl");
			  mem_use.slabunrecl.encJsonValue(obj,"memorySlabUnrecl");
			  mem_use.memused.encJsonValue(obj,"memoryUsed");				  
					  
			  builder.add(obj.build());
		    }
			evelmeasmt.add("memUsageArray", builder);
		  }
		  
		  media_ports_in_use.encJsonValue(evelmeasmt, "numberOfMediaPortsInUse");
		  vnfc_scaling_metric.encJsonValue(evelmeasmt, "vnfcScalingMetric");
		  

	    /***************************************************************************/
	    /* myerrors list.                                                            */
	    /***************************************************************************/
	    if (errstat == true && myerrors != null) 
	    {
	    	evelmeasmt.add("receiveDiscards", myerrors.receive_discards);
	    	evelmeasmt.add("receivemyerrors", myerrors.receive_myerrors);
	    	evelmeasmt.add("transmitDiscards", myerrors.transmit_discards);
	    	evelmeasmt.add("transmitmyerrors", myerrors.transmit_myerrors);
	    }

	    /***************************************************************************/
	    /* Feature Utilization list.                                               */
	    /***************************************************************************/
		  if( feature_usage != null && feature_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<feature_usage.size();i++) {
			  feature_use = feature_usage.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "featureIdentifier", feature_use.feature_id)
			                      .add( "featureUtilization", feature_use.feature_utilization);			  
					  
			  builder.add(obj.build());
		    }
			evelmeasmt.add("featureUsageArray", builder);
		  }


	    /***************************************************************************/
	    /* Codec Utilization list.                                                 */
	    /***************************************************************************/
		  if( codec_usage != null && codec_usage.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<codec_usage.size();i++) {
			  codec_use = codec_usage.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "codecIdentifier", codec_use.codec_id)
			                      .add( "numberInUse", codec_use.number_in_use);			  
					  
			  builder.add(obj.build());
		    }
			evelmeasmt.add("codecUsageArray", builder);
		  }
		  

	    /***************************************************************************/
	    /* Additional Measurement Groups list.                                     */
	    /***************************************************************************/
		  if( additional_measurements != null && additional_measurements.size() > 0 )
		  {
		    JsonArrayBuilder builder = Json.createArrayBuilder();
		    for(int i=0;i<additional_measurements.size();i++) {
			  meas_group = additional_measurements.get(i);
			  JsonObjectBuilder obj = Json.createObjectBuilder()
			                      .add( "name", meas_group.name);
			  JsonArrayBuilder builder2 = Json.createArrayBuilder();
			  for(int j=0;j<meas_group.measurements.size();j++) {
				  custom_meas = meas_group.measurements.get(j);
				  JsonObjectBuilder obj2 = Json.createObjectBuilder()
						                    .add("name", custom_meas.name)
						                    .add("value",custom_meas.value);
				  builder2.add(obj2.build());	  
			  }
			  obj.add("measurements", builder2);
			  builder.add(obj.build());
		    }
			evelmeasmt.add("additionalMeasurements", builder);
		  }
		  
		    /***************************************************************************/
		    /* Additional Objects.                                                 */
		    /***************************************************************************/
	      if( additional_objects != null && additional_objects.size() > 0 )
		  {
			    JsonArrayBuilder builder = Json.createArrayBuilder();
			    for(int i=0;i<additional_objects.size();i++) {
				  JsonObject jobj = additional_objects.get(i);
				  builder.add(jobj);
			    }
				evelmeasmt.add("additionalObjects",builder);
		  }

	    /***************************************************************************/
	    /* Although optional, we always generate the version.  Note that this      */
	    /* closes the object, too.                                                 */
	    /***************************************************************************/
	    evelmeasmt.add("measurementsForVfScalingVersion", version);

	    EVEL_EXIT();
	    
	    return evelmeasmt;
	  }
	  
	  /**************************************************************************//**
	   * Encode the event as a JSON event object according to AT&T's schema.
	   *
	   * retval : String of JSON event message
	   *****************************************************************************/
	  String evel_json_encode_event()
	  {
		EVEL_ENTER();
		
		assert(event_domain == EvelHeader.DOMAINS.EVEL_DOMAIN_STATE_CHANGE);
		//encode common event header and measurement body    
	    JsonObject obj = Json.createObjectBuilder()
	    	     .add("event", Json.createObjectBuilder()
		    	         .add( "commonEventHeader",eventHeaderObject() )
		    	         .add( "measurementsForVfScalingFields",evelScalingMeasurementObject() )
		    	         ).build();

	    EVEL_EXIT();
	    
	    return obj.toString();

	  }


}
