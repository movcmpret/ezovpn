package com.movcmpret.dialog.aboutdialog;

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



import com.movcmpret.dialog.OutputDialogController;

import javafx.stage.Stage;

/**
 * A dialog, providing information about the Software. 
 */
public class AboutDialogController extends OutputDialogController<AboutDialogView>
{
	@Override
	public void initializeComponent() 
	{
		this.windowHeight = view.WINDOW_HEIGHT;
		this.windowWidth = view.WINDOW_WIDTH;
		super.initializeComponent();
		this.stage.setResizable(false);
	}
	@Override
	public void updateTexts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bind() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AboutDialogView createView() {
		return new AboutDialogView();
	}
}
