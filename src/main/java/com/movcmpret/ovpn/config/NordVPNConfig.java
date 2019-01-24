package com.movcmpret.ovpn.config;

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
import com.movcmpret.web.nordvpn.NordVPNInfoJson;

public class NordVPNConfig extends OVPNConfig{
	
	protected String id;
	
	protected String location;
	
	protected String domain; 
	
	protected int load;
	
	/**
	 * Constructor for an Config object with pre defined Location, Protocol and Port
	 * in case of file-independent values. 
	 *@param file A path to the specified openvpn config file 
	 *@param kLocation Location of the openvpn server e.g. DE, EN, GB etc. 
	 *@param Protocol: TCP or UDP 
	 *@param Port: Specified port, e.g. 443 
	 *
	 * Update API usage: 
	 * 
	 * 	 Enables more fields see {@code NordVPNInfoJson}
	 *   obtain config file e.g.:
	 *   https://api.nordvpn.com/files/download/de103.nordvpn.com.tcp443
	 *   
	 *   
	 */
	
	public NordVPNConfig(File file) 
	{		
		super(file);
	}
	
	public NordVPNConfig(NordVPNInfoJson json) 
	{		
		super(new File("condigs/"+json.getFileName()));
	}
	
			
	//Get/Set		
	public String getLocation() 
	{ 
		return location; 
	}
				
	public String getID() 
	{ 
		return id; 
	}
	
	public void setLocation(String location) 
	{
		this.location = location;
	}
	
	public void setID(String id) 
	{
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getLoad() {
		return load;
	}

	public void setLoad(int load) {
		this.load = load;
	}
	
	
	
}

