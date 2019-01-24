package com.movcmpret.ovpn;

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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movcmpret.ovpn.config.NordVPNConfig;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.ovpn.config.OVPNDefaultConfig;
import com.movcmpret.utility.Logger;
import com.movcmpret.web.nordvpn.NordVPNInfoJson;

/**
 * 20.09.2018 
 * 
 * Fileparser for .ovpn files to fetch metadata.
 * Singleton
 * @author movcmpret
 *
 */
public final class OVPNFileParser 
{
	private static OVPNFileParser parser;
	//Singleton 
	private OVPNFileParser(){}
	
	public static OVPNFileParser getInstance() 
	{
		if(parser == null)
		 parser = new OVPNFileParser();
		return parser;
	}
	
	public OVPNConfig parseOVPNFile(OVPNConfigType type, File configFile) throws Exception
	{
		switch(type)
		{
		case NORDVPN: return  parseNordVPNConfig(configFile);
		case DEFAULT: return  parseDefaultConfig(configFile);
		default: return parseDefaultConfig(configFile);
		}						
	}
	
	/**
	 * @deprecated Please use parseOVPNFile
	 * @param json NordVPNInfoJson (coming from ImportManagerNordVPN / WebJsonParser)
	 * @return
	 * @throws Exception
	 */
	public OVPNConfig parseNordVPNFile(NordVPNInfoJson json, File configFile) throws Exception
	{
		NordVPNConfig config = parseNordVPNConfig(configFile);
		config.setLocation(json.getCountry());
		config.setIpAddress(json.getIp_address());
		config.setDomain(json.getDomain());
		config.setLoad(json.getLoad());
		return config;
	}
	
	private NordVPNConfig parseNordVPNConfig(File configFile) throws Exception
	{
		String location;
		String id;
		
		if(configFile != null) 
		{
			List<String> fileName =Arrays.asList(configFile.getName().split("\\."));
			
			if(fileName.size() > 3) 
			{
			// Split Alphanumeric values e.g. udp1194 [udp, 1194] with  Regex: "((?<=[a-zA-Z])(?=[0-9]))|((?<=[0-9])(?=[a-zA-Z]))"
			location = (fileName.get(0).split("((?<=[a-zA-Z])(?=[0-9]))|((?<=[0-9])(?=[a-zA-Z]))")[0].toUpperCase());
			id = (fileName.get(0).split("((?<=[a-zA-Z])(?=[0-9]))|((?<=[0-9])(?=[a-zA-Z]))")[1]);				
			NordVPNConfig conf = new NordVPNConfig(configFile);
			conf.setLocation(location);
			conf.setID(id);
			//Parse anyway
			this.parseOVPNMetadata(conf);
			return conf;
			}							
		}
		throw new IOException("OVPNFileParser: Couldn't parse this Filename. Filename is undefined (null)");		

	}
	
	private OVPNConfig parseDefaultConfig(File configFile) 
	{			
		OVPNDefaultConfig conf = new OVPNDefaultConfig(configFile);
		this.parseOVPNMetadata(conf);
		return conf;
	}
	
	/**
	 * Reparse the config, if the file updated
	 * @param config
	 */
	public void reParseDefaultConfig(OVPNConfig config)
	{
		this.parseOVPNMetadata(config);
	}
	
	
	private void parseOVPNMetadata(OVPNConfig ovpnConfig) {
		
		if(ovpnConfig == null)
			return;
		
		Map<String,String> map = createMapping(ovpnConfig.getConfigFile());
		

		ovpnConfig.setIpAddress(map.get("remote").split(" ")[0]);		
		//Port
		if(map.get("port") != null )
			ovpnConfig.setPort(map.get("port"));
		else
			ovpnConfig.setPort(map.get("remote").split(" ")[1]);

		//Protocol
		if(map.get("proto") != null )
			ovpnConfig.setProtocol(map.get("proto").toUpperCase());
		else
			ovpnConfig.setProtocol(map.get("remote").split(" ")[2].toUpperCase());

	}
	
	/**
	 * Used for Configfile Parsing
	 * @param filePath
	 * @return
	 */
	private Map<String,String> createMapping(File filePath)
	{
		Map<String,String> map = null;
		if( !filePath.exists() || !filePath.canRead()) 
		{
			Logger.LogError(this.getClass().toString() + ": Can't access or read file " + filePath.getAbsolutePath());
			return map;
		}
		
		//prepare map 
		map = new HashMap<String,String>();
		
		//Autoclose interface
		try(BufferedReader input = new BufferedReader( new FileReader(filePath))) {
			while(input.ready())
			{
				// Read the input file line by line and create a mapping for each key-value pair.
				// SPACE is the delimiter
				String line = input.readLine();
				if( OVPN_TAGS.contains(line) )
				{	
					String token = line.replaceAll(" ", "").replaceAll("<", "").replaceAll(">", "");
					String content ="";
					line = input.readLine();
					while( !OVPN_TAGS.contains(line) )
					{
						content+=line+"\n";
						line = input.readLine();
					}
				 map.put(token.toLowerCase(), content);
				 line ="";
				}
				if(line.contains(" ") && (line.indexOf(" ") + 1) < line.length() )
				{
					map.put(line.substring(0,line.indexOf(" ")), line.substring(line.indexOf(" ")+1));
				}
				
				if( !line.contains(" ") )
				{
					map.put(line , "true" );
				}
			}
		} 
		catch (IOException e) {
			Logger.LogError("An error occured while reading file: " + filePath.getAbsolutePath());
			e.printStackTrace();
		}				
		return map;
	}
	// XML Tags 
	public static final List<String> OVPN_TAGS =  Arrays.asList(
			"<ca>", 
			"<CA>",
			"<cA>", 
			"<Ca>", 
			"</ca>", 
			"</CA>",
			"</cA>", 
			"</Ca>", 
			"<tls-auth>", 
			"<TLS-AUTH>", 
			"<TLS-auth>", 
			"</tls-auth>", 
			"</TLS-AUTH>", 
			"</TLS-auth>"
			);
}
