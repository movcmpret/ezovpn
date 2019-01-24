package com.movcmpret.web.nordvpn;

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



import com.google.gson.annotations.SerializedName;

/**
 * Used to save information from https://nordvpn.com/api/server
 * @author movcmpret
 *
 */

public class NordVPNInfoJson
{
	
	private String name;	
	private int id;
	private String ip_address;
	private String domain;
	private String flag;
	private String country;
	private Location location;
	private Features features;
	private int load;
	
	
	private class Location
	{
		@SerializedName("lat")
		public String latitude;
		
		@SerializedName("long")
		public String longitude;
	}
	
	public class Features
	{
		public boolean openvpn_udp;
		
		public boolean openvpn_tcp;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getIp_address() {
		return ip_address;
	}


	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}


	public String getDomain() {
		return domain;
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public int getLoad() {
		return load;
	}


	public void setLoad(int load) {
		this.load = load;
	}
	
	
	public Features getFeatures() {
		return features;
	}


	public void setFeatures(Features features) {
		this.features = features;
	}


	public String getFileName() 
	{
		return this.domain;
	}
	
	@Override
	public String toString() 
	{
		return this.name;		
	}

	
	
}
