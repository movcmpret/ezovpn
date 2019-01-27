package com.movcmpret.osBridge;

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
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movcmpret.application.MainView;
import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.ovpn.FileJSONAdapter;
import com.movcmpret.ovpn.OVPNConfigJSONAdapter;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.persistence.UserProfileData;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.panes.YesNoCheckboxDialogPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * Class for performing non-trivial OS-calls 
 * @author movcmpret
 * Singleton
 */
public class OSBridge
{
	GsonBuilder gsonBuilder;
	Gson gson;
	
	private static OSBridge thisInstance;
		
	private OSBridge() 	
	{
		initGson();
	}
	
	private void initGson() 
	{
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(OVPNConfig.class, new OVPNConfigJSONAdapter());
		gsonBuilder.registerTypeAdapter(File.class, new FileJSONAdapter());
		gson = gsonBuilder.create();
	}
	
	public static OSBridge getInstance() 
	{
		if ( thisInstance == null )
			thisInstance = new OSBridge();
		return thisInstance;
	}
	
	public void isAdmin() 
	{
		if(isLinux())
		{
		if( (new File("/root").canRead() && new File("/root").canWrite()) )
			return;
		}
		else if(isWindows())
		{
		    Preferences prefs = Preferences.systemRoot();
		    try{
		        prefs.put("foo", "bar"); // SecurityException on Windows
		        return;
		    	}
		    catch(Exception e){}
		}
	    
		if( UserProfile.getInstance().getUserProfileData().isShowNoRootDialog() )
		{
		Alert isRootDialog = AlertManager.createOKDoNotShowAgainAlert(AlertType.WARNING,
				Constants.getPermission_NoRoot(), Constants.getPermission_NoRootHeader());
		((YesNoCheckboxDialogPane)isRootDialog.getDialogPane()).setMinHeight(200.);
		
		isRootDialog.showAndWait();

		UserProfile.getInstance().getUserProfileData()
				.setShowNoRootDialog(!((YesNoCheckboxDialogPane) isRootDialog.getDialogPane()).isNotShowAgain());
		}
	}
	
	public void writeSession() {
		try {
			String out = gson.toJson(UserProfile.getInstance().getUserProfileData());
			new File(Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name())
					.delete();
			new File(Constants.getOSBridge_Session_File_Path()).mkdirs();
			FileWriter f = new FileWriter(
					Constants.getOSBridge_Session_File_Path() + "/" + Constants.getOSBridge_Session_File_Name());
			f.write(out);
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void exportSession(File outFile) throws Exception
	{
		
		String out = gson.toJson(UserProfile.getInstance().getUserProfileData());
		outFile.delete();
		FileWriter f = new FileWriter(outFile);
		f.write(out);
		f.close();
	}

	public void readSession() {
		new File(Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name())
				.delete();
		File session = new File(
				Constants.getOSBridge_Session_File_Path() + "/" + Constants.getOSBridge_Session_File_Name());
		if ( !session.exists() || !session.canRead() )
			return;

		try {
			BufferedReader input = new BufferedReader(new FileReader(
					Constants.getOSBridge_Session_File_Path() + "/" + Constants.getOSBridge_Session_File_Name()));
			UserProfile.getInstance().setUserProfileData(gson.fromJson(input, UserProfileData.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void importSession(File path) throws Exception
	{;
		if (!path.exists() || !path.canRead())
			throw new Exception( this.getClass().getName() + ": (importSession) Cannot read file");
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(path));
			UserProfile.getInstance().setUserProfileData(gson.fromJson(input, UserProfileData.class));
			writeSession();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeToFile(String toWrite, File file) 
	{
		try(FileWriter f = new FileWriter(file)) 
		{
			if(!file.canWrite())
				throw new Exception("Cannot write file " + file.getName());
			 f.write(toWrite);
			 f.close();
		} catch (Exception e) {
			e.printStackTrace();
			}	
	}
	
	public void createTempLoginFile() throws Exception
	{
    	FileWriter writer = new FileWriter(Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name());
		writer.write(UserProfile.getInstance().getUserProfileData().getUsername() +"\r\n");
		writer.write(UserProfile.getInstance().getUserProfileData().getPassword());
	    writer.close();
	    if(isLinux())
	    {
	    Files.setPosixFilePermissions(Paths.get(Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name()), getSecureFilePermissions());
	    }
	}
	
	public void deleteTempLoginFile() {
		new File(Constants.getOSBridge_Temp_Login_File_Path() + "/" + Constants.getOSBridge_Temp_Login_File_Name())
				.delete();
	}
	
	private Set<PosixFilePermission> getSecureFilePermissions() 
	{
	    //using PosixFilePermission to set file permissions to user only
	    Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
	    //add owners permission
	    perms.add(PosixFilePermission.OWNER_READ);
	    perms.add(PosixFilePermission.OWNER_WRITE);
	    return perms;
	}
	
	public boolean isWindows() 
	{
		return (Constants.getOS() == OSType.SOME_WINDOWS || Constants.getOS() == OSType.Windows10_x64
				|| Constants.getOS() == OSType.Windows10_x86 || Constants.getOS() == OSType.Windows7_x64
				|| Constants.getOS() == OSType.Windows7_x86);
	}
	
	public boolean isLinux() 
	{
		return (Constants.getOS() == OSType.Ubuntu_16_x64 || Constants.getOS() == OSType.Ubuntu_16_x86
				|| Constants.getOS() == OSType.Ubuntu_18_x64 || Constants.getOS() == OSType.Ubuntu_18_x86);
	}

}
