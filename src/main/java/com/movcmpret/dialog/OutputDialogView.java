package com.movcmpret.dialog;

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




import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

public class OutputDialogView extends AnchorPane
{
	private static final int GRID_ROWS = 2;
	private static final int GRID_COLUMNS = 2;
	
	protected GridPane gridPane;
	protected TextFlow textArea;
	protected ScrollPane scrollPane;
	protected Label topLabel;
	
	public OutputDialogView()
	{
		super();
		gridPane = new GridPane();
		textArea = new TextFlow();
		topLabel = new Label();
		scrollPane = new ScrollPane(textArea);
		setupTextArea();
		setupGrid();
		this.getChildren().clear();
		this.getChildren().add(gridPane);
		setupAnchorPane();
	}
	
	protected void setupTextArea() 
	{

		this.textArea.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		this.scrollPane.setFitToWidth(true);
		this.scrollPane.setFitToHeight(true);
		this.scrollPane.vvalueProperty().bind(textArea.heightProperty());
	}
	
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
		
		this.gridPane.getRowConstraints().get(0).setPercentHeight(10);	
		this.gridPane.getRowConstraints().get(1).setPercentHeight(90);
		
		this.gridPane.getColumnConstraints().get(0).setPercentWidth(50);	

		this.gridPane.add(topLabel, 0, 0);
		this.gridPane.add(scrollPane, 0, 1);
		
		GridPane.setColumnSpan(scrollPane, Integer.MAX_VALUE);			
		this.gridPane.setPadding(new Insets(10,10,10,10));
		
	}
	
	public void setupAnchorPane() 
	{
		AnchorPane.setTopAnchor(gridPane, 0.0);
		AnchorPane.setBottomAnchor(gridPane, 0.0);
		AnchorPane.setLeftAnchor(gridPane, 0.0);
		AnchorPane.setRightAnchor(gridPane, 0.0);
	}

	public Label getTopLabel() 
	{
		return this.topLabel;
	}
	
	public TextFlow getTextArea() 
	{
		return this.textArea;
	}
}
