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


/**
 * Abstract class that describes an OVPNConfig.
 * @author movcmpret
 *
 */
public abstract class OVPNConfig {
		
	protected String protocol;
	protected String device;
	protected String port;
	protected String ipAddress;
	protected String resolv_retry;
	protected boolean remote_random = false;
	protected boolean no_bind = false;
	protected int tun_mtu = -1;
	protected int tun_mtu_extra = -1;
	protected int mssfix = -1;
	protected boolean persist_key = false;
	protected boolean persist_tun = false;
	protected int ping_restart = -1;
	protected boolean ping_timer_rem = false;
	protected int reneg_sec = -1;
	protected String ping;
	
	
	protected String remote_cert_tls;
	protected boolean auth_user_pass = false;

	protected boolean comp_lzo = false;
	protected int verb = -1;
	protected boolean fast_io = false;
	protected String cipher;
	protected String auth;
	
	protected String CA_Cert;
	protected String tls_auth;
	
	//Filepath
	protected File configFile;
	
	/**
	 * Manual constructor for OpenVPN Config files - No fileparsing included
	 * @param file Filepath to .ovpn file
	 * @param kProtocol TCP/UDP
	 * @param kPort Port
	 */
   
	public OVPNConfig(File file, String kProtocol, String kPort) 
	{
	configFile = file;
	protocol = kProtocol;
	port = kPort;	
	ipAddress = "";
	ping = "";
	
	}
	
	public OVPNConfig(File file, String kProtocol, String kPort, String kIP) 
	{
	this(file, kProtocol, kPort);
	ipAddress = kIP;
	}
	
	public OVPNConfig(File file) 
	{
		this(file, "", "");
	}
	
	public String getIpAddress() 
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public void setPort(String port) 
	{
		this.port = port;
	}

	public void setPing(String ping) 
	{
		this.ping = ping;
	}

		//Get/Set
		public File getConfigFile() 
		{	
			return configFile;
		}
		
		public String getIPAddress() 
		{ 
			return ipAddress; 
		}
		
		public String getPing() 
		{ 
			return ping; 
		}	
		
		public String getProtocol()
		{ 
			return protocol; 
		}
		
		public String getPort() 
		{ 
			return port; 
		}
						
		public String getFileName() 
		{
			 return this.configFile.getName();
		}

		
	/**
	 * TODO Ping to a specific IP-Address
	 */
	protected void performPing() 
	{
		//STUB
	}
	
	/**
	 * Absolute filepath is an unique identifier. 
	 * 
	 */
	@Override
	public boolean equals(Object o) 
	{
		if(!(o instanceof OVPNConfig))
			return false;
		return ((OVPNConfig)o).getConfigFile().getAbsolutePath().equals(this.configFile.getAbsolutePath());
	}
}
