package com.movcmpret.dialog.editconfig;

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



import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.osBridge.OSBridge;
import com.movcmpret.ovpn.OVPNFileParser;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.tabs.TabOverviewController;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EditConfigController implements Initializable, DefaultController
{
	protected Stage stage;
	
	protected boolean configChanged = false;
	
	protected OVPNConfig config;
	
	@FXML
	protected TextArea textArea;
	
	@FXML
	protected Button buttonOK;
	
	@FXML
	protected Button buttonCancel;
	
	@Override
	public void updateTexts() 
	{
		buttonCancel.setText(Constants.getCancel());
		Logger.LogInfo(this.getClass().getName() + ": Texts updated");		
	}

	/**
	 * Creates a new EditConfigController ( use setup and show afterwards).
	 * @return
	 */
	public static EditConfigController create() 
	{
		//CredentialInput
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(EditConfigController.class
				.getResource(Constants.getFXML_EditConfiglInputFilename()));
		try {
			Parent p = loader.load();						
			EditConfigController controller = (EditConfigController)loader.getController();
			controller.setStage(stage);
			Scene scene = new Scene(p);
			stage.setScene(scene);
			stage.setTitle(Constants.getEditConfigFile());		         		
			return controller;
			} 
			catch (IOException e) {
				Logger.LogError(EditConfigController.class.getName() +": Error while creating Configeditor");
			Logger.LogError(e.getMessage());
			Logger.LogDebug(e.getStackTrace());
			}
		return null;
	}
	
	public EditConfigController setup(OVPNConfig config) 
	{
		this.config = config;
		try {
			Scanner s = new Scanner(config.getConfigFile()).useDelimiter("\r\n");
			while (s.hasNext()) {
				textArea.appendText(s.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Add change handler to add a * to the title and generate a warning if user
		// discards or clicks OK.
		textArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				if(configChanged == false)
				{
					configChanged = true;
				}
				if(stage.getTitle().equals(Constants.getEditConfigFile()))
				{
				stage.setTitle(stage.getTitle() + "*");
				}
			}
		});
		
		return this;
	}
	
	public EditConfigController show() 
	{
		this.stage.show();
		return this;
	}
	
	
    @FXML 
	protected void buttonOKOnClick(ActionEvent event) {
		if (this.configChanged == true) {

			Alert alert = new Alert(AlertType.INFORMATION ,"", ButtonType.YES,
					ButtonType.NO, ButtonType.CANCEL);
			alert.setHeaderText(Constants.getAlertMessage_SaveChanges());
			alert.setTitle(Constants.getEditConfigFile());
			Optional<ButtonType> btn = alert.showAndWait();
			if (btn.get() == ButtonType.YES) {
				String s = textArea.getText();
				OSBridge.getInstance().writeToFile(s, config.getConfigFile());
				OVPNFileParser.getInstance().reParseDefaultConfig(config);

				// Refresh the list view
				EventManager.getInstance().fireRefreshViewsEvent();
				AlertManager.showInfoToast(Constants.getEN_InformationAlert_ConfigChangesSaved() + " " + config.getFileName(),
						(Stage) TabOverviewController.thisInstance.getButtonConnect().getScene().getWindow());
				stage.hide();
			}
		}
		else 
		{
			stage.hide();
		}

	}
    
    @FXML 
    protected void buttonCancelOnClick(ActionEvent event) 
    {	
		if (this.configChanged == true) {
			Alert alert = new Alert(AlertType.INFORMATION, "",
					ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.setHeaderText(Constants.getAlertMessage_DiscardChanges());
			alert.setTitle(Constants.getEditConfigFile());
			
			Optional<ButtonType> btn = alert.showAndWait();

			if (btn.get() == ButtonType.YES) {
				this.stage.hide();
			} else {
				return;
			}
		}
		this.stage.hide();
    }
    
	
	@Override
	public void refreshViews() {}

	@Override
	public void bind() 
	{
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Register this Controller for UpdateText - Events (Runtime language swap) 
		EventManager.getInstance().registerController(this);
		updateTexts();
		
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	

}
