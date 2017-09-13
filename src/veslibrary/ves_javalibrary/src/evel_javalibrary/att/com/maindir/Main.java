package evel_javalibrary.att.com.maindir;

/**************************************************************************//**
 * @file
 * Sample Test Agent for EVEL library
 *
 * This file implements the Sample Agent which is intended to provide a
 * simple wrapper around the complexity of AT&T's Vendor Event Listener API so
 * that VNFs can use it without worrying about details of the API transport.
 * It also shows how events can be formatted with data for POST
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

import evel_javalibrary.att.com.*;
import evel_javalibrary.att.com.AgentMain.EVEL_ERR_CODES;
import evel_javalibrary.att.com.EvelFault.EVEL_SEVERITIES;
import evel_javalibrary.att.com.EvelFault.EVEL_SOURCE_TYPES;
import evel_javalibrary.att.com.EvelFault.EVEL_VF_STATUSES;
import evel_javalibrary.att.com.EvelHeader.PRIORITIES;
import evel_javalibrary.att.com.EvelMobileFlow.MOBILE_GTP_PER_FLOW_METRICS;
import evel_javalibrary.att.com.EvelScalingMeasurement.MEASUREMENT_CPU_USE;
import evel_javalibrary.att.com.EvelScalingMeasurement.MEASUREMENT_VNIC_PERFORMANCE;
import evel_javalibrary.att.com.EvelStateChange.EVEL_ENTITY_STATE;
import evel_javalibrary.att.com.EvelThresholdCross.EVEL_ALERT_TYPE;
import evel_javalibrary.att.com.EvelThresholdCross.EVEL_EVENT_ACTION;

import org.apache.log4j.Level;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Main
{

        public static void main(String[] args)
        {

       try{

        AgentMain.evel_initialize("http://127.0.0.1", 30000,
                              "/vendor_event_listener","/example_vnf",
                "pill",
                "will",
                Level.DEBUG);
       } catch( Exception e )
       {
           e.printStackTrace();
       }

        for(int i= 0; i < 20; i++)
        {
              EvelHeader header  = EvelHeader.evel_new_heartbeat("Hearbeat_vAFX","vmname_ip");
              header.evel_nfnamingcode_set("vVNF");
              header.evel_nfcnamingcode_set("vVNF");
              AgentMain.evel_post_event(header);
              try {
                Thread.sleep(1);
              } catch( Exception e )
              {
                 e.printStackTrace();
              }

              EvelFault flt  = new EvelFault("Fault_vVNF", "vmname_ip",
            		  "NIC error", "Hardware failed",
                  EvelHeader.PRIORITIES.EVEL_PRIORITY_HIGH,
                  EVEL_SEVERITIES.EVEL_SEVERITY_MAJOR,
                  EVEL_SOURCE_TYPES.EVEL_SOURCE_CARD,
                  EVEL_VF_STATUSES.EVEL_VF_STATUS_ACTIVE);
              flt.evel_fault_addl_info_add("nichw", "fail");
              flt.evel_fault_addl_info_add("nicsw", "fail");
              AgentMain.evel_post_event(flt);

              EvelStateChange stc  = new EvelStateChange("StateChange_vVNF", "vmname_ip",
            		      EvelStateChange.EVEL_ENTITY_STATE.EVEL_ENTITY_STATE_IN_SERVICE,
                          EvelStateChange.EVEL_ENTITY_STATE.EVEL_ENTITY_STATE_OUT_OF_SERVICE,"bgp");
              stc.evel_statechange_addl_info_add("bgpa", "fail");
              stc.evel_statechange_addl_info_add("bgpb", "fail");

              AgentMain.evel_post_event(stc);

              EvelScalingMeasurement sm  = new EvelScalingMeasurement(10.0,"Measurements_vVNF", "vmname_ip");
              sm.evel_nfnamingcode_set("vVNF");
              sm.evel_nfcnamingcode_set("vVNF");
              sm.evel_measurement_myerrors_set(10,20,30,40);
              MEASUREMENT_CPU_USE my1 = sm.evel_measurement_new_cpu_use_add("cpu1", 100.0);
              my1.idle.SetValue(20.0);
              my1.sys.SetValue(21.0);
              MEASUREMENT_CPU_USE my2 = sm.evel_measurement_new_cpu_use_add("cpu2", 10.0);
              my2.steal.SetValue(34.0);
              my2.user.SetValue(32.0);


              MEASUREMENT_VNIC_PERFORMANCE vnic_p = sm.evel_measurement_new_vnic_performance("vnic1","true");
              vnic_p.recvd_bcast_packets_acc.SetValue(2400000.0);
              vnic_p.recvd_mcast_packets_delta.SetValue(5677888.0);
              vnic_p.recvd_mcast_packets_acc.SetValue(5677888.0);
              vnic_p.tx_ucast_packets_acc.SetValue(547856576.0);
              vnic_p.tx_ucast_packets_delta.SetValue(540000.0);
              sm.evel_meas_vnic_performance_add(vnic_p);

              AgentMain.evel_post_event(sm);
              
              EvelSyslog sysl = new EvelSyslog("Syslog_vVNF", "vmname_ip",
            		                    EvelFault.EVEL_SOURCE_TYPES.EVEL_SOURCE_ROUTER,
            		                   "Router failed","JUNIPER");
              sysl.evel_nfnamingcode_set("vVNF");
              sysl.evel_nfcnamingcode_set("vVNF");
              sysl.evel_syslog_proc_id_set(456);
              sysl.evel_syslog_proc_set("routed");
              AgentMain.evel_post_event(sysl);
              
              EvelHeartbeatField hfld = new EvelHeartbeatField(123,"HeartbeatField_vVNF", "vmname_ip");
              hfld.evel_hrtbt_interval_set(23);
              AgentMain.evel_post_event(hfld);
              
              
              EvelSipSignaling sip = new EvelSipSignaling("SipSignaling_vVNF", "vmname_ip","aricent","corlator","127.0.0.1","5647","10.1.1.124","5678");
              sip.evel_nfnamingcode_set("vVNF");
              sip.evel_nfcnamingcode_set("vVNF");
              AgentMain.evel_post_event(sip);
              
              EvelVoiceQuality vq = new EvelVoiceQuality("VoiceQuality_vVNF", "vmname_ip",
            		  "calleeSideCodc",
      			    "callerSideCodc", "corlator",
    			    "midCllRtcp", "juniper");
              vq.evel_nfnamingcode_set("vVNF");
              vq.evel_nfcnamingcode_set("vVNF");
              vq.evel_voice_quality_end_metrics_set("adjname", "Caller", 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 15.1, 160.12, 170, 180, 190);
              AgentMain.evel_post_event(vq);
              
              EvelOther ev = new EvelOther("MyCustomEvent_vVNF", "vmname_ip");
              ev.evel_other_field_add("a1", "b1");
              ev.evel_other_field_add("a1", "b2");
              
              /*ev.evel_other_field_add_namedarray("a1", "b1", "c1");
              ev.evel_other_field_add_namedarray("a1", "b2", "c2");
              ev.evel_other_field_add_namedarray("a2", "b1", "c1");
              ev.evel_other_field_add_namedarray("a2", "b1", "c1");*/
              AgentMain.evel_post_event(ev);
              
              

      		String dateStart = "01/14/2012 09:29:58";
      		String dateStop = "01/15/2012 10:31:48";

      		//HH converts hour in 24 hours format (0-23), day calculation
      		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

      		Date d1 = null;
      		Date d2 = null;

      		try {
      			d1 = format.parse(dateStart);
      			d2 = format.parse(dateStop);
      		}catch (Exception e) {
    			e.printStackTrace();
    		}
              
              
              EvelThresholdCross tca = new EvelThresholdCross("ThresholdCross_vVNF", "vmname_ip",
              "CRIT",
              "mcast Limit reached",
              "mcastRxPackets",
              "1250000000",
              EvelThresholdCross.EVEL_EVENT_ACTION.EVEL_EVENT_ACTION_SET,
              "Mcast Rx breached", 
              EvelThresholdCross.EVEL_ALERT_TYPE.EVEL_ELEMENT_ANOMALY,
              d1, 
              EvelThresholdCross.EVEL_SEVERITIES.EVEL_SEVERITY_CRITICAL,
              d2);
              tca.evel_nfnamingcode_set("vVNF");
              tca.evel_nfcnamingcode_set("vVNF");
              tca.evel_threshold_cross_interfacename_set("ns345");
              tca.evel_thresholdcross_addl_info_add("n1", "v1");
              tca.evel_thresholdcross_addl_info_add("n2", "v2");
              tca.evel_thresholdcross_alertid_add("alert1");
              tca.evel_thresholdcross_alertid_add("alert2");
              
              AgentMain.evel_post_event(tca);
              

              EvelMobileFlow mf = new EvelMobileFlow("MobileFlow_vVNF", "vmname_ip",
            		  "In",
                      null,
                      "GTP",
                       "v2.3",
                      "1.2.3.4",
                      345556,
                      "5.6.7.8",
                      334344);
              MOBILE_GTP_PER_FLOW_METRICS mygtp = mf.new MOBILE_GTP_PER_FLOW_METRICS(
                      1.01,
                      2.02,
                      3,
                      4,
                      5,
                      6,
                      7,
                      8,
                      9,
                      d1,
                      "ACTIVE",
                      10,
                      11,
                      12,
                      13,
                      14,
                      15,
                      16,
                      17,
                      18,
                      19,
                      20,
                      21,
                      22,
                      23,
                      24,
                      25,
                      26,
                      27,
                      28);
              mf.gtp_per_flow_metrics = mygtp;
              mf.evel_nfnamingcode_set("vVNF");
              mf.evel_nfcnamingcode_set("vVNF");
              AgentMain.evel_post_event(mf);


    }

  }
}
