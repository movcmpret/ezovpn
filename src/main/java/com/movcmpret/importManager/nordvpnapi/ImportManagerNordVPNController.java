package com.movcmpret.importManager.nordvpnapi;

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
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.event.ListChangeEvent;
import com.movcmpret.event.ListChangeEventAdditionType;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.ovpn.OVPNFileParser;
import com.movcmpret.ovpn.config.NordVPNConfig;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;
import com.movcmpret.web.nordvpn.NordVPNInfoJson;
import com.movcmpret.web.nordvpn.WebJsonParser;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Import manager for NordVPN configs obtained by api.nordvpn.com
 * @author movcmpret
 *
 */

public final class ImportManagerNordVPNController implements Initializable, DefaultController {

	public static ImportManagerNordVPNController thisInstance = null;	
	
	public ObservableList<NordVPNInfoJson> infoList;
	
	@FXML
	private TextField urlTextField;
	
	@FXML
	private Button buttonDownload;
	
	@FXML
	private ListView importListView;
	
	@FXML
	private CheckBox selectAllCheckBox;
	
	@FXML
	private Button importButton;
	
	@FXML
	private RadioButton clearAndAddRadioButton;
	
	@FXML
	private RadioButton addRadioButton;
	
	@FXML
	private StackPane stackPaneProcessIndicatorOverlay;
	
	@FXML
	private Label filterLabel;
	
	@FXML
	private TextField filterTextField;
	
	@FXML
	private GridPane importManagerRootGridPane;
	
	private VBox progressIndicatorBox;
	
	private ProgressIndicator pi;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set this instance. (Complete Singleton would be the better choice here)
		thisInstance = this;	
		initializeComponent();				
		//Register this Controller for UpdateText - Events (Runtime language swap) 
		EventManager.getInstance().registerController(this);
		
	}
	
	
	@FXML
	public void downloadButtonOnClick(ActionEvent event)
	{		
		showProgressIndicator();     
		WebJsonParserInfoThreadWorker worker = new WebJsonParserInfoThreadWorker();
		Thread thread = new Thread(worker);
		thread.start();
	}
	
	@FXML
	public void importButtonOnClick(ActionEvent event)
	{	
		if( this.importListView.getSelectionModel().getSelectedItems().size() > 0 )
		{
			showProgressIndicator();    
			List<NordVPNInfoJson> jsons = new ArrayList<NordVPNInfoJson>();
			
			if( this.selectAllCheckBox.isSelected() )
				jsons = this.importListView.getItems();
			else 
				jsons = this.importListView.getSelectionModel().getSelectedItems();
			
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
			WebJsonParserFilesThreadWorker worker = new WebJsonParserFilesThreadWorker(jsons, type);
			Thread thread = new Thread(worker);
			thread.start();
		}
		else
		{
			AlertManager.showWarningToast(Constants.getImportManager_noConfigsSelected(), (Stage)importButton.getScene().getWindow() );
		}
	}
	
	private void initializeComponent() 
	{
		this.importListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.infoList = FXCollections.observableArrayList();
		this.importListView.setItems(this.infoList);	
		setupFilter();
		
		updateTexts();
		createProcessIndicatorBox();
	}
	
	//Copy pasta 
	private void setupFilter() 
	{
		FilteredList<NordVPNInfoJson> filteredData = new FilteredList<>(this.infoList, t -> true);
		this.importListView.setItems(filteredData);
		 this.filterTextField.textProperty().addListener(
				 obs->{
		        String filter = filterTextField.getText(); 
		            filteredData.setPredicate(filter.isEmpty() ? s -> true : s -> s.getName().toLowerCase().contains(filter.toLowerCase()));
		    });
	}
		
	@Override
	public void updateTexts() {
		
		this.importListView.setPlaceholder(new Label(Constants.getImportManagerNordVPN_NoConfigsLoaded()));
		this.buttonDownload.setText(Constants.getDownload());
		this.selectAllCheckBox.setText(Constants.getImportManager_selectAllCheckBox());
		this.importButton.setText(Constants.getImportManager_importButton());
		this.addRadioButton.setText(Constants.getImportManager_addRadioButton());
		this.clearAndAddRadioButton.setText(Constants.getImportManager_clearAndAddRadioButton());
		
		Logger.LogDebug(this.getClass().getName() + " Texts updated");		
	}
	@Override
	public void refreshViews() {
		this.importListView.refresh();
		
	}
	@Override
	public void bind() 
	{	
	}

	private void createProcessIndicatorBox() 
	{
		pi = new ProgressIndicator();
        progressIndicatorBox = new VBox(pi);
        progressIndicatorBox.setAlignment(Pos.CENTER);

	}	
	
	private void showProgressIndicator() 
	{
		pi.setProgress(-1);
        stackPaneProcessIndicatorOverlay.setDisable(true);
		if(!importManagerRootGridPane.getChildren().contains(progressIndicatorBox))
	        importManagerRootGridPane.getChildren().add(progressIndicatorBox);	
	}
	
	private void hideProgressIndicator() 
	{
        stackPaneProcessIndicatorOverlay.setDisable(false);
		if(importManagerRootGridPane.getChildren().contains(progressIndicatorBox))
		importManagerRootGridPane.getChildren().remove(progressIndicatorBox);
	}
	
	/**
	 * Thread worker for getting the NordVPN Server info
	 * @author movcmpret
	 *
	 */
	private class WebJsonParserInfoThreadWorker implements Runnable
	{	
		public WebJsonParserInfoThreadWorker() 
		{
			
		}
			
		@Override
		public void run() 
		{		
			nordVPNServerInfo();

			Platform.runLater(new Runnable(){
				@Override
				public void run() {
			hideProgressIndicator();
				}});
			return;
		}
		
		private void nordVPNServerInfo() 
		{
			try {		
				List<NordVPNInfoJson> list = WebJsonParser.getInstance().getNordVPNServerInfo();
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						infoList.clear();
						infoList.addAll( list  );
						refreshViews();

					}	});			
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				Logger.LogError(e.getMessage());
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						AlertManager.showErrorAlert(Constants.getException_ImportManagerNordVPN_ErrorWithNordVPN(), "Error");
					}	});			
			}										
		}
	}
	
	/**
	 * Thread for getting the files from WebJsonParser
	 * @author movcmpret
	 *
	 */
	private class WebJsonParserFilesThreadWorker implements Runnable
	{
		List<NordVPNInfoJson> jsons;
		ListChangeEventAdditionType type;
		
		
		public WebJsonParserFilesThreadWorker(List<NordVPNInfoJson> jsons, ListChangeEventAdditionType type )
		{
			this.jsons = jsons;
			this.type = type;
		}
		@Override
		public void run() {
			
			String errors = "";
			List<NordVPNConfig> parsedConfigs = new ArrayList<NordVPNConfig>();
			for(int i = 0; i < jsons.size(); i++)
			{		
				NordVPNInfoJson info = jsons.get(i);
				try {
					for(File file : WebJsonParser.getInstance().getConfigFileForNordVPNInfoJson(info))
					{
					NordVPNConfig config = (NordVPNConfig) OVPNFileParser.getInstance().parseNordVPNFile(info, file);
					parsedConfigs.add( config );
					}
				} 
				catch (FileNotFoundException e) 
				{
					Logger.LogError(this.getClass().getName() +": "+ e.toString());
					errors += "Downloaderror: " + info.getName() +": " + e.toString()+"\n";
				}		
				catch (Exception e) 
				{
					Logger.LogError(this.getClass().getName() +": "+ e.toString());
					errors += "Parsingerror: " + info.getName() +": " + e.toString()+"\n";
				}
				updateProgressIndicator((double)(i+1)/jsons.size());
			}
			
			final String err = errors;
			Platform.runLater(new Runnable(){
				@Override
				public void run() {

					hideProgressIndicator();
					if( !err.equals("") )
					{
						AlertManager.showWarningAlert(err,
						String.format(Constants.getInformationAlert_ConfigsWithErrorsAdded(), err.split("\n").length));
					}
					else 
					{
						AlertManager.showInfoToast(Constants.getInformationAlert_ConfigsSuccessfullyAdded(), (Stage)importButton.getScene().getWindow());
					}
						
					EventManager.getInstance().fireListChangeEvent(new ListChangeEvent(parsedConfigs, type));
					}	
				});
		}
		
		private void updateProgressIndicator(double value)
		{
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					pi.setProgress(value);
				}	});			
		}
		
	}
	
}
