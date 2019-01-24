package com.movcmpret.event;

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



import java.util.ArrayList;
import java.util.List;

import com.movcmpret.interfaces.ConnectionChanged;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.interfaces.ListChangeHandler;
import com.movcmpret.interfaces.NewMessageHandler;

/**
 * Takes care of event processing
 * @author movcmpret
 *
 */
public class EventManager {

	private List<ListChangeHandler> listChangeHandler;
	private List<DefaultController> updateViewHandler;
	private List<NewMessageHandler> newMessageHandler;
	private List<ConnectionChanged> connectionChangedHandler;

	
	private static EventManager SingletonObject = null;
	/**
	 * Singleton pattern for unique instance
	 */
	private EventManager() 
	{
		listChangeHandler = new ArrayList<ListChangeHandler>();
		updateViewHandler = new ArrayList<DefaultController>();
		newMessageHandler = new ArrayList<NewMessageHandler>();
		connectionChangedHandler = new ArrayList<ConnectionChanged>();
	}
	
	public static EventManager getInstance() 
	{
		if(SingletonObject == null)
			SingletonObject = new EventManager();
		return SingletonObject;	
	}
	
	//List Change 
	/**
	 * Register/Add a ListChange Handler
	 * @param handler The ListChangeHandler implementing object
	 */
	public void registerListChangeHandler(ListChangeHandler handler) 
	{
		if(handler != null && !this.listChangeHandler.contains(handler))
		this.listChangeHandler.add(handler);
	}
	
	public void fireListChangeEvent(ListChangeEvent event) 
	{
		for(ListChangeHandler h: listChangeHandler)
			h.onListChange(event);
	}
	
	//Update View
	public void registerController(DefaultController handler) 
	{
		if(handler != null)
		this.updateViewHandler.add(handler);
	}
	
	/**
	 * UpdateTexts event
	 */
	public void fireUpdateTextsEvent() 
	{
		if(updateViewHandler.size() > 0) 
		{
			for(DefaultController h: updateViewHandler) 
			{
				if(h != null)
					h.updateTexts();
			}
		}
	}
	
	/**
	 * RefreshViews event
	 */
	public void fireRefreshViewsEvent() 
	{
		if(updateViewHandler.size() > 0) 
		{
			for(DefaultController h: updateViewHandler) 
			{
				if(h != null)
					h.refreshViews();
			}
		}
	}
	
	
	/**
	 * Gets fired when a new message is available in the stream 
	 */
	public void registerNewMessageHandler(NewMessageHandler handler)
	{
		if(handler != null)
		this.newMessageHandler.add(handler);
	}
	public void unRegisterNewMessageHandler(NewMessageHandler handler)
	{
		if(handler != null)
		this.newMessageHandler.remove(handler);
	}
	
	public void fireNewMessageStreamReaderThreadWorkerEvent(String message) 
	{
		for(NewMessageHandler s: newMessageHandler) 			
			s.newMessageStreamReaderThreadWorker(message);						
	}
	
	public void firenewMessageManagementInterfaceEvent(String message) 
	{
		for(NewMessageHandler s: newMessageHandler) 			
			s.newMessageManagementInterface(message);						
	}
	
	
	//ConnectionChanged 
	
	public void registerConnectionChangedHandler(ConnectionChanged c) 
	{
		this.connectionChangedHandler.add(c);
	}
	
	public void unRegisterConnectionChangedHandler(ConnectionChanged c) 
	{
		this.connectionChangedHandler.remove(c);
	}
	
	
	public void fireConnectionChangedEvent(ConnectionChangedArgs args) 
	{
		for(ConnectionChanged c: connectionChangedHandler)
		{
			if(args.equals(ConnectionChangedArgs.CONNECTED))
				c.isConnected();
			else if(args.equals(ConnectionChangedArgs.DISCONNECTED))
				c.isDisconnected();	
			else if(args.equals(ConnectionChangedArgs.CONNECTING))
				c.isConnecting();	
		}
	}
	
	
}
