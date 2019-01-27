package com.movcmpret.constants;

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




import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.movcmpret.event.EventManager;
import com.movcmpret.osBridge.OSBridge;
import com.movcmpret.osBridge.OSType;
import com.movcmpret.utility.Logger;

public class Constants {
	
	private static Language activeLanguage  = Language.ENGLISH; 
	private static OSType OSType = null;

/***
 * 
 * Maven
 * 
 * */	
	private final static String Maven_Properties_File = "project.properties";
	private final static Properties Maven_Properties = new Properties();
	
	private static String Maven_ProjectName="n.a.";
	private static String Maven_GroupID="n.a.";
	private static String Maven_ProjectArtifactID="n.a.";
	private static String Maven_ProjectURL="n.a.";
	private static String Maven_ProjectVersion="n.a.";
	
	private static final void parseMavenProperties() 
	{
		try 
		{
			Maven_Properties.load(Constants.class.getResourceAsStream(Maven_Properties_File));
			Maven_ProjectName = Maven_Properties.getProperty("name");
			Maven_GroupID = Maven_Properties.getProperty("groupId");
			Maven_ProjectArtifactID = Maven_Properties.getProperty("artifactId");
			Maven_ProjectURL = Maven_Properties.getProperty("url");
			Maven_ProjectVersion = Maven_Properties.getProperty("version");
		} 
		catch (IOException e) 
		{
			Logger.LogError(Constants.class.getName() + ": Error while parsing Maven properties file: \""
					+ Maven_Properties_File + "\"");
		}
				
	}
	
/***
 * 
 * Language-dependent String definitions 
 * 
 * */
	
	/** 
	 * COMMON  /  GENERAL / INTERNAL 
	 */
		
	//DateFormatter
	
	private static DateTimeFormatter DE_DateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");
	private static DateTimeFormatter EN_DateFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");

	public final static PeriodFormatter TIME_FORMATTER = new PeriodFormatterBuilder()
			.printZeroAlways()
			.appendHours()
			.appendSeparator(":")
			.appendMinutes()
			.appendSeparator(":")
			.appendSeconds()
			.toFormatter();
		// Filepath
	
	private final static String OSBridge_Session_File_Name = "ezovpn.session";
	private final static String OSBridge_Temp_Login_File_Name = "info.temp";
	private static final String OSBridge_Session_File_Path = System. getProperty("user.home") + "/ezOVPNClient";
	private static final String OSBridge_Temp_Login_File_Path = System. getProperty("user.home") + "/ezOVPNClient";
	private final static String Downloaded_Configfile_Dir = System. getProperty("user.home") + "/ezOVPNClient/configs";
	
		//FXML
		private static String FXML_MainSceneFilename = "MainScene.fxml";
		private static String FXML_ImportManagerFilename = "ImportManager.fxml";
		private static String FXML_TabOverviewFilename = "tabOverview.fxml";
		private static String FXML_CredentialInputFilename = "CredentialInput.fxml";
		private static String FXML_EditConfiglInputFilename = "editConfig.fxml";
		private static String FXML_ImportManagerNordVPNFilename = "ImportManagerNordVPN.fxml";
		private static String FXML_TabNetorkingFilename = "TabNetworking.fxml";
		private static String FXML_TabHistoryFilename = "TabHistory.fxml";
		
		
		//Window Properties
		private static String Window_Name = "ezOVPN Client";
		
		// Protocols
		private static String Protocol_TCP = "TCP";
		private static String Protocol_UDP = "UDP";
		
		//Misc
		private static String Port = "Port";
		
		//ImportManager
		private static String ImportManager_Title = "Import Manager";
		private static String ImportManager_filePathTextField =  System. getProperty("user.home");
		private static String ImportManager_openDirectoryBrowserButton = "...";
		
		private static String ImportManagerNordVPN_Title = "Import Manager (NordVPN)";
	
		//Menu
		private static String MenuItemSetKeys="Private/Publickey";
		private static String MenuItemExportProfile="Export";
		private static String MenuItemImportProfile="Import";
		
		/**
//		 * 	GERMAN
		 */
		//Main Window
		
			//General 
			private static String DE_File = "Datei";
			private static String DE_Edit = "Bearbeiten";
			private static String DE_Delete ="Enfernen";
			private static String DE_Import = "Importieren...";
			private static String DE_ChangeLanguage = "Sprache";
			private static String DE_Help = "Hilfe";
			private static String DE_German ="Deutsch";
			private static String DE_English ="Englisch";
			private static String DE_FileName = "Dateiname";
			private static String DE_EditConfigFile = "Config bearbeiten";
			private static String DE_LanguageChangedTo = "Die Sprache wurde erfolgreich auf %s umgestellt.";
			private static String DE_MenuProfile="Profil";
			private static String DE_Removed ="Entfernt";
			private static String DE_Cancel ="Abbrechen";
			private static String DE_History ="Historie";		
			private static String DE_Download = "Herunterladen";
			private static String DE_Load = "Auslastung";
			private static String DE_About = "Über";
			private static String DE_Received = "Empfangen";
			private static String DE_Transmitted = "Gesendet";
			private static String DE_Add = "Hinzufügen";
			private static String DE_Date = "Datum";
			private static String DE_Name = "Name";
			private static String DE_Time = "Zeit";
			
			
			//TabPane
			private static String DE_Overview = "Übersicht";
			private static String DE_Network ="Netzwerk";
			private static String DE_Settings = "Einstellungen";
			
			//Table
			private static String DE_Location = "Standort";
			private static String DE_Protocol = "Protokoll";
			private static String DE_NoDataLoaded = "Keine Daten geladen.";
				
		
			//Menubar			
			private static String DE_MenuItemSetUserPass="Logindaten";
			private static String DE_MenuItem_Edit_NoConfigSelectedAlert = "Kein Element ausgewählt";
			private static String DE_MenuItemImportFromNordVPNServer = "Von NordVPN Server";
			private static String DE_MenuItemImportFromFile = "Aus Datei(en)";
			private static String DE_MenuItem_Import_Failed = "Beim Import ist ein Fehler aufgetreten.";
			private static String DE_MenuItem_Export_Failed = "Beim Export ist ein Fehler aufgetreten.";
			private static String DE_MenuItem_Import_Warning = "Ein Import benötigt einen Neustart. Alle Verbindungen werden dabei geschlossen. Fortfahren?";
			private static String DE_MenuItem_OverviewClear = "Übersicht-Tabelle leeren";
			private static String DE_MenuItem_HistoryClear = "Historie leeren";
			//Button
			private static String DE_Connect = "Verbinden";
		
		//ImportManager
			private static String DE_ImportManager_parseMetadataCheckBox = "NordVPN Parsing";
			private static String DE_ImportManager_selectAllCheckBox = "Alle auswählen";
			private static String DE_ImportManager_importButton = "Importieren";
			private static String DE_ImportManager_noFilesLoaded = "Keine Dateien geladen";
			private static String DE_ImportManager_noCompatibleFilesFound = "Keine .ovpn Dateien gefunden";
			private static String DE_ImportManager_Number ="Nr."; 
			private static String DE_clearAndAddRadioButton = "Sauberes Hinzufügen";
			private static String DE_addRadioButton = "Hinzufügen";
			private static String DE_noConfigsSelected = "Es wurden keine Konfigurationen ausgewählt";			
			
		//ImportManager NordVPN
			private static String DE_ImportManagerNordVPN_NoConfigsLoaded ="Keine Konfigurationen geladen";
			private static String DE_Exception_ImportManagerNordVPN_ErrorWithNordVPN ="Verbindung mit den NordVPN Servern fehlgeschlagen oder Parsingfehler.";
			
		// OSBridge	
			private static String DE_OSBridge_VPNConnectionError = "Fehler beim Verbinden mit diesem Server. Hast du preconditions.sh als Root ausgeführt?";
			private static String DE_OSBridge_VPNConnectionError_Windows = "Fehler beim Verbinden mit diesem Server. Ist OpenVPN installiert?";
			private static String DE_OSBridge_VPNConnectionErrorHeader ="VPN Verbindungsfehler";
			private static String DE_OSBridge_CheckLoginData = "Logindaten falsch oder nicht vorhanden. Überprüfe deine Daten im Profil - Logindaten Menü.";
			
			//Exception Messages			
			private static String DE_Exception_NoReadPermissions = "Zugriff verweigert :-( . Hast du Leserechte?";
			private static String DE_Exception_NotExist = "Datei/Ordner nicht gefunden :-( .  Hast du Leserechte?";
			
			//Information Alert Messages
			private static String DE_InformationAlert_ConfigsSuccessfullyAdded = "Die Konfigurationen wurden erfolgreich hinzugefügt.";
			private static String DE_InformationAlert_ConfigsWithErrorsAdded = "Die Konfigurationen wurden hinzugefügt. Dabei sind allerdings %s Fehler aufgetreten.";	
			private static String DE_InformationAlert_ConfigChangesSaved = "Änderungen gespeichert in ";		
			
		//OutputDialog 
			
			private static String DE_ConsoleOutputDialog_TopLabelText = "OpenVPN Ausgabe:";
			private static String DE_ConsoleOutputDialog_Title = "Konsole";
			private static String DE_ConsoleOutputDialog_Timeout = "Zeitüberschreitung - Siehe log.";
			private static String DE_ConsoleOutputDialog_Error= "Ein Fehler ist aufgetrete - Siehe log.";
			private static String DE_ConsoleOutputDialog_Disconnected = "Verbindung mit %s getrennt.";
			private static String DE_ConsoleOutputDialog_Connected = "Verbunden mit %s.";
		
			//Misc
			private static String DE_Permission_NoRootHeader = "Keine root Rechte";
			private static String DE_Permission_NoRoot = "Es scheint, als hättest du keine root Rechte. Bitte sorge dafür, dass dieser Benutzer alle benötigten Berechtigungen hat. Andernfalls könnte diese Applikation nicht korrekt funktionieren. Du könntest das proconditions Script ausführen.";		
			private static String DE_AlertMessage_SaveChanges = "Möchtest du die Änderungen wirklich speichern?";
			private static String DE_AlertMessage_DiscardChanges="Möchtest du die Änderungen wirklich verwerfen?";
			
		//Credential Dialog
			private static String DE_CredentialDialog_Username = "Benutzername";
			private static String DE_CredentialDialog_Password = "Passwort";
			private static String DE_CredentialDialog_PasswordSaved = "(Gespeichert)";
			private static String DE_CredentialDialog_Title = "Logindaten";
		
		//Connection History 
			private static String DE_ConnectionHistory_LastConnection = "Letzte Verbindung:";
			private static String DE_ConnectionHistory_ConnectedTo = "Konfiguration: ";
			private static String DE_ConnectionHistory_ConnectedSince = "Dauer: ";
			private static String DE_ConnectionHistory_ConnectedAt = "Verbindung aufgebaut: ";
			private static String DE_ConnectionHistory_BytesSent = "Gesendete Bytes:";
			private static String DE_ConnectionHistory_BytesReceived = "Empfangene Bytes";
			
		//YesNoCheckboxDialogPane 
			private static String DE_YesNoCheckboxDialogPane_ExitDialogText = "Möchstest du das Programm wirklich beenden?";
			private static String DE_YesNoCheckboxDialogPane_DoNotShowAgain = "Diese Meldung nicht mehr anzeigen";
			private static String DE_YesNoCheckboxDialogPane_Exit = "Beenden";
			/**
		 * 	English
		 */
		//Main Window
			
			//General 			
			private static String EN_File = "File";
			private static String EN_Edit = "Edit";
			private static String EN_Delete ="Delete";
			private static String EN_Import = "Import...";
			private static String EN_ChangeLanguage = "Language";
			private static String EN_Help = "Help";
			private static String EN_German = "German";
			private static String EN_English = "English";
			private static String EN_FileName = "Filename";
			private static String EN_EditConfigFile = "Edit config file";
			private static String EN_LanguageChangedTo = "The language had successfully been set to %s.";
			private static String EN_MenuProfile="Profile";
			private static String EN_Removed ="removed";
			private static String EN_Cancel ="Cancel";
			private static String EN_History ="History";
			private static String EN_Download = "Download";
			private static String EN_Load = "Load";
			private static String EN_About= "About";
			private static String EN_Received = "Received";
			private static String EN_Transmitted = "Transmitted";
			private static String EN_Add = "Add";
			private static String EN_Date = "Date";
			private static String EN_Name = "Name";
			private static String EN_Time = "Time";
			
			
			//TabPane
			private static String EN_Overview = "Overview";	
			private static String EN_Network ="Network";
			private static String EN_Settings = "Settings";
		
			//Table
			private static String EN_Location = "Location";
			private static String EN_Protocol = "Protocol";
			private static String EN_NoDataLoaded = "No data loaded";
			
			//Menubar
			private static String EN_MenuItemSetUserPass="Credentials";
			private static String EN_MenuItem_Edit_NoConfigSelectedAlert = "No element selected";
			private static String EN_MenuItemImportFromNordVPNServer = "From NordVPN Server";
			private static String EN_MenuItemImportFromFile = "From File(s)";
			private static String EN_MenuItem_Import_Failed = "An error occured while importing.";
			private static String EN_MenuItem_Export_Failed = "An error occured while exporting.";
			private static String EN_MenuItem_Import_Warning = "An import requires a restart. All connections will be closed. Are you sure to continue?";
			private static String EN_MenuItem_OverviewClear = "Clear Overview Table";
			private static String EN_MenuItem_HistoryClear = "Clear History";
			
			//Button
			private static String EN_Connect = "Connect";
		
		//Import Manager
			private static String EN_ImportManager_parseMetadataCheckBox = "Parse NordVPN";
			private static String EN_ImportManager_selectAllCheckBox = "Select all files";
			private static String EN_ImportManager_importButton = "Import";
			private static String EN_ImportManager_noFilesLoaded = "No files loaded";
			private static String EN_ImportManager_noCompatibleFilesFound = "No .ovpn files found";
			private static String EN_ImportManager_Number = "No."; 
			private static String EN_clearAndAddRadioButton = "Clean Addition";
			private static String EN_addRadioButton = "Add";
			private static String EN_noConfigsSelected = "No configs selected";	
			
		//ImportManager NordVPN
			private static String EN_ImportManagerNordVPN_NoConfigsLoaded ="No configs loaded";
			private static String EN_Exception_ImportManagerNordVPN_ErrorWithNordVPN ="An error occured while connecting to NordVPN servers or the parsing failed.";
			
		// OSBridge	
			private static String EN_OSBridge_VPNConnectionError = "Error while connecting to the specified server. Did you execute preconditions.sh as root?";
			private static String EN_OSBridge_VPNConnectionError_Windows = "Error while connecting to the specified Server. Ist OpenVPN installed?";
			private static String EN_OSBridge_VPNConnectionErrorHeader ="VPN connection error";
			private static String EN_OSBridge_CheckLoginData = "Login credentials are wrong or not existent. Please check you login credentials im the Profile - Credentials Menu.";
		//Exception Messages			
			
			private static String EN_Exception_NoReadPermissions = "Access denied :-( .\nDo you have read permissions? ";
			private static String EN_Exception_NotExist = "File/Directory not found :-( .\nDoes it exist? \nDo you have read permissions? ";
			
		//Information Alert Messages
			private static String EN_InformationAlert_ConfigsSuccessfullyAdded = "The configs have successfully been added to the list.";
			private static String EN_InformationAlert_ConfigsWithErrorsAdded = "The configs have been added to the list.\nHowever there occured %s errors.";			
			private static String EN_InformationAlert_ConfigChangesSaved = "Changes saved in";						
			
		//OutputDialog 
			
			private static String EN_ConsoleOutputDialog_TopLabelText = "OpenVPN Output:";
			private static String EN_ConsoleOutputDialog_Title = "Console Output";
			private static String EN_ConsoleOutputDialog_Timeout = "Timeout - Have a look at the log.";
			private static String EN_ConsoleOutputDialog_Error= "An Error occured - Have a look at the log.";
			private static String EN_ConsoleOutputDialog_Disconnected = "Disconnected from %s.";
			private static String EN_ConsoleOutputDialog_Connected = "Connected to %s.";
			
		//Credential Dialog
			private static String EN_CredentialDialog_Username = "Username";
			private static String EN_CredentialDialog_Password = "Password";
			private static String EN_CredentialDialog_PasswordSaved = "(Saved)";	
			private static String EN_CredentialDialog_Title = "Credentials";
			
		//Misc
			private static String EN_Permission_NoRootHeader = "No root permissions";
			private static String EN_Permission_NoRoot = "It seems like you don't have root permissions. Please make sure to run this application as root or set the correct permissions for the current user. You can execute the precondition script.";			
			private static String EN_AlertMessage_SaveChanges = "Do you really want to save the changes?";
			private static String EN_AlertMessage_DiscardChanges= "Do you really want to discard the changes?";
			
		//Connection History 
			private static String EN_ConnectionHistory_LastConnection = "Last connection";
			private static String EN_ConnectionHistory_ConnectedTo = "Config: ";
			private static String EN_ConnectionHistory_ConnectedSince = "Duration: ";
			private static String EN_ConnectionHistory_ConnectedAt = "Connection established: ";
			private static String EN_ConnectionHistory_BytesSent = "Sent bytes: ";
			private static String EN_ConnectionHistory_BytesReceived = "Received bytes: ";
			
		//YesNoCheckboxDialogPane 
			private static String EN_YesNoCheckboxDialogPane_ExitDialogText = "Do you really want to quit?";
			private static String EN_YesNoCheckboxDialogPane_DoNotShowAgain = "Don't show this again";
			private static String EN_YesNoCheckboxDialogPane_Exit = "Quit";
			
/***
 * 
 * Getters and Setters
 * 
 * */
		//Set Active Language
		public static boolean SetActiveLanguage(Language lang) 
		{
			parseMavenProperties();
			determineOS();
			if(activeLanguage != lang) 
			{
			Logger.LogInfo("Changing Language to " + lang.toString());
			activeLanguage = lang;
			
			//Set Java default
			if(lang == Language.ENGLISH)
				Locale.setDefault(Locale.ENGLISH);
			else if(lang == Language.GERMAN)
				Locale.setDefault(Locale.GERMAN);
			
			// Fire update Texts in every controller
			EventManager.getInstance().fireUpdateTextsEvent();
			return true;
			}
			return false;
		}
		
		private static void determineOS() 
		{
			String os = System.getProperty("os.name"); 
			String arch = System.getProperty("os.arch");
			
			//Windows 10
			if(os.matches(".*Windows 10.*") || os.matches(".*windows 10.*"))
			{
				if(arch.contains("64"))
					OSType = OSType.Windows10_x64;
				else
					OSType = OSType.Windows10_x86;				
			}
			//Windows 7
			else if(os.matches(".*Windows 7.*") || os.matches(".*windows 7.*"))
			{
				if(arch.contains("64"))
					OSType = OSType.Windows7_x64;
				else
					OSType = OSType.Windows7_x86;				
			}
			//Ubuntu 16
			else if(os.matches(".*Ubuntu.*16.*") || os.matches(".*ubuntu.*16.*"))
			{
				if(arch.contains("64"))
					OSType = OSType.Ubuntu_16_x64;
				else
					OSType = OSType.Ubuntu_16_x86;	
			}
			//Ubuntu 18
			else if(os.matches(".*Ubuntu.*18.*") || os.matches(".*ubuntu.*18.*"))
			{
				if(arch.contains("64"))
					OSType = OSType.Ubuntu_18_x64;
				else
					OSType = OSType.Ubuntu_18_x86;	
			}
			else if(os.matches(".*Windows*") || os.matches(".*windows.*"))
			{
				Logger.LogError(Constants.class.getName() + ": Cannot determine OS. Using a linux distribution");
				OSType = OSType.SOME_WINDOWS;
			}
			else
			{
				Logger.LogError(Constants.class.getName() + ": Cannot determine OS. Using a linux distribution");
				OSType = OSType.Ubuntu_16_x86;
			}
			
			Logger.LogInfo(Constants.class.getName() + ": Setting OS to: " + OSType.toString());
			
			
		}
		
		public static OSType getOS() 
		{
			return OSType;
		}
		
		/**
		 * 	Maven
		 */
		public static String getMaven_ProjectName() {
			return Maven_ProjectName;
		}


		public static String getMaven_GroupID() {
			return Maven_GroupID;
		}


		public static String getMaven_ProjectArtifactID() {
			return Maven_ProjectArtifactID;
		}


		public static String getMaven_ProjectURL() {
			return Maven_ProjectURL;
		}


		public static String getMaven_ProjectVersion() {
			return Maven_ProjectVersion;
		}
		
		/**
		 * 	Common
		 */
		
		public static Language getActiveLanguage() 
		{
		return activeLanguage;	
		}
		
		
		public static String getFXML_MainSceneFilename() 
		{
			return FXML_MainSceneFilename;
		}
		
		public static String getFXML_ImportManagerFilename() 
		{
			return FXML_ImportManagerFilename;
		}
		
		public static String getFXML_TabOverviewFilename()
		{
			return FXML_TabOverviewFilename;
		}
		
		public static String getFXML_CredentialInputFilename()
		{
			return FXML_CredentialInputFilename;
		}
		
		public static String getFXML_EditConfiglInputFilename() 
		{
			return FXML_EditConfiglInputFilename;
		}
		
		public static String getFXML_ImportManagerNordVPNFilename () 
		{
			return FXML_ImportManagerNordVPNFilename;
		}
		
		public static String getFXML_TabNetworkingFilename () 
		{
			return FXML_TabNetorkingFilename;
		}
		
		public static String getFXML_TabHistoryFilename () 
		{
			return FXML_TabHistoryFilename;
		}		
		
		public static String getWindow_Name() 
		{
			return Window_Name;
		}
		
		public static String getProtocol_TCP() 
		{
			return Protocol_TCP;
		}
		
		public static String getProtocol_UDP() 
		{
			return Protocol_UDP;
		}
		
		public static String getPort() 
		{
			return Port;
		}
			
		public static String getImportManager_Title() 
		{
			return ImportManager_Title;
		}
		
		public static String getImportManagerNordVPN_Title() 
		{
			return ImportManagerNordVPN_Title;
		}
		
		public static String getMenuItemSetKeys()
		{
			return MenuItemSetKeys;
		}
		
		public static String getMenuItemExportProfile()
		{		
			return MenuItemExportProfile;
		}
		
		public static String getMenuItemImportProfile()
		{
			return MenuItemImportProfile;
		}
		
		
		public static String getImportManager_filePathTextField()
		{
			return ImportManager_filePathTextField;
		}
		public static void setImportManager_filePathTextField(String s)
		{
			ImportManager_filePathTextField = s;
		}
		
		public static String getImportManager_openDirectoryBrowserButton()
		{
			return ImportManager_openDirectoryBrowserButton;
		}
		
		public static String getOSBridge_Session_File_Name()
		{
			return OSBridge_Session_File_Name;
		}
		
		public static String getOSBridge_Temp_Login_File_Name()
		{
			return OSBridge_Temp_Login_File_Name;
		}
		
		public static String getOSBridge_Temp_Login_File_Path()
		{
			return OSBridge_Temp_Login_File_Path;
		}
		
		public static String getOSBridge_Session_File_Path()
		{
			return OSBridge_Session_File_Path;
		}
		
		public static String getDownloaded_Configfile_Dir()
		{
			return Downloaded_Configfile_Dir;
		}
		
		

		
		public static DateTimeFormatter getDateFormatter() 
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_DateFormatter;
			case ENGLISH: 	return EN_DateFormatter;
			default: 		return EN_DateFormatter;
			}
		}
		
		public static String getLocation() 
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Location;
			case ENGLISH: 	return EN_Location;
			default: 		return EN_Location;
			}
		}
		
		public static String getProtocol()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Protocol;
			case ENGLISH: 	return EN_Protocol;
			default: 		return EN_Protocol;
			}
		}
		
		public static String getFile()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_File;
			case ENGLISH: 	return EN_File;
			default: 		return EN_File;
			}
		}
		
		public static String getEdit()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Edit;
			case ENGLISH: 	return EN_Edit;
			default: 		return EN_Edit;
			}
		}
		
		public static String getDelete()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Delete;
			case ENGLISH: 	return EN_Delete;
			default: 		return EN_Delete;
			}
		}
		
		public static String getRemoved()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Removed;
			case ENGLISH: 	return EN_Removed;
			default: 		return EN_Removed;
			}
		}

		
		public static String getImport()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Import;
			case ENGLISH: 	return EN_Import;
			default: 		return EN_Import;
			}
		}
		
		public static String getChangeLanguage()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ChangeLanguage;
			case ENGLISH: 	return EN_ChangeLanguage;
			default: 		return EN_ChangeLanguage;
			}
		}	
		
		public static String getHelp()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Help;
			case ENGLISH: 	return EN_Help;
			default: 		return EN_Help;
			}
		}	
		
		public static String getOverviewClear()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItem_OverviewClear;
			case ENGLISH: 	return EN_MenuItem_OverviewClear;
			default: 		return EN_MenuItem_OverviewClear;
			}
		}
		
		public static String getHistoryClear()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItem_HistoryClear;
			case ENGLISH: 	return EN_MenuItem_HistoryClear;
			default: 		return EN_MenuItem_HistoryClear;
			}
		}
		
		public static String getConnect()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Connect;
			case ENGLISH: 	return EN_Connect;
			default: 		return EN_Connect;
			}
		}
		
		public static String getOverview()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Overview;
			case ENGLISH: 	return EN_Overview;
			default: 		return EN_Overview;
			}
		}
		
		public static String getNetwork()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Network;
			case ENGLISH: 	return EN_Network;
			default: 		return EN_Network;
			}
		}
		
		
		
		public static String getEnglish()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_English;
			case ENGLISH: 	return EN_English;
			default: 		return EN_English;
			}
		}
		
		public static String getGerman()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_German;
			case ENGLISH: 	return EN_German;
			default: 		return EN_German;
			}
		}
		
		public static String getCancel() 
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Cancel;
			case ENGLISH: 	return EN_Cancel;
			default: 		return EN_Cancel;
			}
		}
		
		public static String getHistory() 
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_History;
			case ENGLISH: 	return EN_History;
			default: 		return EN_History;
			}
		}
		
		public static String getFileName()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_FileName;
			case ENGLISH: 	return EN_FileName;
			default: 		return EN_FileName;
			}
		}
		
		public static String getDownload()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Download;
			case ENGLISH: 	return EN_Download;
			default: 		return EN_Download;
			}
		}
				
		public static String getLoad()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Load;
			case ENGLISH: 	return EN_Load;
			default: 		return EN_Load;
			}
		}
		
		public static String getAbout()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_About;
			case ENGLISH: 	return EN_About;
			default: 		return EN_About;
			}
		}			
		
		public static String getReceived()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Received;
			case ENGLISH: 	return EN_Received;
			default: 		return EN_Received;
			}
		}			
		
		public static String getTransmitted()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Transmitted;
			case ENGLISH: 	return EN_Transmitted;
			default: 		return EN_Transmitted;
			}
		}
		
		public static String getSettings()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Settings;
			case ENGLISH: 	return EN_Settings;
			default: 		return EN_Settings;
			}
		}
		
		public static String getAdd()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Add;
			case ENGLISH: 	return EN_Add;
			default: 		return EN_Add;
			}
		}
		
		public static String getName()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Name;
			case ENGLISH: 	return EN_Name;
			default: 		return EN_Name;
			}
		}
		
		public static String getDate()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Date;
			case ENGLISH: 	return EN_Date;
			default: 		return EN_Date;
			}
		}
		
		public static String getTime()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Time;
			case ENGLISH: 	return EN_Time;
			default: 		return EN_Time;
			}
		}						
			
		public static String getLanguageChangedTo()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_LanguageChangedTo;
			case ENGLISH: 	return EN_LanguageChangedTo;
			default: 		return EN_LanguageChangedTo;
			}
		}

		public static String getEditConfigFile()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_EditConfigFile;
			case ENGLISH: 	return EN_EditConfigFile;
			default: 		return EN_EditConfigFile;
			}
		}
		
		public static String getPermission_NoRootHeader()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Permission_NoRootHeader;
			case ENGLISH: 	return EN_Permission_NoRootHeader;
			default: 		return EN_Permission_NoRootHeader;
			}
		}
			
		public static String getPermission_NoRoot()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Permission_NoRoot;
			case ENGLISH: 	return EN_Permission_NoRoot;
			default: 		return EN_Permission_NoRoot;
			}
		}
		
		public static String getMenuProfile()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuProfile;
			case ENGLISH: 	return EN_MenuProfile;
			default: 		return EN_MenuProfile;
			}
		}
		public static String getMenuItemSetUserPass()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItemSetUserPass;
			case ENGLISH: 	return EN_MenuItemSetUserPass;
			default: 		return EN_MenuItemSetUserPass;
			}
		}

		
		public static String getImportManager_parseMetadataCheckBox()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ImportManager_parseMetadataCheckBox;
			case ENGLISH: 	return EN_ImportManager_parseMetadataCheckBox;
			default: 		return EN_ImportManager_parseMetadataCheckBox;
			}
		}
		
		public static String getImportManager_selectAllCheckBox()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ImportManager_selectAllCheckBox;
			case ENGLISH: 	return EN_ImportManager_selectAllCheckBox;
			default: 		return EN_ImportManager_selectAllCheckBox;
			}
		}
		
		public static String getImportManager_importButton()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ImportManager_importButton;
			case ENGLISH: 	return EN_ImportManager_importButton;
			default: 		return EN_ImportManager_importButton;
			}
		}


		public static String getImportManager_noFilesLoaded() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ImportManager_noFilesLoaded;
			case ENGLISH: 	return EN_ImportManager_noFilesLoaded;
			default: 		return EN_ImportManager_noFilesLoaded;
			}
		}

		public static String getImportManager_noCompatibleFilesFound() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ImportManager_noCompatibleFilesFound;
			case ENGLISH: 	return EN_ImportManager_noCompatibleFilesFound;
			default: 		return EN_ImportManager_noCompatibleFilesFound;
			}
		}
		
		public static String getImportManager_Number() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_ImportManager_Number;
		case ENGLISH: 	return EN_ImportManager_Number;
		default: 		return EN_ImportManager_Number;
		}
		}
		
		
		public static String getImportManager_clearAndAddRadioButton() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_clearAndAddRadioButton;
		case ENGLISH: 	return EN_clearAndAddRadioButton;
		default: 		return EN_clearAndAddRadioButton;
		}
		}
		
		public static String getImportManager_addRadioButton() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_addRadioButton;
		case ENGLISH: 	return EN_addRadioButton;
		default: 		return EN_addRadioButton;
		}
		}
		
		public static String getImportManager_noConfigsSelected() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_noConfigsSelected;
		case ENGLISH: 	return EN_noConfigsSelected;
		default: 		return EN_noConfigsSelected;
		}
		}
		
		public static String getNoDataLoaded() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_NoDataLoaded;
		case ENGLISH: 	return EN_NoDataLoaded;
		default: 		return EN_NoDataLoaded;
		}
		}
		
		
		public static String getException_NoReadPermissions() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_Exception_NoReadPermissions;
		case ENGLISH: 	return EN_Exception_NoReadPermissions;
		default: 		return EN_Exception_NoReadPermissions;
		}
		}
		
		
		public static String getException_NotExist() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_Exception_NotExist;
		case ENGLISH: 	return EN_Exception_NotExist;
		default: 		return EN_Exception_NotExist;
		}
		}
		
		
		public static String getInformationAlert_ConfigsSuccessfullyAdded() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_InformationAlert_ConfigsSuccessfullyAdded;
		case ENGLISH: 	return EN_InformationAlert_ConfigsSuccessfullyAdded;
		default: 		return EN_InformationAlert_ConfigsSuccessfullyAdded;
		}
		}
		
		public static String getInformationAlert_ConfigsWithErrorsAdded() 
		{
		switch(activeLanguage) 
		{
		case GERMAN: 	return DE_InformationAlert_ConfigsWithErrorsAdded;
		case ENGLISH: 	return EN_InformationAlert_ConfigsWithErrorsAdded;
		default: 		return EN_InformationAlert_ConfigsWithErrorsAdded;
		}
		}

		public static String getOSBridge_VPNConnectionError() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return (OSBridge.getInstance().isLinux())? DE_OSBridge_VPNConnectionError: DE_OSBridge_VPNConnectionError_Windows;
			case ENGLISH: 	return (OSBridge.getInstance().isLinux())? EN_OSBridge_VPNConnectionError: EN_OSBridge_VPNConnectionError_Windows;
			default: 		return (OSBridge.getInstance().isLinux())? EN_OSBridge_VPNConnectionError: EN_OSBridge_VPNConnectionError_Windows;
			}
		}

		public static String getOSBridge_VPNConnectionErrorHeader() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_OSBridge_VPNConnectionErrorHeader;
			case ENGLISH: 	return EN_OSBridge_VPNConnectionErrorHeader;
			default: 		return EN_OSBridge_VPNConnectionErrorHeader;
			}
		}

		public static String getConsoleOutputDialog_TopLabelText() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConsoleOutputDialog_TopLabelText;
			case ENGLISH: 	return EN_ConsoleOutputDialog_TopLabelText;
			default: 		return EN_ConsoleOutputDialog_TopLabelText;
			}
		}

		public static String getConsoleOutputDialog_Title() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConsoleOutputDialog_Title;
			case ENGLISH: 	return EN_ConsoleOutputDialog_Title;
			default: 		return EN_ConsoleOutputDialog_Title;
			}
		}
		
		public static String getConsoleOutputDialog_Error() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConsoleOutputDialog_Error;
			case ENGLISH: 	return EN_ConsoleOutputDialog_Error;
			default: 		return EN_ConsoleOutputDialog_Error;
			}
		}
		
		public static String getConsoleOutputDialog_Timeout() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConsoleOutputDialog_Timeout;
			case ENGLISH: 	return EN_ConsoleOutputDialog_Timeout;
			default: 		return EN_ConsoleOutputDialog_Timeout;
			}
		}
		
		public static String getConsoleOutputDialog_Disconnected() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConsoleOutputDialog_Disconnected;
			case ENGLISH: 	return EN_ConsoleOutputDialog_Disconnected;
			default: 		return EN_ConsoleOutputDialog_Disconnected;
			}
		}
		
		public static String getConsoleOutputDialog_Connected() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConsoleOutputDialog_Connected;
			case ENGLISH: 	return EN_ConsoleOutputDialog_Connected;
			default: 		return EN_ConsoleOutputDialog_Connected;
			}
		}
		
		public static String getCredentialDialog_Username() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_CredentialDialog_Username;
			case ENGLISH: 	return EN_CredentialDialog_Username;
			default: 		return EN_CredentialDialog_Username;
			}
		}
		public static String getCredentialDialog_Password() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_CredentialDialog_Password;
			case ENGLISH: 	return EN_CredentialDialog_Password;
			default: 		return EN_CredentialDialog_Password;
			}
		}
		public static String getCredentialDialog_PasswordSaved() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_CredentialDialog_PasswordSaved;
			case ENGLISH: 	return EN_CredentialDialog_PasswordSaved;
			default: 		return EN_CredentialDialog_PasswordSaved;
			}
		}
		public static String getCredentialDialog_Title(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_CredentialDialog_Title;
			case ENGLISH: 	return EN_CredentialDialog_Title;
			default: 		return EN_CredentialDialog_Title;
			}
		}
		
		public static String getOSBridge_CheckLoginData(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_OSBridge_CheckLoginData;
			case ENGLISH: 	return EN_OSBridge_CheckLoginData;
			default: 		return EN_OSBridge_CheckLoginData;
			}
		}
		public static String getMenuItem_Edit_NoConfigSelectedAlert(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItem_Edit_NoConfigSelectedAlert;
			case ENGLISH: 	return EN_MenuItem_Edit_NoConfigSelectedAlert;
			default: 		return EN_MenuItem_Edit_NoConfigSelectedAlert;
			}
		}
	
		public static String getConnectionHistory_ConnectedTo(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConnectionHistory_ConnectedTo;
			case ENGLISH: 	return EN_ConnectionHistory_ConnectedTo;
			default: 		return EN_ConnectionHistory_ConnectedTo;
			}
		}
		
		public static String getConnectionHistory_ConnectedSince(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConnectionHistory_ConnectedSince;
			case ENGLISH: 	return EN_ConnectionHistory_ConnectedSince;
			default: 		return EN_ConnectionHistory_ConnectedSince;
			}
		}
		
		public static String getConnectionHistory_BytesReceived(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConnectionHistory_BytesReceived;
			case ENGLISH: 	return EN_ConnectionHistory_BytesReceived;
			default: 		return EN_ConnectionHistory_BytesReceived;
			}
		}
		
		public static String getConnectionHistory_BytesSent(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConnectionHistory_BytesSent;
			case ENGLISH: 	return EN_ConnectionHistory_BytesSent;
			default: 		return EN_ConnectionHistory_BytesSent;
			}
		}
		
		public static String getConnectionHistory_ConnectedAt(){
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConnectionHistory_ConnectedAt;
			case ENGLISH: 	return EN_ConnectionHistory_ConnectedAt;
			default: 		return EN_ConnectionHistory_ConnectedAt;
			}
		}
		
		public static String getYesNoCheckBoxDialogPane_ExitDialogText() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_YesNoCheckboxDialogPane_ExitDialogText;
			case ENGLISH: 	return EN_YesNoCheckboxDialogPane_ExitDialogText;
			default: 		return EN_YesNoCheckboxDialogPane_ExitDialogText;
			}
		}
		
		public static String getYesNoCheckboxDialogPane_DoNotShowAgain() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_YesNoCheckboxDialogPane_DoNotShowAgain;
			case ENGLISH: 	return EN_YesNoCheckboxDialogPane_DoNotShowAgain;
			default: 		return EN_YesNoCheckboxDialogPane_DoNotShowAgain;
			}
		}
		
		public static String getYesNoCheckboxDialogPane_Exit() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_YesNoCheckboxDialogPane_Exit;
			case ENGLISH: 	return EN_YesNoCheckboxDialogPane_Exit;
			default: 		return EN_YesNoCheckboxDialogPane_Exit;
			}
		}
		
		public static String getAlertMessage_SaveChanges() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_AlertMessage_SaveChanges;
			case ENGLISH: 	return EN_AlertMessage_SaveChanges;
			default: 		return EN_AlertMessage_SaveChanges;
			}
		}
		
		public static String getEN_InformationAlert_ConfigChangesSaved() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_InformationAlert_ConfigChangesSaved;
			case ENGLISH: 	return EN_InformationAlert_ConfigChangesSaved;
			default: 		return EN_InformationAlert_ConfigChangesSaved;
			}
		}
		
		public static String getAlertMessage_DiscardChanges() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_AlertMessage_DiscardChanges;
			case ENGLISH: 	return EN_AlertMessage_DiscardChanges;
			default: 		return EN_AlertMessage_DiscardChanges;
			}
		}
		
		public static String getMenuItemImportFromFile() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItemImportFromFile;
			case ENGLISH: 	return EN_MenuItemImportFromFile;
			default: 		return EN_MenuItemImportFromFile;
			}
		}
		
		public static String getMenuItemImportFromNordVPNServer() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItemImportFromNordVPNServer;
			case ENGLISH: 	return EN_MenuItemImportFromNordVPNServer;
			default: 		return EN_MenuItemImportFromNordVPNServer;
			}
		}
	
		public static String getImportManagerNordVPN_NoConfigsLoaded() {
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ImportManagerNordVPN_NoConfigsLoaded;
			case ENGLISH: 	return EN_ImportManagerNordVPN_NoConfigsLoaded;
			default: 		return EN_ImportManagerNordVPN_NoConfigsLoaded;
			}
		}
		
		public static String getException_ImportManagerNordVPN_ErrorWithNordVPN()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_Exception_ImportManagerNordVPN_ErrorWithNordVPN;
			case ENGLISH: 	return EN_Exception_ImportManagerNordVPN_ErrorWithNordVPN;
			default: 		return EN_Exception_ImportManagerNordVPN_ErrorWithNordVPN;
			}
		}
		
		public static String getMenuItem_Export_Failed()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItem_Export_Failed;
			case ENGLISH: 	return EN_MenuItem_Export_Failed;
			default: 		return EN_MenuItem_Export_Failed;
			}
		}
		
		public static String getMenuItem_Import_Failed()
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItem_Import_Failed;
			case ENGLISH: 	return EN_MenuItem_Import_Failed;
			default: 		return EN_MenuItem_Import_Failed;
			}
		}
		
		public static String getMenuItem_Import_Warning() 
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_MenuItem_Import_Warning;
			case ENGLISH: 	return EN_MenuItem_Import_Warning;
			default: 		return EN_MenuItem_Import_Warning;
			}
		}
		
		public static String getConnectionHistory_LastConnection() 
		{
			switch(activeLanguage) 
			{
			case GERMAN: 	return DE_ConnectionHistory_LastConnection;
			case ENGLISH: 	return EN_ConnectionHistory_LastConnection;
			default: 		return EN_ConnectionHistory_LastConnection;
			}
		}
		
}
