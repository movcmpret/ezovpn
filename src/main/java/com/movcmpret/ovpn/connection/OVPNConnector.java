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


import java.io.File;

import com.movcmpret.constants.Constants;
import com.movcmpret.dialog.consoledialog.ConsoleOutputDialog;
import com.movcmpret.event.ConnectionChangedArgs;
import com.movcmpret.event.EventManager;
import com.movcmpret.osBridge.OSBridge;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;
import com.movcmpret.interfaces.NewMessageHandler;

import javafx.scene.paint.Color;

/**
 * Used to create OVPNConnections
 * @author movcmpret
 *
 */
public class OVPNConnector implements NewMessageHandler
{
	
	private Thread VPNThread;
	private static OVPNConnector thisInstance = null;
	
	public boolean connectionError = false;
	
	
	private OVPNConnector() 	
	{
		EventManager.getInstance().registerNewMessageHandler(this);
	}
	
	
	public static OVPNConnector getInstance() 
	{
		if ( thisInstance == null )
			thisInstance = new OVPNConnector();
		return thisInstance;
	}
	
	/**
	 *  Connects with the specified OVPNConfig
	 * @param configFile
	 */
	public void ConnectWithCredentials(OVPNConfig config) 
	{
		connectionError = false;
		UserProfile.getInstance().setConnectionInfo(new OVPNConnectionInfo(config, new ConsoleOutputDialog()));
    	
		if( !UserProfile.getInstance().getConnectionInfo().getConnectionState().equals(ConnectionChangedArgs.DISCONNECTED))
			return;
		
		//just to be sure
		disconnect();

		String[] cmd = buildConnectionString(config.getConfigFile());
		
		if(cmd == null)
		{
			disconnect();
			EventManager.getInstance().fireConnectionChangedEvent(ConnectionChangedArgs.DISCONNECTED);
			return;
		}
		
		
		OpenVPNThreadWorker openVPNThreadWorker = new OpenVPNThreadWorker(cmd);	
		VPNThread = new Thread(openVPNThreadWorker);
		VPNThread.start();			
	}
	
	private String[] buildConnectionString(File configFile) 
	{	
	
		//Write user and pass to temp file
	    try {
	    	OSBridge.getInstance().createTempLoginFile();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}     
	    
	    if(OSBridge.getInstance().isLinux())
	    {
		return new String[] { "openvpn", "--management", OpenVPNManagementInterface.HOST,
				OpenVPNManagementInterface.PORT, "--config", configFile.getAbsolutePath(), "--auth-user-pass",
				Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name(),
				"--verb", "3" };
	    }
	    else
	    {
	    	return new String[] {"openvpn.exe", "--management", OpenVPNManagementInterface.HOST,
					OpenVPNManagementInterface.PORT, "--config", configFile.getAbsolutePath(), "--auth-user-pass",
					Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name(),
					"--verb", "3" };
	    }
	}
	
	public void disconnect() 
	{
		//Should work
		OpenVPNManagementInterface.getInstance().disconnect();
		//nevertheless - redundancy
		if(this.VPNThread != null)
		{
			this.VPNThread.interrupt();
			this.VPNThread = null;
		}

	}
	
	
	private void newMessageStreamReaderThreadWorkerWindows(String message) 
	{
		if (UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog() != null)
			if(message.matches(".*ERROR:.*Windows route add.*failed.*"))
			{
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(message);
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(
						"Error while adding a route. Please try to restart as adminstrator.", Color.RED);
				connectionError = true;
			}
			else if (message.contains("Error") || message.contains("ERROR") || message.contains("FAILED"))
			{
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(message, Color.RED);
				this.disconnect();
			}
			else if (message.matches(".*MANAGEMENT:.*TCP.*listening on.*")) 
			{
				OpenVPNManagementInterface.getInstance().connect();
			} 
			else if (message.matches(".*MANAGEMENT:.*Socket bind failed.*")) 
			{
				OpenVPNManagementInterface.getInstance().connect();
				OpenVPNManagementInterface.getInstance().disconnect();
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(
						"An (old) instance seem to block the communication. Trying to kill the process, please restart." + 
						"If this error occurs again, please make sure to kill all OpenVPN processes that may run a " +
						"management interface instance. Some \"abandoned instances\" may kill themselfs after a few minutes.", Color.RED);
			} 
			else 
			{
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(message,
						Color.BLACK);
			}
		if (message.matches(".*authfile.*is empty.*") || message.contains("AUTH_FAILED")) {
			UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog()
					.appendString(Constants.getOSBridge_CheckLoginData(), Color.RED);
			AlertManager.showErrorAlert(Constants.getOSBridge_CheckLoginData(), "Login Error");
			connectionError = true;
		}
		// Check if openvpn output has an error. If not, set as connected.
		if (message.contains("Initialization Sequence Completed") && !connectionError) {
			EventManager.getInstance().fireConnectionChangedEvent(ConnectionChangedArgs.CONNECTED);
		}
	}

	
	private void newMessageStreamReaderThreadWorkerLinux(String message) 
	{

		if (UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog() != null)
			if (message.contains("Error") || message.contains("ERROR") || message.contains("FAILED"))
			{
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(message, Color.RED);
				connectionError = true;
			}
			else if (message.matches(".*MANAGEMENT:.*TCP.*listening on.*")) 
			{
				OpenVPNManagementInterface.getInstance().connect();
			} 
			else if (message.matches(".*MANAGEMENT:.*Socket bind failed.*")) 
			{
				OpenVPNManagementInterface.getInstance().connect();
				OpenVPNManagementInterface.getInstance().disconnect();
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(
						"An (old) seem to block the communication. Trying to kill the process, please restart." + 
						"If this error occurs again, please make sure to kill all OpenVPN processes that may run a " +
						"management interface instance. Some \"abandoned instances\" may kill themselfs after a few minutes.", Color.RED);
			} 
			else 
			{
				UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().appendString(message,
						Color.BLACK);
			}
		if (message.matches(".*authfile.*is empty.*") || message.contains("AUTH_FAILED")) {
			UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog()
					.appendString(Constants.getOSBridge_CheckLoginData(), Color.RED);
			AlertManager.showErrorAlert(Constants.getOSBridge_CheckLoginData(), "Login Error");
			connectionError = true;
		}
		// Check if openvpn output has an error. If not, set as connected.
		if (message.contains("Initialization Sequence Completed") && !connectionError) {
			EventManager.getInstance().fireConnectionChangedEvent(ConnectionChangedArgs.CONNECTED);
		}
	}
	
	/**
	 * New message available from stdout of the VPNThread. Show it in
	 * {@code ConsoleOutputDialog} and log it
	 */
	@Override
	public void newMessageStreamReaderThreadWorker(String message) 
	{
		Logger.LogInfo(message);
		if(OSBridge.getInstance().isLinux())
		{
		newMessageStreamReaderThreadWorkerLinux(message);
		}
		else 
		{
			newMessageStreamReaderThreadWorkerWindows(message);
		}
	}

	@Override
	public void newMessageManagementInterface(String message) 
	{
		if(message.matches(">BYTECOUNT:.*"))
		{
			String received = message.substring(message.indexOf(":")+1, message.indexOf(","));
			String sent = message.substring(message.indexOf(",")+1);
			received = String.format("%.2f",(((double)Integer.valueOf(received).intValue()/1024/1024))) + " Mb";
			sent = String.format("%.2f",(((double)Integer.valueOf(sent).intValue()/1024/1024))) + " Mb";
			UserProfile.getInstance().getConnectionInfo().getSentBytesProperty().set(sent);
			UserProfile.getInstance().getConnectionInfo().getReceivedBytesProperty().set(received);			
		}
		
	}
		
	}

