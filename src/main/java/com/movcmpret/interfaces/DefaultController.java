package com.movcmpret.interfaces;

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




/*
 * Default-interface for all Controllers
 * 
 * 
 * */ 

public interface DefaultController {	

	//Sets the text depending on the language
	public abstract void updateTexts();	
	
	//Refreshes the views such as Tables and other containers
	public abstract void refreshViews();
	
	//Binds data to a view
	public abstract void bind();
}
