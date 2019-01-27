package com.movcmpret.application;

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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.movcmpret.constants.Constants;
import com.movcmpret.constants.Language;
import com.movcmpret.dialog.aboutdialog.AboutDialogController;
import com.movcmpret.dialog.credentials.CredentialInputController;
import com.movcmpret.dialog.editconfig.EditConfigController;
import com.movcmpret.event.EventManager;
import com.movcmpret.event.ListChangeEvent;
import com.movcmpret.importManager.fileimport.ImportManagerController;
import com.movcmpret.importManager.nordvpnapi.ImportManagerNordVPNController;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.interfaces.ListChangeHandler;
import com.movcmpret.osBridge.OSBridge;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.ovpn.tables.DefaultOVPNTable;
import com.movcmpret.ovpn.tables.NordVPNTable;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.tabs.TabHistoryController;
import com.movcmpret.tabs.TabNetworkingController;
import com.movcmpret.tabs.TabOverviewController;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;
import com.sun.javafx.tk.FileChooserType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public class MainViewController implements Initializable, DefaultController,ListChangeHandler{
	
	//ImportManager	
	Parent importParentRoot;
	static Stage importStage;
	FXMLLoader importFXMLLoader;
	
	//Import Manager NordVPN API
	Parent importNordVPNParentRoot;
	static Stage importNordVPNStage;
	FXMLLoader importNordVPNFXMLLoader;
	
	//CredentialInput
	Parent credentialInputParentRoot;
	static Stage  credentialInputStage;
	FXMLLoader credentialInputFXMLLoader;
	
	//Tabs 
	Parent overviewParentRoot;
	FXMLLoader overviewFXMLLoader;
	TabOverviewController tabOverviewController;
	
	Parent networkingParentRoot;
	FXMLLoader networkingFXMLLoader;
	TabNetworkingController tabNetworkingController;
	
	Parent historyParentRoot;
	FXMLLoader historyFXMLLoader;
	TabHistoryController tabHistoryController;
	
	@FXML
	private Button invisibleButton;
	@FXML 	
	private GridPane nodeGridPane;
	@FXML 	
	private TabPane OverviewTabPane;
	
	@FXML 
	private Tab OverviewTab;
	@FXML
	private Tab NetworkingTab;
	@FXML
	private Tab SettingsTab;
	@FXML
	private Tab HistoryTab;
	
	//File 
	@FXML
	private Menu MenuFile;
	
	//Import
		@FXML
		private Menu MenuImport;
		@FXML
		private MenuItem MenuItemImportFromFile;
		@FXML
		private MenuItem MenuItemImportFromNordVPNServer;
	
	@FXML
	private Menu MenuEdit;
	
	//Help
	@FXML
	private Menu MenuHelp;
	@FXML
	private MenuItem MenuItemOverviewClear;
	@FXML
	private MenuItem MenuItemHistoryClear;
	@FXML
	private Menu MenuChangeLanguage;
	@FXML
	private RadioMenuItem RadioMenuItemEnglish;
	@FXML
	private RadioMenuItem RadioMenuItemGerman;
	@FXML
	private MenuItem AboutMenuItem;
	
	
	@FXML
	private MenuItem MenuItemEditConfig;
	
	//View Menu
	@FXML
	private Menu MenuView;
	@FXML
	private Menu SubMenuOverviewList;	
	@FXML
	private RadioMenuItem SubRadioMenuItemOverviewListDefault;
	@FXML
	private RadioMenuItem SubRadioMenuItemOverviewListNordVPN;
	
	@FXML
	private ToggleGroup toggleGroupOverviewList;
	
	//Profile Menu
	@FXML
	private Menu MenuProfile;
	@FXML
	private MenuItem MenuItemSetUserPass;
	@FXML
	private MenuItem  MenuItemExportProfile;
	@FXML
	private MenuItem  MenuItemImportProfile;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Set language 
		Constants.SetActiveLanguage(UserProfile.getInstance().getUserProfileData().getLanguage());
		OSBridge.getInstance().isAdmin();
		InitializeComponent();		
		//Register this Controller for UpdateText - Events (Runtime language swap) 
		EventManager.getInstance().registerController(this);
		EventManager.getInstance().registerListChangeHandler(this);    	
				
	}
	
		
	public void InitializeComponent() 
	{ 	
		//Reset static instances to force reinitialization 
		//TODO Put this in an interface
		ImportManagerController.thisInstance = null;
		ImportManagerNordVPNController.thisInstance = null;
		CredentialInputController.thisInstance = null;
		
		if(importStage != null)
			importStage.close();
		
		if(importNordVPNStage != null)
			importNordVPNStage.close();
		
		if(credentialInputStage != null)
			credentialInputStage.close();
		
		
		//ImportManager
		importStage = new Stage();
		importStage.getIcons().add(new Image(this.getClass().getResourceAsStream("ezOVPN_logo.png")));
		importFXMLLoader = new FXMLLoader(getClass().getResource(Constants.getFXML_ImportManagerFilename()));

		//ImportManager NordVPN
		importNordVPNStage = new Stage();
		importNordVPNStage.getIcons().add(new Image(this.getClass().getResourceAsStream("ezOVPN_logo.png")));
		importNordVPNFXMLLoader = new FXMLLoader(getClass().getResource(Constants.getFXML_ImportManagerNordVPNFilename()));

		
		//CredentialInput
		credentialInputStage = new Stage();
		credentialInputStage.getIcons().add(new Image(this.getClass().getResourceAsStream("ezOVPN_logo.png")));
		credentialInputFXMLLoader = new FXMLLoader(getClass().getResource(Constants.getFXML_CredentialInputFilename()));
		
    	//Load Tabs 
		overviewFXMLLoader = new FXMLLoader(getClass().getResource(Constants.getFXML_TabOverviewFilename()));
		networkingFXMLLoader = new FXMLLoader(getClass().getResource(Constants.getFXML_TabNetworkingFilename()));
		historyFXMLLoader = new FXMLLoader(getClass().getResource(Constants.getFXML_TabHistoryFilename()));
		createOverviewTab();
		createNetworkingTab();
		createHistoryTab();
		
		//Set RadioMenuItem selected for OverviewTable
		setupOverviewTableRadioMenuItems();
		
		//Set RadioMenuItem selected for Language
		setupLanguageRadioMenuItems();
		
		// Hide unfinished tabs
//		this.OverviewTabPane.getTabs().remove(HistoryTab);
		this.OverviewTabPane.getTabs().remove(SettingsTab);
		
		updateTexts();		
		bind();
		refreshViews();			
    	Logger.LogInfo(this.getClass().getName() + ": Application Initialized.");      
       	
	}
			
	private void setupLanguageRadioMenuItems() {
		
		if( UserProfile.getInstance().getUserProfileData().getLanguage().equals(Language.GERMAN) )
			this.RadioMenuItemGerman.setSelected(true);
		if( UserProfile.getInstance().getUserProfileData().getLanguage().equals(Language.ENGLISH) )
			this.RadioMenuItemEnglish.setSelected(true);	
	}

	
    private void setupOverviewTableRadioMenuItems() 
    {
		if(UserProfile.getInstance().getUserProfileData().getOverviewTable().equals(NordVPNTable.class.getName())) 
		{
			this.SubRadioMenuItemOverviewListNordVPN.setSelected(true);
		}
		else if(UserProfile.getInstance().getUserProfileData().getOverviewTable().equals(DefaultOVPNTable.class.getName()))
		{
			this.SubRadioMenuItemOverviewListDefault.setSelected(true);
		}
		
	}

	/**
     *  Open ImportManagerView (File)
     *
     */
    @FXML 
    protected void importFromFileMenuItemOnClick(ActionEvent event) {
   	   	 	
		this.openImportManager();
    }
    
	/**
     *  Open ImportManagerView (NordVPN API)
     *
     */
    @FXML 
    protected void importFromNordVPNServerMenuItemOnClick(ActionEvent event) {
   	   	
    	//If view not open
    	if(ImportManagerNordVPNController.thisInstance == null) 
    	{ 
    		EventManager.getInstance().registerListChangeHandler(this);    		
    	}    	
		this.openImportManagerNordVPN();
    }
    
    /**
     *  Clear OverviewTable
     *
     */
    @FXML 
    protected void menuItemOverviewClearOnClick(ActionEvent event) 
    {
    	this.tabOverviewController.getOverviewTableData().clear();
    	UserProfile.getInstance().getUserProfileData().setLastConfig(null);
    }
    
    
    /**
     *  Clear HistoryTable
     *
     */
    @FXML 
    protected void menuItemClearHistoryOnClick(ActionEvent event) 
    {
    	this.tabHistoryController.getHistoryTableData().clear();
    }
    
    /**
     *  Clear OverviewTable
     *
     */
    @FXML 
    protected void menuItemEditOnClick(ActionEvent event) {


    	OVPNConfig selectedConf = (OVPNConfig)this.tabOverviewController.getOverviewTableView().getSelectionModel().getSelectedItem();
    	if( selectedConf != null ) 
    	{
    		EditConfigController.create().setup(selectedConf).show();
		}
    	else
    		AlertManager.showErrorToast(Constants.getMenuItem_Edit_NoConfigSelectedAlert(), (Stage)tabOverviewController.getButtonConnect().getScene().getWindow() );
    
   }
    
    @FXML 
    protected void aboutMenuItemOnClick(ActionEvent event) {

    	new AboutDialogController().showOnTop();
    
   }
    
    /**
     * 
     * Profilemenu
     */
    @FXML 
    protected void menuItemSetUserPassOnClick(ActionEvent event) {

		this.openCredentialDialog();
    }
    
    /**
     * Opens a file chooser to Export a config file
     * @param event
     */
    @FXML 
    protected void menuItemExportOnClick(ActionEvent event) 
    {
    	File file = openExportDialog();
    	if( file != null )
    	{
    		try {
			OSBridge.getInstance().exportSession(file);
		} catch (Exception e) {
			AlertManager.showErrorToast(Constants.getMenuItem_Export_Failed(), (Stage)this.OverviewTabPane.getScene().getWindow()); 
			Logger.LogError(e.getMessage());
			e.printStackTrace();
		}
    	}
    }
    /**
     * Opens a file chooser to Import a config file and restarts the Application
     * @param event
     */
    @FXML 
    protected void menuItemImportOnClick(ActionEvent event) 
    {
    	File file = openImportDialog();
    	if( file != null )
    	{
    	Alert importAlert = new Alert(AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
    	
    	importAlert.setHeaderText(Constants.getMenuItem_Import_Warning());
		importAlert.setTitle(Constants.getImport());
		Optional<ButtonType> btn = importAlert.showAndWait();
		
		if (btn.get() == ButtonType.YES) {

    	try {
			OSBridge.getInstance().importSession(file);
			MainView.relaunch();
		} catch (Exception e) {
			AlertManager.showErrorToast(Constants.getMenuItem_Import_Failed(), (Stage)this.OverviewTabPane.getScene().getWindow()); 
			Logger.LogError(e.getMessage());
			e.printStackTrace();
		 }
    	}
	  }
    }
    
    
    @FXML 
    protected void germanMenuItemOnClick(ActionEvent event) 
    {
    	UserProfile.getInstance().getUserProfileData().setLanguage(Language.GERMAN);
    	if( Constants.SetActiveLanguage(Language.GERMAN) )
    	AlertManager.showInfoToast(String.format(Constants.getLanguageChangedTo(), Constants.getGerman()), (Stage)this.OverviewTabPane.getScene().getWindow());
    }
    
    @FXML 
    protected void englishMenuItemOnClick(ActionEvent event) 
    {
    	UserProfile.getInstance().getUserProfileData().setLanguage(Language.ENGLISH);
    	if( Constants.SetActiveLanguage(Language.ENGLISH) )
    	AlertManager.showInfoToast(String.format(Constants.getLanguageChangedTo(), Constants.getEnglish()), (Stage)this.OverviewTabPane.getScene().getWindow());
    }
    
    /**
     * Set Default Table  
     * @param event
     */
    @FXML
    protected void subMenuItemOverviewListDefaultOnClick(ActionEvent event) 
    {
    	if(UserProfile.getInstance().getUserProfileData().getOverviewTable().equals(DefaultOVPNTable.class.getName()))
    		return;
    	UserProfile.getInstance().getUserProfileData().setOverviewTable(DefaultOVPNTable.class.getName());
    	this.tabOverviewController.swapTable();
    }
    /**
     * Set NordVPN table
     * @param event
     */
    @FXML
    protected void subMenuItemOverviewListNordVPNOnClick(ActionEvent event) 
    {	
    	if(UserProfile.getInstance().getUserProfileData().getOverviewTable().equals(NordVPNTable.class.getName()))
    		return;
    	UserProfile.getInstance().getUserProfileData().setOverviewTable(NordVPNTable.class.getName());
    	this.tabOverviewController.swapTable();
    }
    
    /**
     *  Open ImportManagerView
     *
     */
    private void openImportManager() 
    {
		if(ImportManagerController.thisInstance != null)
		{
			importStage.show();
			return;
		}		
		try {
			importParentRoot = importFXMLLoader.load();			
			Scene scene = new Scene(importParentRoot);
			importStage.setScene(scene);
			importStage.setTitle(Constants.getImportManager_Title());
			importStage.show();			         		
		} catch (IOException e) {
			Logger.LogError(e.getMessage());
			Logger.LogDebug(e.getStackTrace());
		}
    }
    
    
    /**
     *  Open ImportManagerView
     *
     */
    private void openImportManagerNordVPN() 
    {
		if(ImportManagerNordVPNController.thisInstance != null)
		{
			importNordVPNStage.show();
			return;
		}		
		try {
			importNordVPNParentRoot = importNordVPNFXMLLoader.load();			
			Scene scene = new Scene(importNordVPNParentRoot);
			importNordVPNStage.setScene(scene);
			importNordVPNStage.setTitle(Constants.getImportManagerNordVPN_Title());
			importNordVPNStage.show();			         		
		} catch (IOException e) {
			Logger.LogError(e.getMessage());
			Logger.LogDebug(e.getStackTrace());
		}
    }
    
    
    
    
    private void openCredentialDialog() 
    {
		if(CredentialInputController.thisInstance != null)
		{
			credentialInputStage.show();
			return;
		}		
		try {
			credentialInputParentRoot = credentialInputFXMLLoader.load();			
			Scene scene = new Scene(credentialInputParentRoot);
			credentialInputStage.setScene(scene);
			credentialInputStage.setTitle(Constants.getCredentialDialog_Title());
			credentialInputStage.show();			         		
		} catch (IOException e) {
			Logger.LogError(e.getMessage());
			Logger.LogDebug(e.getStackTrace());
		}
    }
    
    private File openExportDialog() 
    {
		return openImportExportDialog("Export", FileChooserType.SAVE);
    }
    
    private File openImportDialog() 
    {
		return openImportExportDialog("Import", FileChooserType.OPEN);
    	
    }
    
    private File openImportExportDialog(String title, FileChooserType type) 
    {
    	FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ezOVPN Session files (*.session)", "*.session");
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().add(filter);
    	fc.initialDirectoryProperty()
				.set(new File(Constants.getImportManager_filePathTextField()));
    	fc.setTitle(title);
    	File path = null;
    	try {
    		if(type == FileChooserType.OPEN)
    			path = fc.showOpenDialog(this.tabOverviewController.getButtonConnect().getScene().getWindow());
    		else if(type == FileChooserType.SAVE)
    			path = fc.showSaveDialog(this.tabOverviewController.getButtonConnect().getScene().getWindow());		

    	if(path != null)    
			{
    		String filepath=path.getAbsolutePath();
				UserProfile.getInstance().getUserProfileData()
						.setImportExport_LastFolder(filepath.substring(filepath.lastIndexOf('/'), filepath.length()));
          }

    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    	return path;
    	
        
    }
    
    private void createOverviewTab() 
    {		
		try {
			overviewParentRoot = overviewFXMLLoader.load();						
			// Dangerous cast ?
			tabOverviewController = (TabOverviewController)overviewFXMLLoader.getController();
			this.OverviewTab.setContent(overviewParentRoot);
		} catch (IOException e) {
			Logger.LogError(this.getClass().getName() + ": Error while creating overview tab.  " + e.getMessage());
			Logger.LogDebug(e.getStackTrace());
		}
    }
    
    private void createNetworkingTab() 
    {
		try {
			networkingParentRoot = networkingFXMLLoader.load();						
			tabNetworkingController = (TabNetworkingController)networkingFXMLLoader.getController();
			this.NetworkingTab.setContent(networkingParentRoot);
		} catch (IOException e) {
			Logger.LogError(this.getClass().getName() + ": Error while creating network tab.  " + e.getMessage());
			Logger.LogDebug(e.getStackTrace());
		}
    }
    
    private void createHistoryTab() 
    {
		try {
			historyParentRoot = historyFXMLLoader.load();						
			tabHistoryController = (TabHistoryController)historyFXMLLoader.getController();
			this.HistoryTab.setContent(historyParentRoot);
		} catch (IOException e) {
			Logger.LogError(this.getClass().getName() + ": Error while creating  tab.  " + e.getMessage());
			Logger.LogDebug(e.getStackTrace());
		}
    }

     		
	@Override
	public void updateTexts()
	{		
		// Tabpane
		this.OverviewTab.setText(Constants.getOverview());
		this.NetworkingTab.setText(Constants.getNetwork());
		this.HistoryTab.setText(Constants.getHistory());
		this.SettingsTab.setText(Constants.getSettings());
		
		//Menu
		this.MenuFile.setText(Constants.getFile());
		this.MenuImport.setText(Constants.getImport());		
		this.MenuItemImportFromFile.setText(Constants.getMenuItemImportFromFile());
		this.MenuItemImportFromNordVPNServer.setText(Constants.getMenuItemImportFromNordVPNServer());
		
		
		this.MenuEdit.setText(Constants.getEdit());
		this.MenuItemOverviewClear.setText(Constants.getOverviewClear());	
		this.MenuItemHistoryClear.setText(Constants.getHistoryClear());	
		
		//Menu Help
		this.MenuHelp.setText(Constants.getHelp());
		this.MenuChangeLanguage.setText(Constants.getChangeLanguage());
		this.RadioMenuItemEnglish.setText(Constants.getEnglish());
		this.RadioMenuItemGerman.setText(Constants.getGerman());
		this.MenuItemEditConfig.setText(Constants.getEditConfigFile());	
		this.AboutMenuItem.setText(Constants.getAbout());
			//Menu Profile
			this.MenuProfile.setText(Constants.getMenuProfile());
			this.MenuItemSetUserPass.setText(Constants.getMenuItemSetUserPass());
			this.MenuItemExportProfile.setText(Constants.getMenuItemExportProfile());
			this.MenuItemImportProfile.setText(Constants.getMenuItemImportProfile());

			Logger.LogDebug(this.getClass().getName() + " Texts updated");
	}

	@Override
	public void refreshViews() 
	{
		//TODO update tabs
	}


	@Override
	public void bind() 
	{
	}

	/**
	 * Handler for {@code ListChangeEvent}
	 */
	@Override
	public void onListChange(ListChangeEvent event) {
		
		
		if(event.getList().size() > 0)
		{
			if(event.getList() instanceof ArrayList<?>) {							
				
				/**
				 * Overview Tab 
				 * 
				 */
				if(event.getList().get(0) instanceof OVPNConfig)
				{
					// Clear table and add new data
					if(event.getListChangeEventAdditionType() != null && event.getListChangeEventAdditionType() == event.getListChangeEventAdditionType().CLEAN_ADDITION)
					{
						this.tabOverviewController.setOverviewTableData((ArrayList<OVPNConfig>) event.getList());
					}
					// add new data to table 
					else if(event.getListChangeEventAdditionType() != null && event.getListChangeEventAdditionType() == event.getListChangeEventAdditionType().ADDITION)
					{
						this.tabOverviewController.getOverviewTableData().addAll((ArrayList<OVPNConfig>) event.getList());			
					}
					// Default: Clear table and add new data
					else
					{
						this.tabOverviewController.setOverviewTableData((ArrayList<OVPNConfig>) event.getList());
					}
					
					/**
					 * Space for other
					 * 
					 */										
				}
			}
		}
	}
	

		
	
}
