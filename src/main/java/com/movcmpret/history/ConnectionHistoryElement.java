package com.movcmpret.history;

/*-
 * #%L
 * ezOVPNClient
 * %%
 * Copyright (C) 2018 - 2019 movcmpret.com
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */



import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import com.movcmpret.constants.Constants;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.ovpn.connection.OVPNConnectionInfo;

/**
 * Stores connection information, which can be serialized to the session object. Depends on OVPNConnectionInfo
 * @author movcmpret
 *
 */
public class ConnectionHistoryElement 
{

	private OVPNConfig config;
	private String connectionTimeDuration;
	private List<String> log;
	private Date connectionDate;
	private String bytesSent;
	private String bytesReceived;
	
	public ConnectionHistoryElement(OVPNConnectionInfo info)
	{
		this.config = info.getConfig();
		this.connectionTimeDuration = info.getTimeSpan();
		this.log =  info.getConsoleOutputDialog().getFullText();
		this.connectionDate = info.getConnectionEstablished();
		this.bytesSent = info.getSentBytesProperty().get();
		this.bytesReceived = info.getReceivedBytesProperty().get();
	}
	public ConnectionHistoryElement() 
	{
		
	}
	public OVPNConfig getConfig() {
		return config;
	}
	public void setConfig(OVPNConfig config) {
		this.config = config;
	}
	public String getConnectionTimeDuration() {
		return connectionTimeDuration;
	}
	public void setConnectionTimeDuration(String connectionTimeDuration) {
		this.connectionTimeDuration = connectionTimeDuration;
	}
	public List<String> getLog() {
		return log;
	}
	public void setLog(List<String> log) {
		this.log = log;
	}
	
	public String getConnectionDateAsString() 
	{
		if(connectionDate != null)
		return Constants.getDateFormatter().print(new DateTime(this.connectionDate.getTime()));
		return "";
	}
	public Date getConnectionDate() {
		return connectionDate; 
	}

	public void setConnectionDate(Date connectionDate) {
		this.connectionDate = connectionDate;
	}
	public String getBytesSent() {
		return bytesSent;
	}
	public void setBytesSent(String bytesSent) {
		this.bytesSent = bytesSent;
	}
	public String getBytesReceived() {
		return bytesReceived;
	}
	public void setBytesReceived(String bytesReceived) {
		this.bytesReceived = bytesReceived;
	}	
	
	//###For History Table
		
		public String getName() 
		{
			if(this.config != null)
				return this.getConfig().getFileName();
			else
				return "N/A";
		}
		
		public String getDate() 
		{
			if(this.connectionDate != null)
				return this.getConnectionDateAsString();
			else
				return "N/A";
		}
		//Duration
		public String getTime() 
		{
			return this.connectionTimeDuration;
		}
	
}
