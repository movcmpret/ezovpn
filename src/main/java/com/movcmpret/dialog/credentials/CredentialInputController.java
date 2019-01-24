package com.movcmpret.dialog.credentials;

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



import java.net.URL;
import java.util.ResourceBundle;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.interfaces.DefaultController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.utility.Logger;

public class CredentialInputController implements Initializable, DefaultController
{
	public static CredentialInputController thisInstance = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		thisInstance = this;
		//Register this Controller for UpdateText - Events (Runtime language swap) 
		EventManager.getInstance().registerController(this);
		updateTexts();
	}
	
	@FXML
	GridPane credentialInputGridPane;
	
	@FXML
	TextField textFieldUsername;
	
	@FXML
	PasswordField passwordField;
	
	@FXML
	Label labelUsername;
	
	@FXML
	Label labelPassword;
	
	@FXML
	Button buttonOK;
	
	/**
	 * OK/ConfirmButton Click Handler
	 *
	 * @param event
	 */
	@FXML
	public void buttonOKOnClick( ActionEvent event ) 
	{
		if( this.textFieldUsername.getText().isEmpty() )
		{
		//TODO	
		}
		UserProfile.getInstance().getUserProfileData().setUsername(this.textFieldUsername.getText());
		
		if( !this.passwordField.getText().isEmpty() )
		 {
			UserProfile.getInstance().getUserProfileData().setPasswordBase64(this.passwordField.getText());
		 }
		
		closeWindow();

	}
	
	private void closeWindow() 
	{
		 Stage stage = (Stage) this.buttonOK.getScene().getWindow();
		 stage.hide();
	}
	@Override
	public void updateTexts() {
		
		this.textFieldUsername.setText(UserProfile.getInstance().getUserProfileData().getUsername());
		
		if(!UserProfile.getInstance().getUserProfileData().getPassword().isEmpty())
		this.passwordField.setPromptText(Constants.getCredentialDialog_PasswordSaved());
	
		this.labelPassword.setText(Constants.getCredentialDialog_Password());
		this.labelUsername.setText(Constants.getCredentialDialog_Username());
		
		Logger.LogDebug(this.getClass().getName() + " Texts updated");
		
	}

	@Override
	public void refreshViews() {
		//method stub
		
	}

	@Override
	public void bind() {
		// method stub
		
	}



}
