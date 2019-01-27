package com.movcmpret.persistence;

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

import com.movcmpret.dialog.consoledialog.ConsoleOutputDialog;
import com.movcmpret.ovpn.connection.OVPNConnectionInfo;

/**
 * Takes care of the Session.
 * @author movcmpret
 *
 */
public class UserProfile 
{

	private static UserProfile profile;
	private final UserProfileData data;
	private OVPNConnectionInfo connectionInfo;

	private UserProfile() 
	{
		data = new UserProfileData();
	}
	public static UserProfile getInstance() 
	{
		if( profile == null)
			profile = new UserProfile();
		return profile;
	}
	

	public void loadConfigFromFile(File file) 
	{
		// Stub. atm implemented in OSBridge	
	}
	
	public void saveConfigToFile(File path) 
	{
		// Stub. atm implemented in OSBridge
	}
	
	public void setUserProfileData(UserProfileData profileData) 
	{
		setData(profileData);
	}
	
	/**
	 *  Data-object is unique. This method copies all values to the existent 
	 *  object. Can be interpreted as copy "constructor"
	 *  TODO: Databinding!.
	 * @param profileData UserProfileData
	 */
	private void setData(UserProfileData profileData)
	{
		data.setImportManager_LastFolder(profileData.getImportManager_LastFolder());
		data.setLastConfig(profileData.getLastConfig());
		data.setOverViewTab_rawOverviewTableData(profileData.getOverViewTab_rawOverviewTableData());
		data.setLanguage(profileData.getLanguage());
		data.setUsername(profileData.getUsername());
		data.setPasswordBase64(profileData.getPassword());
		data.setPrivateKeyFilePath(profileData.getPrivateKeyFilePath());
		data.setOverviewTable(profileData.getOverviewTable());
		data.setConnectionInformationList(profileData.getConnectionInformationList());
		data.setShowExitDialog(profileData.isShowExitDialog());
		data.setImportExport_LastFolder(profileData.getImportExport_LastFolder());
		data.setShowNoRootDialog(profileData.isShowNoRootDialog());
	}
	
	public UserProfileData getUserProfileData() 
	{
		return data;
	}
	
	public OVPNConnectionInfo getConnectionInfo() {
		if( connectionInfo == null )
			setConnectionInfo( new OVPNConnectionInfo(null, new ConsoleOutputDialog()) );
		return connectionInfo;
	}
	public void setConnectionInfo(OVPNConnectionInfo obj) {
		if (connectionInfo != null )
			connectionInfo.destroy();
		connectionInfo = obj;
	}
	
}
