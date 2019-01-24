package com.movcmpret.importManager.fileimport;

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



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Class for a cell in the ImportManagerListView
 * 
 * @author movcmpret
 *
 */
public class ImportManagerListViewElement {
	
	private StringProperty filePath;
	
	public ImportManagerListViewElement(String path) 
	{		
		filePath = new SimpleStringProperty(path);
	}
				
	//Get/Set
	public String getFilePath() 
	{	
		return filePath.getValue();
	}
	
	@Override
	public String toString() 
	{
		return filePath.getValue();
		
	}

	
	
}
