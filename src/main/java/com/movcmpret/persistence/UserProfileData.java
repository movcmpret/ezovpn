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



import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.movcmpret.constants.Constants;
import com.movcmpret.constants.Language;
import com.movcmpret.history.ConnectionHistoryElement;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.ovpn.tables.DefaultOVPNTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains all necessary session data.
 * @author movcmpret
 *
 */
public class UserProfileData {

	private  String ImportManager_LastFolder;
	private  String ImportExport_LastFolder;
	private  List<OVPNConfig> OverViewTab_rawOverviewTableData;
	private  OVPNConfig lastConfig = null;
	private  Language language;
	//The type of the table (NordVPNTable, DefaultOVPNTable...) see ovpn.tables
	private  String overviewTable;
	private List<ConnectionHistoryElement> connectionInformationList;	
	//Used to wrap connectionInformationList ( for HistoryTab ). Transient: no GSON serialization
	private transient ObservableList<ConnectionHistoryElement> obervableConnectionInformationList;
	
	
	//Flags 
		//Show the exit Dialog
		private boolean showExitDialog = true;
	
	// in ms
	private  long timeout;
	
	//TODO Put this stuff into some kind of better crypto shizzle 
	private  String password ="";
	private  String username ="";
	private  String privateKeyFilePath="";


	public UserProfileData() 
	{
		//Set constants as default values if possible
		ImportManager_LastFolder = Constants.getImportManager_filePathTextField();
		ImportExport_LastFolder = Constants.getImportManager_filePathTextField();
		OverViewTab_rawOverviewTableData = new ArrayList<OVPNConfig>();
		language = Language.ENGLISH;
		timeout = 15000;
		overviewTable = DefaultOVPNTable.class.getName();
		connectionInformationList = new ArrayList<ConnectionHistoryElement>();
		
		obervableConnectionInformationList = FXCollections.observableList(connectionInformationList);
	}

	public String getImportManager_LastFolder() {
		return ImportManager_LastFolder;
	}

	public void setImportManager_LastFolder(String importManager_LastFolder) {
		ImportManager_LastFolder = importManager_LastFolder;
	}

	public String getImportExport_LastFolder() {
		return ImportExport_LastFolder;
	}

	public void setImportExport_LastFolder(String importExport_LastFolder) {
		ImportExport_LastFolder = importExport_LastFolder;
	}

	public List<OVPNConfig> getOverViewTab_rawOverviewTableData() {
		return OverViewTab_rawOverviewTableData;
	}

	public void setOverViewTab_rawOverviewTableData(List<OVPNConfig> overViewTab_rawOverviewTableData) {
		OverViewTab_rawOverviewTableData = overViewTab_rawOverviewTableData;
	}

	public OVPNConfig getLastConfig() {
		return lastConfig;
	}

	public void setLastConfig(OVPNConfig lastConfig) {
		this.lastConfig = lastConfig;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
		
	public String getPassword() {
		if(!this.password.isEmpty())
		return new String( Base64.getDecoder().decode(Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8) ;
 		return "";
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}

	public void setPasswordBase64(String password) {
		//double encryption (I know this is not accurate, but it helps to believe! )
		byte[] encoded = Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8));
		encoded = Base64.getEncoder().encode(encoded);
		
		this.password = new String(encoded,StandardCharsets.UTF_8 );
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPrivateKeyFilePath() {
		return privateKeyFilePath;
	}

	public void setPrivateKeyFilePath(String privateKeyFilePath) {
		this.privateKeyFilePath = privateKeyFilePath;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getOverviewTable() {
		return overviewTable;
	}

	public void setOverviewTable(String overviewTable) {
		this.overviewTable = overviewTable;
	}

	public ObservableList<ConnectionHistoryElement> getObservableConnectionInformationList() {
		return obervableConnectionInformationList;
	}
	
	@Deprecated
	/**
	 * Use getObservableConnectionInformationList ! 
	 * @return
	 */
	public List<ConnectionHistoryElement> getConnectionInformationList() {
		return connectionInformationList;
	}
	
	public void setConnectionInformationList(List<ConnectionHistoryElement> connectionInformationList) {
		this.connectionInformationList = connectionInformationList;
		this.obervableConnectionInformationList = FXCollections.observableList(connectionInformationList);
	}

	public boolean isShowExitDialog() {
		return showExitDialog;
	}

	public void setShowExitDialog(boolean showExitDialog) {
		this.showExitDialog = showExitDialog;
	}
	

}
