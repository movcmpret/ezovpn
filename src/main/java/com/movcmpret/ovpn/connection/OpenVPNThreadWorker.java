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



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.ConnectionChangedArgs;
import com.movcmpret.event.EventManager;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;
import com.movcmpret.utility.StreamReaderThreadWorker;

import javafx.application.Platform;

/**
 *  Thread worker class for VPN connections. 
 *  Can also be used as process executer with specified shell commands.
 * @author movcmpret
 *
 */
public class OpenVPNThreadWorker implements Runnable {

	private Process process;
	private String[] commands;
	
	private StreamReaderThreadWorker stdOutThreadWorker;
	
	@Override
	public synchronized void run() {
		EventManager.getInstance().fireConnectionChangedEvent(ConnectionChangedArgs.CONNECTING);
		openVPNConnect();
		OVPNConnector.getInstance().disconnect();
		EventManager.getInstance().fireConnectionChangedEvent(ConnectionChangedArgs.DISCONNECTED);
	}
	
	/**
	 * Commands to be run 
	 * @param commands
	 */
	public OpenVPNThreadWorker(String[] kCommands)
	{
		commands = kCommands;
		
		stdOutThreadWorker =  new StreamReaderThreadWorker();
		Thread stdOutThread = new Thread(stdOutThreadWorker);
		
		stdOutThread.start();
	}

	
	private synchronized void openVPNConnect() 
	{
	
	try 
		{
		ProcessBuilder pb =  new ProcessBuilder(commands);
		pb.redirectErrorStream(true);
		process = pb.start();
		if(stdOutThreadWorker != null)
			stdOutThreadWorker.setReader(process.getInputStream());
					
		process.waitFor();

		} catch (final IOException  e) {
			e.printStackTrace();	
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					Logger.LogError("Error while executing VPN connect!");
					AlertManager.showErrorAlert(Constants.getOSBridge_VPNConnectionError() + "\n\n" + e.getMessage(), Constants.getOSBridge_VPNConnectionErrorHeader() ); 				
				}	});			
		}
	catch(InterruptedException ie)
	{
		killProcess();
	}
}

	private void killProcess() 
	{	
		process.destroy();
		boolean success = false;
		try {
			success = process.waitFor(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!success)
		{
			forceProcessKill();
		}		
	}	
	
	
	private void forceProcessKill() 
	{
		try {
			Runtime.getRuntime().exec("sudo killall -SIGINT openvpn");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	
}
