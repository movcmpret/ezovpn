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



import java.util.Optional;

import com.movcmpret.constants.Constants;
import com.movcmpret.dialog.credentials.CredentialInputController;
import com.movcmpret.importManager.fileimport.ImportManagerController;
import com.movcmpret.importManager.nordvpnapi.ImportManagerNordVPNController;
import com.movcmpret.osBridge.OSBridge;
import com.movcmpret.ovpn.connection.OVPNConnector;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;
import com.movcmpret.utility.panes.YesNoCheckboxDialogPane;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainView extends Application implements EventHandler<WindowEvent> {

	private int windowMinHeight;
	private int windowMinWidth;
	private int windowMaxHeight;
	private int windowMaxWidth;

	private int windowHeight;
	private int windowWidth;

	public static Stage stage;
	
	public static MainView mainView;

	public static void main(String[] args) {
		System.out.println("    ezOVPN  Client Copyright (C) 2018-2019  movcmpret.com\n" + 
				"    This program comes with ABSOLUTELY NO WARRANTY;\n" + 
				"    This is free software, and you are welcome to redistribute it\n" + 
				"    under certain conditions;");
		mainView = new MainView();
		OSBridge.getInstance().readSession();
		Application.launch(args);
	}

	public MainView() {
		windowMinHeight = 350;
		windowMinWidth = 350;
		windowMaxHeight = 99999;
		windowMaxWidth = 99999;

		windowHeight = 700;
		windowWidth = 900;

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;
		try {
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("ezOVPN_logo.png")));
		} catch (Exception e) {
			Logger.LogError(this.getClass().getName() + ": Something went wrong while processing \"ezOVPN_logo\" ");
		}
		Platform.setImplicitExit(false);
		stage.setOnCloseRequest(this);
		InitView();
		
		//Set shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() 
		{@Override
		public void run(){
			OVPNConnector.getInstance().disconnect();
			OSBridge.getInstance().writeSession();
			;}}));
		
		
		Parent root = FXMLLoader.load(
				getClass().getResource(Constants.getFXML_MainSceneFilename()));

		Scene scene = new Scene(root, windowWidth, windowHeight);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void InitView() {

		stage.setTitle(Constants.getWindow_Name());
		stage.setMaxHeight(this.windowMaxHeight);
		stage.setMaxWidth(this.windowMaxWidth);
		stage.setMinHeight(this.windowMinHeight);
		stage.setMinWidth(this.windowMinWidth);

	}

	public int getWindowHeight() {
		return windowMinHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowMinHeight = windowHeight;
	}

	public int getWindowWidth() {
		return windowMinWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowMinWidth = windowWidth;
	}

	public int getWindowMaxHeight() {
		return windowMaxHeight;
	}

	public void setWindowMaxHeight(int windowMaxHeight) {
		this.windowMaxHeight = windowMaxHeight;
	}

	public int getWindowMaxWidth() {
		return windowMaxWidth;
	}

	public void setWindowMaxWidth(int windowMaxWidth) {
		this.windowMaxWidth = windowMaxWidth;
	}

	public int getWindowMinHeight() {
		return windowMinHeight;
	}

	public void setWindowMinHeight(int windowMinHeight) {
		this.windowMinHeight = windowMinHeight;
	}

	public int getWindowMinWidth() {
		return windowMinWidth;
	}

	public void setWindowMinWidth(int windowMinWidth) {
		this.windowMinWidth = windowMinWidth;
	}

	public Stage getStage() {
		return stage;
	}

	// Close the stage
	public void handle(WindowEvent event) {
		if (UserProfile.getInstance().getUserProfileData().isShowExitDialog()) {
			Alert exitDialog = AlertManager.createYesNoDoNotShowAgainAlert(AlertType.INFORMATION,
					Constants.getYesNoCheckBoxDialogPane_ExitDialogText(), Constants.getYesNoCheckboxDialogPane_Exit());
			Optional result = exitDialog.showAndWait();
			if (((YesNoCheckboxDialogPane) exitDialog.getDialogPane()).isNotShowAgain())
				UserProfile.getInstance().getUserProfileData().setShowExitDialog(false);

			// If the user does not want to receive this warning.
			if (result.isPresent() && result.get() == ButtonType.YES) {
				cleanExit();
			}
		} else {
			cleanExit();
		}
		event.consume();
		this.stage.show();
	}

	private void cleanExit() {
		OVPNConnector.getInstance().disconnect();
		OSBridge.getInstance().writeSession();
		Platform.exit();
		System.exit(0);
	}
	
	public static void relaunch() 
	{
		OVPNConnector.getInstance().disconnect();
		OSBridge.getInstance().writeSession();
		MainView.stage.close();
		mainView = new MainView();
		try {
			mainView.start(new Stage());
		} catch (Exception e) {
			Logger.LogError(MainView.class.getName()+": Error while relaunching app. Closing app. ");
			e.printStackTrace();
			Platform.exit();
			System.exit(0);
		}
	}

}
