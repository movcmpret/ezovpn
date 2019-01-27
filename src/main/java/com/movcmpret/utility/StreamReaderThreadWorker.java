package com.movcmpret.utility;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.movcmpret.event.EventManager;

import javafx.application.Platform;

/**
 * Reads a stream and fires an event on new messages. Used for asynchronous reading of streams. 
 * Very rough implementation though - be careful
 * @author movcmpret
 *
 */

public class StreamReaderThreadWorker implements Runnable
{
	private BufferedReader reader;
	private ArrayList<String> history;
	private boolean isTerminated = false;
	public StreamReaderThreadWorker()
	{
		history =  new ArrayList<String>();
	}
	
	public StreamReaderThreadWorker(BufferedReader bufReader) 
	{
		this();
		reader = bufReader;

	}
	
	public StreamReaderThreadWorker(InputStream input) 
	{
		this();
		reader = new BufferedReader(new InputStreamReader(input));
	}
	
	@Override
	public synchronized void run() {

		while(!isTerminated) 
		{
			try {
				Thread.sleep(25);
					if(reader != null && reader.ready()) 
					{
						String msg = reader.readLine();
						history.add(msg);
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								EventManager.getInstance().fireNewMessageStreamReaderThreadWorkerEvent(msg);
							}	});			
					}
					//Thread sync
					Thread.sleep(25);
					}
				 catch (IOException | InterruptedException e) {
					Logger.LogDebug("Interrupting/Closing "+this.getClass().getName());
					return;
				}

		}
	}
	
	public ArrayList<String> getHistory()
	{
		return history;
	}
	
	public String getLastMessage()
	{	if(history.size() >=1 )
		return history.get(history.size()-1);
		return "";
	}
	
	public void setReader(BufferedReader r) 
	{
		this.reader = r;
	}
	
	public void setReader(InputStream i) 
	{
		this.reader = new BufferedReader(new InputStreamReader(i));
	}	
	
	public void terminate() 
	{
		isTerminated = true;
	}

}
