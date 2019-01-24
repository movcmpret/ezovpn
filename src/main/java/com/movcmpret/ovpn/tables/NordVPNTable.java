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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import com.movcmpret.ovpn.config.NordVPNConfig;

/**
 * Table implementation of NordVPN Config
 * @author movcmpret
 *
 */
public class NordVPNTable extends OVPNTable
{
	protected TableColumn locationTableColumn;
	protected TableColumn idTableColumn;
	protected TableColumn loadColumn;
	
	public NordVPNTable() 
	{
		initializeComponent();
		updateTexts();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeComponent() 
	{	
		this.locationTableColumn = new TableColumn();
		this.idTableColumn= new TableColumn();
		this.loadColumn = new TableColumn();
		
		locationTableColumn.setCellValueFactory(new PropertyValueFactory<NordVPNConfig, String>("Location")); 
		idTableColumn.setCellValueFactory(new PropertyValueFactory<NordVPNConfig, String>("ID"));
		loadColumn.setCellValueFactory(new PropertyValueFactory<NordVPNConfig, Integer>("Load"));
		
		// Table cell coloring
		loadColumn.setCellFactory(new Callback<TableColumn<NordVPNConfig, Integer>, TableCell<NordVPNConfig, Integer>>() {
            @Override
            public TableCell<NordVPNConfig, Integer> call(TableColumn<NordVPNConfig, Integer> param) {
                return new TableCell<NordVPNConfig, Integer>() {

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty() && item != null) {
                        	if(item.intValue() < 30)
                        		this.setTextFill(Color.GREEN);
                        	else if(item.intValue() >= 30 && item.intValue() < 50)
                        		this.setTextFill(Color.ORANGE);
                        	else if(item.intValue() >= 50 && item.intValue() < 85 )
                        		this.setTextFill(Color.ORANGERED);
                        	else if(item.intValue() >= 85  )
                        		this.setTextFill(Color.RED);
                        	
                            setText(item.toString());

                        }
                    }

                };
            }
        });
		
		
		this.getColumns().add(locationTableColumn);
		this.getColumns().add(protocolTableColumn);
		this.getColumns().add(fileNameColumn);
		this.getColumns().add(portTableColumn);
		this.getColumns().add(idTableColumn);	
		this.getColumns().add(loadColumn);	
		
		setColumnWidth();
	}
	
	@Override
	protected void setColumnWidth() 
	{
		locationTableColumn.prefWidthProperty().bind(this.widthProperty().divide(4)); 
		protocolTableColumn.prefWidthProperty().bind(this.widthProperty().divide(10)); 
		portTableColumn.prefWidthProperty().bind(this.widthProperty().divide(10));
		idTableColumn.prefWidthProperty().bind(this.widthProperty().divide(10));
		fileNameColumn.prefWidthProperty().bind(this.widthProperty().divide(3.05));
		loadColumn.prefWidthProperty().bind(this.widthProperty().divide(8.5));
	}
	
	@Override
	public void updateTexts() 
	{	
		super.updateTexts();
		this.setPlaceholder(new Label(Constants.getNoDataLoaded()));	
		this.locationTableColumn.setText(Constants.getLocation());;
		this.idTableColumn.setText(Constants.getImportManager_Number());	
		this.loadColumn.setText(Constants.getLoad());
	}
	
	public TableColumn getLocationTableColumn() {
		return locationTableColumn;
	}

	public void setLocationTableColumn(TableColumn locationTableColumn) {
		this.locationTableColumn = locationTableColumn;
	}

	public TableColumn getIdTableColumn() {
		return idTableColumn;
	}

	public void setIdTableColumn(TableColumn idTableColumn) {
		this.idTableColumn = idTableColumn;
	}
	
}
