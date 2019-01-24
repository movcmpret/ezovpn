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



import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.movcmpret.constants.Constants;

/**
 * Connects to webinterfaces and parses JSON Strings and Files 
 * @author movcmpret
 *
 */

public class WebJsonParser 
{	
	private final static int CONNECTION_TIMEOUT = 15000;
	private final static String NORD_VPN_SERVER_INFO_URL = "https://nordvpn.com/api/server";
	// + (domain).(protocol)(port) e.g.  de103.nordvpn.com.tcp443
	private final static String NORD_VPN_GET_CONFIG_FILE_URL ="https://api.nordvpn.com/files/download/";
	private final static String NORD_VPN_LOAD_INFO ="https://nordvpn.com/api/server/stats";
	
	
	private static WebJsonParser thisInstance = null;

	public static WebJsonParser getInstance() 
	{
		if(thisInstance == null)
			thisInstance = new WebJsonParser();	
		return thisInstance;
	}
	
	public List<NordVPNInfoJson> getNordVPNServerInfo() throws Exception
	{
		List<NordVPNInfoJson> list = new ArrayList<NordVPNInfoJson>();
		
		String json = readUrl(NORD_VPN_SERVER_INFO_URL);
		
		 Gson gson = new Gson();        
		 list = Arrays.asList(gson.fromJson(json, NordVPNInfoJson[].class));
		 list.sort(new Comparator<NordVPNInfoJson>() 
		 {
			@Override
			public int compare(NordVPNInfoJson arg0, NordVPNInfoJson arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
			 
		 });
 		 return list;
	} 
	
	/**
	 * Returns one TCP and UDP config, if available
	 * TODO: Remove this code doubling! 
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public List<File> getConfigFileForNordVPNInfoJson(NordVPNInfoJson info) throws Exception
	{
		List<File> configFiles = new ArrayList<File>();
		if(info.getFeatures().openvpn_tcp)
		{
		URL url = new URL(NORD_VPN_GET_CONFIG_FILE_URL + info.getDomain()+ ".tcp443");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setConnectTimeout(CONNECTION_TIMEOUT);
    	con.setReadTimeout(CONNECTION_TIMEOUT);
    	con.addRequestProperty("User-Agent", "Mozilla/4.76");
    	File config = new File(Constants.getDownloaded_Configfile_Dir());
    	
    	if(!config.exists())
    	config.mkdirs();
    	
    	config = new File(Constants.getDownloaded_Configfile_Dir()+"/" + info.getDomain()+".tcp.ovpn");
    	Files.copy(con.getInputStream(), Paths.get(config.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);	
    	con.getInputStream().close();	
    	configFiles.add(config);
		}
		if(info.getFeatures().openvpn_udp)
		{
		URL url = new URL(NORD_VPN_GET_CONFIG_FILE_URL + info.getDomain()+ ".udp1194");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setConnectTimeout(CONNECTION_TIMEOUT);
	    con.setReadTimeout(CONNECTION_TIMEOUT);
	    con.addRequestProperty("User-Agent", "Mozilla/4.76");
	    File config = new File(Constants.getDownloaded_Configfile_Dir());
	    	
	    if(!config.exists())
	    config.mkdirs();
	    	
	    config = new File(Constants.getDownloaded_Configfile_Dir()+"/" + info.getDomain()+".udp.ovpn");
	    Files.copy(con.getInputStream(), Paths.get(config.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);	
	    con.getInputStream().close();			
    	configFiles.add(config);
		}
    	return configFiles;
	}
	
	
	private String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	    	
	    	URL url = new URL(urlString);
	    	URLConnection con = url.openConnection();
	    	con.setConnectTimeout(CONNECTION_TIMEOUT);
	    	con.setReadTimeout(CONNECTION_TIMEOUT);
	    	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}
	
	// Load info via https://nordvpn.com/api/server/stats
	public HashMap<String, Integer>getNordVPNLoadInfo() throws Exception
	{	
		HashMap<String, Integer> map = new HashMap<String, Integer>(); 
		String json;
			json = readUrl(NORD_VPN_LOAD_INFO);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(json);
			JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject
			Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object
			for(Map.Entry<String, JsonElement> entry : entries)
			{	
				if(!entry.getValue().isJsonNull())
				{
				map.put(entry.getKey(), Integer.valueOf(entry.getValue().getAsJsonObject().get("percent").toString()));
				}
			}			
 		 return map;
	}
}
