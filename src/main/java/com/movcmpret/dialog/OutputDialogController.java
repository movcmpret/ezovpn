package com.movcmpret.dialog;

import com.movcmpret.application.MainViewController;

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



import com.movcmpret.interfaces.DefaultController;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Wrapper for an OutputDialogView. Keep it generic and create concrete classes :-). 
 * 
 * @author movcmpret
 *
 */
public abstract class OutputDialogController<T extends OutputDialogView> implements DefaultController {

	protected T view; 
	protected Scene scene;
	protected Stage stage;
	protected int windowWidth = 100;
	protected int windowHeight = 100;
	/**
	 *  Init with own stage.
	 */
	public OutputDialogController() 
	{
		view = createView();
		initializeComponent();
	}
	
	public OutputDialogController(Stage kStage) 
	{
		this();
		stage = kStage;	
		stage.setScene(scene);
	}
	
	/**
	 * Override and call for custom window size
	 */
	protected void initializeComponent() 
	{
		scene = new Scene(view, windowWidth, windowHeight);
		stage = new Stage();
		stage.getIcons().add(new Image(MainViewController.class.getResourceAsStream("ezOVPN_logo.png")));
		stage.setScene(scene);
	}
	
	public void showOnTop() 
	{
		stage.show();
		stage.setAlwaysOnTop(true);
		stage.setAlwaysOnTop(false);
	}
	
	public void show() 
	{
		stage.show();
	}
	
	public void hide() 
	{
		stage.hide();
	}
	
		
	public abstract T createView();

}
