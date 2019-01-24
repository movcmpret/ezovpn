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



import java.util.ArrayList;
import java.util.Date;

public class Logger 
{

	public static boolean writeLogFile  = true;
	public static boolean printToStdOut = true;
	public static ArrayList <String> logs  = new ArrayList<String>() 
	{
		@Override
		public boolean add(String s) {
			if(printToStdOut)
			System.out.println(s);
			return super.add(s);
			
		}
	};
	
	public static void LogError(String logMessage) 
	{	
		Log( logMessage ,"ERROR: ");				
	}
	
	public static void LogInfo(String logMessage) 
	{		
		Log( logMessage ,"INFO: ");
	}
	
	public static void LogDebug(String logMessage) 
	{		
		Log( logMessage ,"DEBUG: ");
	}
	
	public static void LogDebug(String[] logMessages) 
	{	Log( "","DEBUG: ");
		for(String logMessage:logMessages)
		Log(logMessage);
	}
	
	public static <E> void LogDebug(E[] logMessages) 
	{	Log( "","DEBUG: ");
		for(E logMessage:logMessages)
		Log(logMessage.toString());
	}
	
	
	private static void	Log(String logMessage, String logType)
	{
		logs.add( logType + ": " +  new Date().toString() + "     " + logMessage);
	}
	
	private static void	Log(String logMessage)
	{
		logs.add(logMessage);
	}
	
	
	
}
