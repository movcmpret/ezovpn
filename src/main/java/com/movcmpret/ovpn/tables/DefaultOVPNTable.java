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




/**
 * Default implementation for OVPNTable. Completely applicable for all configs
 * @author movcmpret
 *
 */
public class DefaultOVPNTable extends OVPNTable {
	
	public DefaultOVPNTable() 
	{
		super();
		initializeComponent();
		updateTexts();
	}
	
	@Override
	public void initializeComponent() {
		this.getColumns().add(fileNameColumn);
		this.getColumns().add(protocolTableColumn);
		this.getColumns().add(portTableColumn);
		setColumnWidth();
	}

	@Override
	public void updateTexts() {
		super.updateTexts();
		
	}

	@Override
	protected void setColumnWidth() {
		fileNameColumn.prefWidthProperty().bind(this.widthProperty().divide(3));
		protocolTableColumn.prefWidthProperty().bind(this.widthProperty().divide(3)); 
		portTableColumn.prefWidthProperty().bind(this.widthProperty().divide(3.07));

		
	}
	
	

}
