package com.movcmpret.ovpn.tables;

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



import com.movcmpret.constants.Constants;
import com.movcmpret.dialog.editconfig.EditConfigController;
import com.movcmpret.ovpn.config.OVPNConfig;
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

public abstract class OVPNTable extends TableView<OVPNConfig>
{
	// Properties all config types share 

	protected TableColumn fileNameColumn;	
	protected TableColumn protocolTableColumn;	
	protected TableColumn portTableColumn;
	
	//Context Menu
	ContextMenu contextMenu;
	MenuItem menuItemEdit;
	MenuItem menuItemDelete;
	MenuItem menuItemConnect;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public OVPNTable() 
	{
		super();
		
		this.fileNameColumn = new TableColumn();
		this.protocolTableColumn = new TableColumn();
		this.portTableColumn = new TableColumn();
	
		fileNameColumn.setCellValueFactory(new PropertyValueFactory<OVPNConfig, String>("FileName"));   	
		protocolTableColumn.setCellValueFactory(new PropertyValueFactory<OVPNConfig, String>("Protocol"));
		portTableColumn.setCellValueFactory(new PropertyValueFactory<OVPNConfig, String>("Port"));
		
		//Context Menu
		this.initContextMenu();
	}	
	
	/**
	 *  basically used to add the desired columns ( this.getColumns().add( ... ) 
	 */
	public abstract void initializeComponent();
	
	/**
	 * Used to set the (relative) column widths
	 */
	protected abstract void setColumnWidth();
	
	public void updateTexts() 
	{
		this.protocolTableColumn.setText(Constants.getProtocol());
		this.portTableColumn.setText(Constants.getPort());
		this.fileNameColumn.setText(Constants.getFileName());
		
		this.menuItemEdit.setText(Constants.getEdit());
		this.menuItemDelete.setText(Constants.getDelete());
		this.menuItemConnect.setText(Constants.getConnect());
		
		Logger.LogInfo(this.getClass().getName() + ": Texts updated");
	}
	
	/**
	 * Initializes the ContextMenu
	 */
	
	private void initContextMenu() 
	{
		this.contextMenu = new ContextMenu();		
		this.menuItemEdit = new MenuItem();
		this.menuItemDelete = new MenuItem();
		this.menuItemConnect = new MenuItem();
		
		this.menuItemEdit.setOnAction(this::onContextMenuEditAction);
		this.menuItemDelete.setOnAction(this::onContextMenuDeleteAction);
		this.menuItemConnect.setOnAction(this::onContextMenuConnectAction);
	
		this.contextMenu.getItems().add(menuItemConnect);	
		this.contextMenu.getItems().add(menuItemEdit);
		this.contextMenu.getItems().add(menuItemDelete);
		
		this.setRowFactory(
			    new Callback<TableView<OVPNConfig>, TableRow<OVPNConfig>>() {
			        @Override
			        public TableRow<OVPNConfig> call(TableView<OVPNConfig> tableView) {
			            final TableRow<OVPNConfig> row = new TableRow<>();        
			            // only display context menu for non-null items:
			            row.contextMenuProperty().bind(
			              Bindings.when(Bindings.isNotNull(row.itemProperty()))
			              .then(contextMenu)
			              .otherwise((ContextMenu)null));
			            return row;
			    }
			});
	}
	

	public void onContextMenuEditAction(ActionEvent e)
	{
		OVPNConfig selectedItem = this.getSelectionModel().getSelectedItem();
		if (selectedItem != null) 
		{
			EditConfigController controller = EditConfigController.create();
			if(controller != null )
				controller.setup(selectedItem).show();
		}
	}

	public void onContextMenuDeleteAction(ActionEvent e) {
		OVPNConfig selectedItem = this.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			this.getItems().remove(selectedItem);
			AlertManager.showInfoToast(selectedItem.getFileName() + " " + Constants.getRemoved().toLowerCase() + ".",
					 (Stage)TabOverviewController.thisInstance.getButtonConnect().getScene().getWindow());
		}
	}
	
	public void onContextMenuConnectAction(ActionEvent e)
	{
		OVPNConfig selectedItem = this.getSelectionModel().getSelectedItem();
		if (selectedItem != null) 
		{
		TabOverviewController.thisInstance.triggerConnectButton();
		}
	}
	
	public TableColumn getProtocolTableColumn() {
		return protocolTableColumn;
	}

	
	public void setProtocolTableColumn(TableColumn protocolTableColumn) {
		this.protocolTableColumn = protocolTableColumn;
	}

	public TableColumn getPortTableColumn() {
		return portTableColumn;
	}

	public void setPortTableColumn(TableColumn portTableColumn) {
		this.portTableColumn = portTableColumn;
	}

	public TableColumn getFileNameColumn() {
		return fileNameColumn;
	}

	public void setFileNameColumn(TableColumn fileNameColumn) {
		this.fileNameColumn = fileNameColumn;
	}
	
}
