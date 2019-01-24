package com.movcmpret.utility;

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



import java.util.Timer;
import java.util.TimerTask;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.movcmpret.utility.panes.YesNoCheckboxDialogPane;

/**
 * Class for Alert views
 * @author movcmpret
 *
 */
public class AlertManager {
	

	//Notification-delay in seconds 
	static final int TOAST_DELAY = 3;
	
	//Please do not create an instance OR implement Singleton! 
	private AlertManager(){}
	public static void showErrorAlert(String text, String header) 
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
		alert.showAndWait();
	}
	
	public static void showInfoAlert(String text, String header) 
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
		alert.showAndWait();
	}
	
	public static void showWarningAlert(String text, String header) 
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
		alert.showAndWait();
	}
	
	
	
	/* External Library calls for various notifications: "Controls FX" Credits to http://fxexperience.com/
	 */
	public static void showInfoBottomRightNotification(String title, String text)
	{
		 Notifications.create()
         .title(title)
         .text(text)
         .showInformation();
	}
	
	public static void showQuestionBottomRightNotification(String title, String text)
	{
		 Notifications.create()
         .title(title)
         .text(text)
         .showConfirm();
	}
	
	public static void showErrorBottomRightNotification(String title, String text)
	{
	 Notifications.create()
     .title(title)
     .text(text)
         .showError();
	}
	
	public static void showWarningBottomRightNotification(String title, String text) 
	{
		 Notifications.create()
         .title(title)
         .text(text)
         .showWarning();
	}
	
	// FIXME: Too much code doubling. Maybe switch icons via enumeration
	public static void showWarningToast(String text, Stage stage) {
		
		ImageView WarningImage = new ImageView(new Image(AlertManager.class.getResourceAsStream("warning.png"))); 
		Scene oldScene = stage.getScene();
		Parent parent = oldScene.getRoot();
		NotificationPane pane = new NotificationPane(parent);	
		pane.setGraphic(WarningImage);
		pane.setText(text);
		Scene newScene = new Scene(pane, oldScene.getWidth(), oldScene.getHeight());
		stage.setScene(newScene);
		pane.show();		
		NotificationTimer(pane);
	}
	
	
	public static void showInfoToast(String text, Stage stage) {

		ImageView ConfirmImage = new ImageView(new Image(AlertManager.class.getResourceAsStream("check.png")));
		Scene oldScene = stage.getScene();
		Parent parent = oldScene.getRoot();
		NotificationPane pane = new NotificationPane(parent);	
		pane.setGraphic(ConfirmImage);
		pane.setText(text);
		Scene newScene = new Scene(pane, oldScene.getWidth(), oldScene.getHeight());
		stage.setScene(newScene);
		pane.show();	
		NotificationTimer(pane);
	}
	
	public static void showErrorToast(String text, Stage stage) {
		
		ImageView ErrorImage = new ImageView(new Image(AlertManager.class.getResourceAsStream("cross.png"))); 
		Scene oldScene = stage.getScene();
		Parent parent = oldScene.getRoot();
		NotificationPane pane = new NotificationPane(parent);	
		pane.setGraphic(ErrorImage);
		pane.setText(text);
		Scene newScene = new Scene(pane, oldScene.getWidth(), oldScene.getHeight());
		stage.setScene(newScene);
		pane.show();		
		NotificationTimer(pane);

	}
	
	private static void NotificationTimer(NotificationPane pane)
	{
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
			pane.hide();	
			}			
		}, TOAST_DELAY*1000);
	}
	
	public static Alert createYesNoDoNotShowAgainAlert(AlertType type, String text, String header)
	{
		Alert a = new Alert(type);
		a.setDialogPane(new YesNoCheckboxDialogPane(text, header));
		return a;
	}
	
	
	
	

}
