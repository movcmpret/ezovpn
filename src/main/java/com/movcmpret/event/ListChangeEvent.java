package com.movcmpret.event;

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



import java.util.List;

public class ListChangeEvent{

	private List <?> list;
	private ListChangeEventAdditionType listChangeEventAdditionType;

	/**
	 * Constructor for a {@code ListChangeEvent}
	 * @param kList The specified list.
	 */
	public ListChangeEvent(List<?> kList) 
	{
		list = kList;
		listChangeEventAdditionType = null;
	}

	/**
	 * Constructor for a {@code ListChangeEvent} with {@code ListChangeEventAdditionType}.
	 * @param kList The specified list.
	 * @param kType The  {@code ListChangeEventAdditionType}.
	 */
	public ListChangeEvent(List<?> kList, ListChangeEventAdditionType kType)
	{
		list = kList;
		listChangeEventAdditionType = kType;		
	}		
	
	public List<?> getList()
	{
		return list;
	}
	
	public ListChangeEventAdditionType getListChangeEventAdditionType()
	{
		return listChangeEventAdditionType;
	}
}
