package com.movcmpret.dialog.consoledialog;

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



import java.util.ArrayList;
import java.util.List;

import com.movcmpret.constants.Constants;
import com.movcmpret.dialog.OutputDialogController;
import com.movcmpret.dialog.OutputDialogView;
import com.movcmpret.event.EventManager;
import com.movcmpret.utility.Logger;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ConsoleOutputDialog extends OutputDialogController<OutputDialogView>
{	
	public static final int WINDOW_WIDTH = 650;
	public static final int WINDOW_HEIGHT= 300;
	
	public ConsoleOutputDialog() 
	{
		super();		
	}
	
	public void appendString(String text, Color c) 
	{
		Text t = new Text( text +"\n");
		t.setFill(c);
		t.setFont(Font.font ("Consolas", 10));
		this.view.getTextArea().getChildren().add(t);
	}
	
	public void appendString(String text) 
	{
		appendString(text, Color.BLACK);
	}
	
	@Override
	public void initializeComponent() 
	{
		this.windowHeight = WINDOW_HEIGHT;
		this.windowWidth = WINDOW_WIDTH;
		super.initializeComponent();
		EventManager.getInstance().registerController(this);
		updateTexts();
	}

	@Override
	public void updateTexts() {
		
		this.stage.setTitle(Constants.getConsoleOutputDialog_Title());
		this.view.getTopLabel().setText(Constants.getConsoleOutputDialog_TopLabelText());	
		Logger.LogDebug(this.getClass().getName() + " Texts updated");
	}

	//clears the textbox
	public void clear() 
	{
		this.view.getTextArea().getChildren().clear();
	}
	@Override
	public void refreshViews() {}

	@Override
	public void bind() {}

	public List<String> getFullText() 
	{
		List<String> texts = new ArrayList<String>();
		for (Node node : this.view.getTextArea().getChildren()) {
		    if (node instanceof Text) {
		    	texts.add(((Text)node).textProperty().getValue());
		    }
		}
		return texts;
	}

	@Override
	public OutputDialogView createView() {
		return new OutputDialogView();
	}
	
	public void setLogText(List<String> list) 
	{
		for(String s : list)
			this.appendString(s);
	}
	
}
