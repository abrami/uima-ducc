/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.apache.uima.ducc.cli;

import java.util.List;
import java.util.Properties;

import org.apache.uima.ducc.transport.event.CancelServiceDuccEvent;
import org.apache.uima.ducc.transport.event.CancelServiceReplyDuccEvent;
import org.apache.uima.ducc.transport.event.IDuccContext.DuccContext;
import org.apache.uima.ducc.transport.event.cli.JobRequestProperties;

/**
 * Cancel a DUCC service instance
 */

public class DuccServiceCancel 
    extends CliBase
{
	
    JobRequestProperties requestProperties = new JobRequestProperties();
    static String or_port = "ducc.orchestrator.http.port";
    static String or_host = "ducc.orchestrator.node";

    long canceledPid = -1;
    String responseMessage = null;

    UiOption[] opts = new UiOption []
    {
        UiOption.Help,
        UiOption.Debug, 

        UiOption.JobId,
        UiOption.RoleAdministrator,
    };

	
	public DuccServiceCancel(String [] args) 
        throws Exception
    {
        init(this.getClass().getName(), opts, args, requestProperties, or_host, or_port, "or");
	}

	public DuccServiceCancel(List<String> args) 
        throws Exception
    {
        String[] arg_array = args.toArray(new String[args.size()]);
        init(this.getClass().getName(), opts, arg_array, requestProperties, or_host, or_port, "or");
	}

	public DuccServiceCancel(Properties props) 
        throws Exception
    {
        for ( Object k : props.keySet() ) {      
            Object v = props.get(k);
            requestProperties.put(k, v);
        }
        init(this.getClass().getName(), opts, null, requestProperties, or_host, or_port, "or");
	}

	public String getResponseMessage()
	{
		return responseMessage;
	}

    /**
     * Services and processes share 99% of their code.  This particular module (ServiceCancel) is identical with
     * DuccProcessCancel but CLI and API uses of it may want to know which context it was executed in.  This tells you ...
     */
    public boolean isService()
    {
        return true;
    }

	public boolean execute() 
        throws Exception 
    {

        CancelServiceDuccEvent      cancelServiceDuccEvent      = new CancelServiceDuccEvent(requestProperties, DuccContext.Service);
        CancelServiceReplyDuccEvent cancelServiceReplyDuccEvent = null;
        try {
            cancelServiceReplyDuccEvent = (CancelServiceReplyDuccEvent) dispatcher.dispatchAndWaitForDuccReply(cancelServiceDuccEvent);
        } catch (Exception e) {
            message("Job not submitted:", e.getMessage());
            return false;
        } finally {
            dispatcher.close();
        }
			
        /*
         * process reply
         */
    	boolean rc = extractReply(cancelServiceReplyDuccEvent);            	
    	responseMessage = cancelServiceReplyDuccEvent.getProperties().getProperty(UiOption.Message.pname());

        // need : getResponseMessage
        //      : canceled Pids
        //      : getDuccId
    	// duccMessageProcessor.out("Job"+" "+jobId+" "+msg);
		return rc;
	}
	
	public static void main(String[] args) {
		try {
			DuccServiceCancel dsc = new DuccServiceCancel(args);
			boolean rc = dsc.execute();

            long id = dsc.getDuccId();
            String msg = dsc.getResponseMessage();
            String dtype = dsc.isService() ? "Service " : "Process ";
            System.out.println(dtype + id + " " + msg);
            System.exit(rc ? 0 : 1);

		} catch (Exception e) {
            System.out.println("Cannot cancel: " + e.getMessage());
            System.exit(1);
		}
	}
	
}
