package com.movcmpret.dialog.aboutdialog;

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



import java.awt.Desktop;
import java.net.URI;

import javax.swing.SwingUtilities;

import com.movcmpret.constants.Constants;
import com.movcmpret.dialog.OutputDialogView;
import com.movcmpret.utility.Logger;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;



public class AboutDialogView extends OutputDialogView
{
	private static final int GRID_ROWS = 20;
	private static final int GRID_COLUMNS = 1;
	
	public final int WINDOW_HEIGHT = 375;
	public final int WINDOW_WIDTH = 275;
	ImageView backgroundImageView;
	String groupID;
	String artifactID;
	String projectName;
	String projectURL;
	String projectVersion;
	Label  projectNameAndVersion; 
	Label  groupAndArtifactID; 
	Label specialThanksTo;
	Label copyrightControlsFX;
	Label copyrightGSON;
	Label copyrightJODA;
	Label copyrightIcons;
	Label copyrightOpenVPN;
	Label copyrightNordVPN;
	Label copyrightLogo;
	
	Label infoGPL;
	Hyperlink  link; 

	public AboutDialogView() {

		try {
			backgroundImageView = new ImageView(
					new Image(this.getClass().getResourceAsStream("background_ezOVPN.png")));
		} catch (Exception e) {
			Logger.LogError(this.getClass().getName() + ": Could not get background image from resources");
		}
		groupID = Constants.getMaven_GroupID();
		artifactID = Constants.getMaven_ProjectArtifactID();
		projectName = Constants.getMaven_ProjectName();
		projectURL = Constants.getMaven_ProjectURL();
		projectVersion = Constants.getMaven_ProjectVersion();
		
		initializeComponent();
	}

	private void initializeComponent() 
	{
		this.gridPane.getChildren().remove(this.topLabel);
		
		//Content labels
		projectNameAndVersion = new Label("\n" + this.projectName + " " + this.projectVersion);
		projectNameAndVersion.setStyle("-fx-font-weight: bold");
		projectNameAndVersion.setWrapText(true);
		
		link = new Hyperlink(this.projectURL);
		link.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");	
		link.setOnAction((ActionEvent event) -> {
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			            try {
							Desktop.getDesktop().browse( new URI(link.getText()) );
						} catch (Exception e) {
							Logger.LogError(this.getClass().getName() + ": Error while opening default browser.");
						}	
			        }        
			});
		});
		
		groupAndArtifactID = new Label(groupID + ":" + artifactID);
		groupAndArtifactID.setStyle("-fx-font-style: italic; -fx-font-size: 9;");	
		groupAndArtifactID.setWrapText(true);
		
		specialThanksTo = new Label("Thanks to:");
		
		copyrightControlsFX = new Label("ControlsFX: Copyright © 2013, 2014, ControlsFX");
		copyrightControlsFX.setStyle("-fx-font-size: 9;");
		
		copyrightGSON = new Label("GSON: Copyright © 2008 Google Inc. Apache License 2.0");
		copyrightGSON.setStyle("-fx-font-size: 9;");
		
		copyrightJODA= new Label("Joda-Time: Copyright © 2002-2018 Joda.org.");
		copyrightJODA.setStyle("-fx-font-size: 9;");
		
		copyrightIcons = new Label("Icons: Atomic Lotus, Bogdan Rosu Creative");
		copyrightIcons.setStyle("-fx-font-size: 9;");
		
		copyrightOpenVPN = new Label("Copyright (c) 2009-2018 OpenVPN Inc.. All rights reserved.");
		copyrightOpenVPN.setStyle("-fx-font-size: 9;");
		
		copyrightNordVPN = new Label("NordVPN Copyright © 2012-2018 NordVPN.com");
		copyrightNordVPN.setStyle("-fx-font-size: 9;");
		
		infoGPL = new Label("Copyright (C) 2018 movcmpret.com\n" + 
				"This program comes with ABSOLUTELY NO WARRANTY; This is free software, and you are welcome to redistribute it" + 
				" under certain conditions;");
		infoGPL.setStyle("-fx-font-size: 9;");	
		infoGPL.setWrapText(true);
		
		copyrightLogo = new Label("Logo by Don Theonardo Liesanni");
		copyrightLogo.setStyle("-fx-font-size: 9;");
		
		
		
		//Background
		this.backgroundImageView.setFitHeight(275);
		this.backgroundImageView.setPreserveRatio(true);		
		StackPane transparancyLayer = new StackPane();
		transparancyLayer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
		
		//GridPane and alignments
		gridPane.add(this.backgroundImageView, 0,0);
		GridPane.setColumnSpan(this.backgroundImageView, GRID_ROWS);
		GridPane.setRowSpan(this.backgroundImageView, GRID_ROWS);
		GridPane.setHalignment(this.backgroundImageView, HPos.CENTER);
		GridPane.setValignment(this.backgroundImageView, VPos.CENTER);

		this.gridPane.add(transparancyLayer,0,0);
		GridPane.setColumnSpan(transparancyLayer, GRID_ROWS);
		GridPane.setRowSpan(transparancyLayer, GRID_ROWS);
		GridPane.setHalignment(transparancyLayer, HPos.CENTER);
		GridPane.setValignment(transparancyLayer, VPos.CENTER);
		
		this.gridPane.add(projectNameAndVersion, 0,1);	
		GridPane.setHalignment(this.projectNameAndVersion, HPos.CENTER);
		
		this.gridPane.add(groupAndArtifactID, 0,2);	
		GridPane.setHalignment(this.groupAndArtifactID, HPos.CENTER);
		GridPane.setValignment(this.groupAndArtifactID, VPos.TOP);
		
		this.gridPane.add(specialThanksTo, 0,4);	
		GridPane.setHalignment(this.specialThanksTo, HPos.CENTER);
				
		this.gridPane.add(copyrightControlsFX, 0,5);	
		GridPane.setHalignment(this.copyrightControlsFX, HPos.CENTER);
		GridPane.setValignment(this.copyrightControlsFX, VPos.TOP);
		
		this.gridPane.add(copyrightGSON, 0,5);	
		GridPane.setHalignment(this.copyrightGSON, HPos.CENTER);
		GridPane.setValignment(this.copyrightGSON, VPos.BOTTOM);
		
		this.gridPane.add(copyrightJODA, 0,6);	
		GridPane.setHalignment(this.copyrightJODA, HPos.CENTER);
		GridPane.setValignment(this.copyrightJODA, VPos.TOP);
		
		this.gridPane.add(copyrightOpenVPN, 0,6);	
		GridPane.setHalignment(this.copyrightOpenVPN, HPos.CENTER);
		GridPane.setValignment(this.copyrightOpenVPN, VPos.BOTTOM);
		
		this.gridPane.add(copyrightNordVPN, 0,7);	
		GridPane.setHalignment(this.copyrightNordVPN, HPos.CENTER);
		GridPane.setValignment(this.copyrightNordVPN, VPos.TOP);
					
		
		this.gridPane.add(copyrightIcons, 0,8);	
		GridPane.setHalignment(this.copyrightIcons, HPos.CENTER);
		GridPane.setValignment(this.copyrightIcons, VPos.TOP);
		
		this.gridPane.add(copyrightLogo, 0,8);	
		GridPane.setHalignment(this.copyrightLogo, HPos.CENTER);
		GridPane.setValignment(this.copyrightLogo, VPos.BOTTOM);
					
		this.gridPane.add(infoGPL, 0,12);	
		GridPane.setHalignment(this.infoGPL, HPos.CENTER);
					
		
		
		this.gridPane.add(link, 0,18);	
		GridPane.setHalignment(this.link, HPos.CENTER);
	
	}
	
	@Override
	protected void setupGrid() 
	{
		for(int i = 0; i != GRID_ROWS; i++) {
			
			RowConstraints c = new RowConstraints();
			c.setVgrow(Priority.ALWAYS);
			this.gridPane.getRowConstraints().add(c);	
		}

		for(int i = 0; i != GRID_COLUMNS; i++) 
		{
			ColumnConstraints c = new ColumnConstraints();
			c.setHgrow(Priority.ALWAYS);
			this.gridPane.getColumnConstraints().add(c);
		}
		this.gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	
		
	} 

}
