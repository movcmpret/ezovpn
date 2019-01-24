package com.movcmpret.history;

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

import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.event.ListChangeEvent;
import com.movcmpret.event.ListChangeEventAdditionType;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.ovpn.config.OVPNDefaultConfig;
import com.movcmpret.tabs.TabOverviewController;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Table for the History Tab
 * @author movcmpret
 *
 */
public class HistoryTable extends TableView<ConnectionHistoryElement> 
{

	protected TableColumn nameTableColumn;	
	protected TableColumn dateTableColumn;	
	protected TableColumn timeTableColumn;
		
		//Context Menu
		ContextMenu contextMenu;
		MenuItem menuItemAdd;
		MenuItem menuItemDelete;		
		
		@SuppressWarnings("unchecked")
		public HistoryTable() 
		{
			super();
			
			this.nameTableColumn = new TableColumn();
			this.dateTableColumn = new TableColumn();
			this.timeTableColumn = new TableColumn();
		
			nameTableColumn.setCellValueFactory(new PropertyValueFactory<ConnectionHistoryElement, String>("Name"));   	
			dateTableColumn.setCellValueFactory(new PropertyValueFactory<ConnectionHistoryElement, String>("Date"));
			timeTableColumn.setCellValueFactory(new PropertyValueFactory<ConnectionHistoryElement, String>("Time"));
			
			this.getColumns().add(nameTableColumn);
			this.getColumns().add(dateTableColumn);
			this.getColumns().add(timeTableColumn);			
			
			setColumnWidth();
			//Context Menu
			this.initContextMenu();
		}	
		
		/**
		 * Used to set the (relative) column widths
		 */
		protected void setColumnWidth() 
		{
			nameTableColumn.prefWidthProperty().bind(this.widthProperty().divide(3.05)); 
			dateTableColumn.prefWidthProperty().bind(this.widthProperty().divide(3));
			timeTableColumn.prefWidthProperty().bind(this.widthProperty().divide(3)); 
		}
		
		public void updateTexts() 
		{
			this.dateTableColumn.setText(Constants.getDate());
			this.timeTableColumn.setText(Constants.getTime());
			this.nameTableColumn.setText(Constants.getName());
			
			this.menuItemAdd.setText(Constants.getAdd());
			this.menuItemDelete.setText(Constants.getDelete());

			Logger.LogInfo(this.getClass().getName() + ": Texts updated");
		}
		
		/**
		 * Initializes the ContextMenu
		 */
		
		private void initContextMenu() 
		{
			this.contextMenu = new ContextMenu();		
			this.menuItemAdd = new MenuItem();
			this.menuItemDelete = new MenuItem();
			
			this.menuItemAdd.setOnAction(this::onContextMenuAddAction);
			this.menuItemDelete.setOnAction(this::onContextMenuDeleteAction);
		
			this.contextMenu.getItems().add(menuItemAdd);	
			this.contextMenu.getItems().add(menuItemDelete);
			
			this.setRowFactory(
				    new Callback<TableView<ConnectionHistoryElement>, TableRow<ConnectionHistoryElement>>() {
				        @Override
				        public TableRow<ConnectionHistoryElement> call(TableView<ConnectionHistoryElement> tableView) {
				            final TableRow<ConnectionHistoryElement> row = new TableRow<>();        
				            // only display context menu for non-null items:
				            row.contextMenuProperty().bind(
				              Bindings.when(Bindings.isNotNull(row.itemProperty()))
				              .then(contextMenu)
				              .otherwise((ContextMenu)null));
				            return row;
				    }
				});
		}
		

		public void onContextMenuAddAction(ActionEvent e)
		{
			ConnectionHistoryElement selectedItem = this.getSelectionModel().getSelectedItem();
		if (selectedItem != null && selectedItem.getConfig() != null) 
		{
			ArrayList<OVPNConfig> list = new ArrayList<OVPNConfig>();
			list.add(selectedItem.getConfig());
			EventManager.getInstance().fireListChangeEvent(
					new ListChangeEvent(list, ListChangeEventAdditionType.ADDITION));
			AlertManager.showInfoToast(
					Constants.getInformationAlert_ConfigsSuccessfullyAdded().replace("configs", "config")
							.replace("Konfigurationen", "Konfiguration").replace("wurden", "wurde"),
					(Stage) getScene().getWindow());
		}
		else
		{
			Logger.LogError(this.getClass().getName() + ": Error while adding config: Selected element is null.");
		}
		}

		public void onContextMenuDeleteAction(ActionEvent e) {
			ConnectionHistoryElement selectedItem = this.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				this.getItems().remove(selectedItem);
				AlertManager.showInfoToast(selectedItem.getName() + " " + Constants.getRemoved().toLowerCase() + ".",
						 (Stage)TabOverviewController.thisInstance.getButtonConnect().getScene().getWindow());
			}
		}
		
}
