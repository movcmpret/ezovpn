package com.movcmpret.importManager.fileimport;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.event.ListChangeEvent;
import com.movcmpret.event.ListChangeEventAdditionType;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.ovpn.OVPNConfigType;
import com.movcmpret.ovpn.OVPNFileParser;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Controller class for the {@code ImportManager.fxml} view.
 * @author movcmpret
 *
 */
public final class ImportManagerController implements Initializable, DefaultController {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Set this instance. (Complete Singleton would be the better choice here)
		thisInstance = this;	
		initializeComponent();
		
		//Register this Controller for UpdateText - Events (Runtime language swap) 
		EventManager.getInstance().registerController(this);
	}
	
	
	public void initializeComponent()
	{
		rawImportTableData = new ArrayList<ImportManagerListViewElement>();
		updateTexts();	
		bind();
		
	}
	public static ImportManagerController thisInstance = null;
	
	//ListData
	private ObservableList<ImportManagerListViewElement> importTableData;
	private ArrayList<ImportManagerListViewElement> rawImportTableData;
	
	@FXML
	GridPane importManagerRootGridPane;
	@FXML
	CheckBox parseMetadataCheckBox;
	
	@FXML
	CheckBox selectAllCheckBox;
	
	@FXML
	ListView<ImportManagerListViewElement> importListView;
	
	@FXML
	TextField filePathTextField;
	
	@FXML
	Button openDirectoryBrowserButton;
	
	@FXML
	Button importButton;
	
	@FXML
	RadioButton clearAndAddRadioButton;
	
	@FXML
	RadioButton addRadioButton;
	
	@FXML
	protected void filePathTextFieldOnKeyPressed(KeyEvent event)
	{
		//if enter pressed 
		if(event.getCode() == KeyCode.ENTER)
		{			
	    	try
	    	{
	    	updateListView();
	    	}
	    	catch(Exception r)
	    	{
	    		AlertManager.showErrorToast(r.getMessage(), (Stage) this.importButton.getScene().getWindow());
	    	}								
		}
	}
	@Override
	public void updateTexts() {
		parseMetadataCheckBox.setText(Constants.getImportManager_parseMetadataCheckBox());
		selectAllCheckBox.setText(Constants.getImportManager_selectAllCheckBox());
		filePathTextField.setText(UserProfile.getInstance().getUserProfileData().getImportManager_LastFolder());
		openDirectoryBrowserButton.setText(Constants.getImportManager_openDirectoryBrowserButton());
		importButton.setText(Constants.getImportManager_importButton());
		clearAndAddRadioButton.setText(Constants.getImportManager_clearAndAddRadioButton());
		addRadioButton.setText(Constants.getImportManager_addRadioButton());
		importListView.setPlaceholder(new Label(Constants.getImportManager_noCompatibleFilesFound()));

		Logger.LogDebug(this.getClass().getName() + " Texts updated");
	
	}

	@Override
	public void refreshViews() {
	//Try to parse configfiles from last path (UserProfile)
		try 
		{
			updateListView();
		}
		catch(Exception e) {
			Logger.LogError("Failed to load configs");
		}
		
		importListView.refresh();	
			
	}

	@Override
	public void bind() {
		populateListView();
		refreshViews();	

	}
	
	@FXML
	public void openDirectoryBrowserButtonOnClick(ActionEvent event) 
	{
		DirectoryChooser dc = new DirectoryChooser();
		dc.initialDirectoryProperty()
				.set(new File(UserProfile.getInstance().getUserProfileData().getImportManager_LastFolder()));
		dc.setTitle("Open config-filepath");
    	String path = null;
    	try {
    	path = dc.showDialog(importButton.getScene().getWindow()).getPath(); 
    	}
    	catch(Exception e){return;}
    	
        if(path != null && !path.isEmpty())    
        {
        	UserProfile.getInstance().getUserProfileData().setImportManager_LastFolder(path);
        	filePathTextField.setText(path);   
        }
        try
    	{
    	updateListView();
    	}
    	catch(Exception r)
    	{
    		AlertManager.showErrorToast(r.getMessage(), (Stage) this.importButton.getScene().getWindow());
    	}  	
	}
	
	@FXML
	public void importButtonOnClick(ActionEvent event)
	{	
			
		//If there are entries in the listview that are not "not found"-Strings 
		if( this.importTableData.size() > 0 ||  !importListView.getSelectionModel().getSelectedItems().isEmpty() )
		{
			List<OVPNConfig> configs;
			//Integer wrapper for "fake references"
			List<String>  errors = new ArrayList<String>();
			/*logMessage
			 * CheckBoxes
			 */
			//If user wants to try parsing metadata
			if(this.selectAllCheckBox.isSelected()) {
				this.importListView.getSelectionModel().selectAll();
			}
			if(this.parseMetadataCheckBox.isSelected())
    		{				
				configs = processSelectedItemsMetadata(this.importListView.getSelectionModel().getSelectedItems(), errors);
			}
			else
			{
				configs = processSelectedItems(this.importListView.getSelectionModel().getSelectedItems(), errors);
			}
			
			/**
			 * RadioButtons
			 */
			
			ListChangeEventAdditionType type;
			if(this.addRadioButton.isSelected()) 
			{
				type = ListChangeEventAdditionType.ADDITION;
			}
			else if(this.clearAndAddRadioButton.isSelected()) 
			{
				type = ListChangeEventAdditionType.CLEAN_ADDITION;
			}
			else
			{
				type = ListChangeEventAdditionType.CLEAN_ADDITION;
			}
		//Fire event with processed list
		EventManager.getInstance().fireListChangeEvent(new ListChangeEvent(configs, type));
		
		//Show information messages 
		if(errors.size() >  0)
		{
			String errorTexts = "";
			for(String s: errors)
				errorTexts+="\n"+s;
		AlertManager.showWarningAlert(errorTexts , String.format(Constants.getInformationAlert_ConfigsWithErrorsAdded(), errors.size()));
		}
		else
		{
		AlertManager.showInfoToast(Constants.getInformationAlert_ConfigsSuccessfullyAdded(), (Stage)importButton.getScene().getWindow());		
		}
	}
		else
		{
			AlertManager.showWarningToast(Constants.getImportManager_noConfigsSelected(), (Stage)importButton.getScene().getWindow() );
		}
}
	
	private List<OVPNConfig> processSelectedItemsMetadata(List<ImportManagerListViewElement> list, List<String> errors) 
	{
		List<File> selectedFiles = new ArrayList<File>();
		for(ImportManagerListViewElement element: list)
			selectedFiles.add(new File(element.getFilePath()));		
		
		return parseNordVPNMetadata(selectedFiles, errors);			
	}
	
	private List<OVPNConfig> processSelectedItems(List<ImportManagerListViewElement> list, List<String> errors) 
	{
		List<File> selectedFiles = new ArrayList<File>();
		for(ImportManagerListViewElement element: list) 
			selectedFiles.add(new File(element.getFilePath()));		
		
		return parseConfig(selectedFiles, errors);	
	
	}
	
	/**
	 * Setup ImportListView with items 
	 */
	public void populateListView() 
	{
		importListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	   	importTableData = FXCollections.observableList(rawImportTableData);		   	
	   	importListView.setItems(importTableData);

	}
	/*
	 * Update ImportListView with new files in the chosen directory of filePathTextField
	 */
	public void updateListView() throws Exception
	{
		importTableData.clear();
		
		//Check if accessable
		if( !new File(filePathTextField.getText()).exists())
		{
			throw new Exception(Constants.getException_NotExist());
		}
		if( !new File(filePathTextField.getText()).canRead())
		{
			throw new Exception(Constants.getException_NoReadPermissions());
		}
		
		
		for( File f : new File(filePathTextField.getText()).listFiles())
			if(f.getName().matches(".*\\.ovpn"))
				importTableData.add(new ImportManagerListViewElement( f.getPath() ) );
			
		//Sort Data
		importTableData.sort(new Comparator<ImportManagerListViewElement>()
                 {
					@Override
					public int compare(ImportManagerListViewElement o1, ImportManagerListViewElement o2) {						
						return o1.getFilePath().compareTo(o2.getFilePath());					
					}        
                 });
	}
	
	/**
	 * Own method for parsing NordVPN configs.
	 * @param files Filepaths to each configfile. 
	 * @return parsed OVPNConfigs (NordConfig)
	 */
	 public List<OVPNConfig> parseNordVPNMetadata(List<File> files, List<String> errors)
	    {
	    	List<OVPNConfig> list = new ArrayList<OVPNConfig>();
			for(File file: files)   
			{
	    	try 
	    	{		  	
	    		list.add( OVPNFileParser.getInstance().parseOVPNFile(OVPNConfigType.NORDVPN, file) );   	
			} 
	    	catch (Exception e) 
	    	{		
	    		errors.add(file.getName().toString());
				Logger.LogError(e.getMessage());		
			}
			}
	    	if(errors.size() > 0)
			Logger.LogError("Total parsing Errors: " +  errors.size());   
			
	    	return list;
	    }
	 
	 /**
	  * Method for parsing OVPNConfigs
	  * @param files Filepaths to each configfile. 
	  * @return parsed OVPNConfigs
	  */
	 public List<OVPNConfig> parseConfig(List<File> files,  List<String> errors)
	    {
	    	List<OVPNConfig> list = new ArrayList<OVPNConfig>();
			for(File file: files)   
			{
	    	try 
	    	{		  	
	    		list.add(OVPNFileParser.getInstance().parseOVPNFile(OVPNConfigType.DEFAULT, file));   	
			} 
	    	catch (Exception e) 
	    	{		
	    		errors.add("Parsing error: "+e.getMessage());
				Logger.LogError(e.getMessage());		
			}
			}
	    	if(errors.size() != 0)
			Logger.LogError("Total parsing Errors: " +  errors.size());   
			
	    	return list;
	    }
}
