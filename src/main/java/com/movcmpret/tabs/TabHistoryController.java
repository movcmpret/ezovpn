package com.movcmpret.tabs;

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
import java.util.List;
import java.util.ResourceBundle;

import com.movcmpret.event.EventManager;
import com.movcmpret.history.ConnectionHistoryElement;
import com.movcmpret.history.HistoryTable;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.persistence.UserProfile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

/**
 * Controller class for the History Tab
 * @author movcmpret
 *
 */
public class TabHistoryController implements DefaultController, Initializable
{

	@FXML
	GridPane gridPaneHistory;
	
	ObservableList<ConnectionHistoryElement> historyTableData;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		EventManager.getInstance().registerController(this);	
		initHistoryTable();
		updateTexts();
	}
	
	
	HistoryTable historyTableView;

	private void  initHistoryTable() 
	{
		this.historyTableView = new HistoryTable();
		this.historyTableData = UserProfile.getInstance().getUserProfileData().getObservableConnectionInformationList();
		
		this.gridPaneHistory.add(this.historyTableView, 0, 0);
		GridPane.setColumnSpan(historyTableView, Integer.MAX_VALUE);
		GridPane.setMargin(this.historyTableView, new Insets(5,5,5,5));
		this.historyTableView.setItems(historyTableData);	

	}
	
	@Override
	public void updateTexts() 
	{
		this.historyTableView.updateTexts();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshViews() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind() 
	{
		// TODO Auto-generated method stub
		
	}


}
