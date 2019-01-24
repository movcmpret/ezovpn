package com.movcmpret.ovpn.connection;

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

import org.joda.time.DateTime;
import org.joda.time.Period;
import com.movcmpret.constants.Constants;
import com.movcmpret.dialog.consoledialog.ConsoleOutputDialog;
import com.movcmpret.event.ConnectionChangedArgs;
import com.movcmpret.event.EventManager;
import com.movcmpret.history.ConnectionHistoryElement;
import com.movcmpret.interfaces.ConnectionChanged;
import com.movcmpret.osBridge.OSBridge;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.persistence.UserProfile;

/**
 * Class for storing connection information
 * @author movcmpret
 *
 */
public class OVPNConnectionInfo implements ConnectionChanged
{

	private Date connectionEstablished;
	private Date lastDate = null;
	private OVPNConfig config;
	private ConsoleOutputDialog consoleOutputDialog;
	private ConnectionChangedArgs connectionState = ConnectionChangedArgs.DISCONNECTED;
	private boolean isDisconnected = true;
	private boolean isDestroyed = false;
	
	private StringProperty timeSpan = new SimpleStringProperty("");
	private StringProperty bytesSent = new SimpleStringProperty("");
	private StringProperty bytesReceived = new SimpleStringProperty("");
	
	public OVPNConnectionInfo(OVPNConfig conf, ConsoleOutputDialog dialog)
	{
		this.config = conf;
		this.consoleOutputDialog = dialog;
		EventManager.getInstance().registerConnectionChangedHandler(this);
		
		setupTimingThread();
	}
	
	private void setupTimingThread() {
		Thread timethread = new Thread(new Runnable() 
		{
			@Override
			public void run() {
				
				while(!isDestroyed)
				{
					
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						timeSpan.setValue(calcTimeSpan());
					}
				});
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
					
					
			}});
				
		timethread.start();
		
	}
	
	/**
	 * Stops thread and unregisters from EventManager
	 */
	public void destroy() 
	{
		this.isDestroyed = true;
		EventManager.getInstance().unRegisterConnectionChangedHandler(this);
	}
	
	public String getConnectionDateAsString() 
	{
		if( connectionEstablished != null )
		return Constants.getDateFormatter().print(new DateTime(this.connectionEstablished.getTime()));
		return "";
	}

	/**
	 * 
	 * @return Connection duration
	 */
	private String calcTimeSpan() 
	{
		if(!isDisconnected && this.lastDate != null && this.connectionEstablished != null)
		{
		lastDate = new Date();
		return diffDatesAndMakeString(this.connectionEstablished , lastDate );
		}
		else if(isDisconnected && this.lastDate != null && this.connectionEstablished != null)
		{
			return diffDatesAndMakeString(this.connectionEstablished , lastDate );
		}
	return "";
	}
	
	private String diffDatesAndMakeString(Date begin, Date end) 
	{
		Period p = new Period(end.getTime() - begin.getTime());
		return Constants.TIME_FORMATTER.print(p);

	}
	
	public String getTimeSpan() 
	{
		return timeSpan.getValue();
	}

	public Date getConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(Date connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	public OVPNConfig getConfig() {
		return config;
	}

	public void setConfig(OVPNConfig config) {
		this.config = config;
	}

	public ConsoleOutputDialog getConsoleOutputDialog() {
		return consoleOutputDialog;
	}

	public void setConsoleOutputDialog(ConsoleOutputDialog dialog) {
		this.consoleOutputDialog = dialog;
	}
	
	
	public ConnectionChangedArgs getConnectionState() {
		return connectionState;
	}

	public void setConnectionState(ConnectionChangedArgs connectionState) {
		this.connectionState = connectionState;
	}

	@Override
	public void isConnected() {
		this.connectionState = ConnectionChangedArgs.CONNECTED;
		connectionEstablished = new Date();
		lastDate = new Date();
		isDisconnected = false;
		OSBridge.getInstance().deleteTempLoginFile();
	}

	@Override
	public void isDisconnected() {
		this.connectionState = ConnectionChangedArgs.DISCONNECTED;
		isDisconnected = true;
		UserProfile.getInstance().getUserProfileData().getObservableConnectionInformationList()
				.add(new ConnectionHistoryElement(this));
		OSBridge.getInstance().deleteTempLoginFile();
	}

	@Override
	public void isConnecting() {
		this.connectionState = ConnectionChangedArgs.CONNECTING;
		isDisconnected = true;
		
	}

	public StringProperty getTimeSpanProperty() 
	{
		return this.timeSpan;
	}

	public StringProperty getSentBytesProperty() 
	{
		return this.bytesSent;
	}

	public StringProperty getReceivedBytesProperty() 
	{
		return this.bytesReceived;
	}
			
}
