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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.movcmpret.event.EventManager;
import com.movcmpret.utility.Logger;

import javafx.application.Platform;

/**
 * Class for interacting with the management interface
 * 
 * see:  https://openvpn.net/community-resources/management-interface/
 * 		 https://openvpn.net/community-resources/reference-manual-for-openvpn-2-0/
 * 
 * Singleton
 * @author movcmpret
 *
 */
public class OpenVPNManagementInterface 
{
	// COMMANDS 
	//kills the connection
	private static final String SIGNAL_SIGTERM = "signal SIGTERM";
	private static final String ENABLE_BYTECOUNT = "bytecount 1";
	private static final String DISABLE_BYTECOUNT = "bytecount 0";
		
	//One client - one host 
	public static final String PORT = "1337";
	public static final String HOST= "127.0.0.1";
	
	private Socket clientSocket;
	OutputStreamWriter output;
	private boolean isConnected;
	
	private static OpenVPNManagementInterface thisInstance = null;
		
	public static OpenVPNManagementInterface getInstance() 
	{
		if(thisInstance == null)
			thisInstance = new OpenVPNManagementInterface();
		return thisInstance;
	}
	
	/**
	 * Connects the socket and 
	 */
	public void connect()
	{
		isConnected = true;
		startSocketReader();
	}
	
	/**
	 * Disconnects the Socket and kills the OVPN process
	 */
	public void disconnect()
	{
			this.writeCommandToStream(SIGNAL_SIGTERM);
		//Socket will be closed automatically due to try-with-resource
		isConnected = false;
	}
	

	private void writeCommandToStream(String command) 
	{
		try {
			if(output!=null && this.clientSocket.isConnected() && isConnected)
			{
				output.write(command+"\r\n");
				output.flush();
			}			
		} catch (IOException e) {
			e.printStackTrace();
			Logger.LogError(this.getClass().getName() + ": Error while sending command\" "+command+"\" to Socket.");
		}
		
	}
	
	private void startSocketReader() 
	{
		Thread readerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				
				try(Socket clientSocket = new Socket(HOST,Integer.valueOf(PORT).intValue()))
				{		
					OpenVPNManagementInterface.this.clientSocket = clientSocket;
					BufferedReader input = null;
					if(clientSocket.isConnected())
					{
					if( output == null )
						output = new OutputStreamWriter(clientSocket.getOutputStream());	
					if( input == null ) 
						input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					writeCommandToStream(ENABLE_BYTECOUNT);
					}
					while(clientSocket.isConnected() && isConnected)
					{					
						Thread.sleep(1000);
						while(input.ready())
							newMessage(input.readLine());
					}
				} 
				catch (NumberFormatException | IOException e) 
				{
					Logger.LogError(this.getClass().getName()+": Error while creating TCP Socket for host: " + HOST+":"+PORT+"\n"+e.getMessage());
					e.printStackTrace();
				}
				catch( InterruptedException e)
				{
					Logger.LogDebug(this.getClass().getName()+": SocketReader interrupted.");
				}
				output = null;
				return;
			}								
		});
		readerThread.start();				
	}
	
	private void newMessage(String message)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() 
			{
				EventManager.getInstance().firenewMessageManagementInterfaceEvent(message);
			}		
		});
	}
}
