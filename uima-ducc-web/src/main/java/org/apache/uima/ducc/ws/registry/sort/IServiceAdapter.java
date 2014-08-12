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
package org.apache.uima.ducc.ws.registry.sort;

import java.util.ArrayList;
import java.util.Properties;

public interface IServiceAdapter {
	
	public Properties getSvc();
	public Properties getMeta();
	
	public int getId();
	
	public long getLastUse();
	public long getInstances();
	public long getDeployments();
	public long getPgIn();
	public long getSwap();
	public long getSwapMax();
	public long getSize();
	
	public boolean isPingOnly();
	public boolean isPingActive();
	public boolean isRegistered();
	public boolean isViable();
	public boolean isAlert();
	public boolean getHealth();
	
	public String getPopup();
	public String getErrorText();
	public String getName();
	public String getState();
	public String getStatistics();
	public String getUser();
	public String getShareClass();
	public String getDescription();
	
	public ArrayList<String> getImplementors();
	
	public ArrayList<String> getDependentJobs();
	public ArrayList<String> getDependentServices();
	public ArrayList<String> getDependentReservations();
}