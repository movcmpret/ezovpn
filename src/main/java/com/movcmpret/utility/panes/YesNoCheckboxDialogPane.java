package com.movcmpret.utility.panes;

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

/**
 * Used to show a Yes/No + "do not show again" alert box. 
 */
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;


public class YesNoCheckboxDialogPane extends DialogPane 
{
	CheckBox doNotShowAgain;

	/**
	 * 
	 * @param Text 
	 * @param Header The bigger Text above 
	 */
	public YesNoCheckboxDialogPane(String Text, String Header) 
	{
		this(Text);
		this.setHeaderText(Header);
	}	
	
	public YesNoCheckboxDialogPane(String Text) 
	{
		doNotShowAgain = new CheckBox(Constants.getYesNoCheckboxDialogPane_DoNotShowAgain());
		this.getButtonTypes().add(ButtonType.YES);
		this.getButtonTypes().add(ButtonType.NO);

/*  |----*/  this.setExpandableContent(new Group());
/*  |  */	 this.setExpanded(true);
/*  |  */	    
/*  |  */	 this.setContentText(Text);
/*  |  */	 this.getChildren().add(doNotShowAgain);
/*  |  */			
/*  |  */			
/*  |  */	 this.setHeight(150);
/*  |  */	 this.setWidth(175);
/*  |  */		}
/*  |  */		
//	------>  gets called when setExpandableContent is called :-) 
	   @Override
	      protected Node createDetailsButton() {
	        return doNotShowAgain;
	      }
	   
	   public boolean isNotShowAgain() 
	   {
		   return this.doNotShowAgain.isSelected();
	   }

	public CheckBox getDoNotShowAgain() {
		return doNotShowAgain;
	}

	    
}
