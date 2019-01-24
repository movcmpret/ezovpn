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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.EventManager;
import com.movcmpret.interfaces.ConnectionChanged;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.interfaces.NewMessageHandler;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.utility.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;

/**
 * Controller class for the networking tab
 * @author movcmpret
 *
 */
public class TabNetworkingController implements DefaultController, Initializable, NewMessageHandler, ConnectionChanged
{
	
	private final static int X_Resolution = 10;
	
	private double before_sent;
	
	private double before_received;
	
	
	TabNetworkingController thisInstance = null;
	
	@FXML
	StackedAreaChart<Integer, Double> rxChart;

	@FXML
	StackedAreaChart<Integer, Double> txChart;
	
	@FXML
	NumberAxis rxNumberAxisX;
	
	@FXML
	NumberAxis rxNumberAxisY;
	
	@FXML
	NumberAxis txNumberAxisX;
	
	@FXML
	NumberAxis txNumberAxisY;
	
	@FXML
	BarChart barChartTotal;
	
	@FXML
	NumberAxis numberAxisTotal;
	
	@FXML
	CategoryAxis categoryAxisTotal;
	
	Series<Integer, Double> rxSeries;
	
	Series<Integer, Double> txSeries;
	
	Series<Double, String> rxTotalSeries;
	
	Series<Double, String> txTotalSeries;
	
	public TabNetworkingController() {
		thisInstance = this;
		EventManager.getInstance().registerController(this);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		EventManager.getInstance().registerNewMessageHandler(this);
		rxSeries = new Series<Integer, Double>();
		txSeries = new Series<Integer, Double>();
		rxTotalSeries = new Series<Double, String>();
		txTotalSeries = new Series<Double, String>();
		initBarChartTotal();
		initRXChart();
		initTXChart();
		updateTexts();
	}
	
	private void initRXChart() 
	{
		rxNumberAxisY.labelProperty().set("kBit/s");
		rxNumberAxisY.setUpperBound(10000);
		rxNumberAxisX.setForceZeroInRange(false);
		rxNumberAxisX.setUpperBound(10);
		rxNumberAxisX.setTickLabelsVisible(false);
		for(int i = 0; i < X_Resolution; i++ )
		rxSeries.getData().add(new Data<Integer, Double>(i,0.));	
		
		rxChart.getData().add(rxSeries);
		rxChart.setAnimated(false);
		
		rxChart.setStyle("CHART_COLOR_1: rgba(115,194,251, 1)");
		Node fill = rxSeries.getNode().lookup(".chart-series-area-fill"); // only for AreaChart
		Node line = rxSeries.getNode().lookup(".chart-series-area-line");

		//Maya Blue
		fill.setStyle("-fx-fill: rgba(115,194,251, 0.25);");
		line.setStyle("-fx-stroke: rgba(115,194,251, 0.75);");				
	}
	
	private void initTXChart() 
	{	
		txNumberAxisY.labelProperty().set("kBit/s");
		txNumberAxisY.setUpperBound(10000);
		txNumberAxisX.setForceZeroInRange(false);
		txNumberAxisX.setUpperBound(10);
		txNumberAxisX.setTickLabelsVisible(false);
		for(int i = 0; i < X_Resolution; i++ )
		txSeries.getData().add(new Data<Integer, Double>(i,0.));	
		
		txChart.getData().add(txSeries);
		txChart.setAnimated(false);
		
		txChart.setStyle("CHART_COLOR_1:  rgba(255, 141, 25, 1)");
		Node fill = txSeries.getNode(); // only for AreaChart
		Node line = txSeries.getNode().lookup(".chart-series-area-line");

		fill.setStyle("-fx-fill: rgba(255, 141, 25, 0.25);");
		line.setStyle("-fx-stroke: rgba(255, 141, 25, 0.75);");
			
	}
	
	@SuppressWarnings("unchecked")
	private void initBarChartTotal() 
	{
		this.numberAxisTotal.labelProperty().set("MB");
		this.barChartTotal.getData().addAll(this.rxTotalSeries, this.txTotalSeries);
		
	}

			private void shiftSeriesAndAddValue( Series<Integer, Double> series, NumberAxis axis, Double value)
			{
				for(int i = 0; i <  X_Resolution-1; i++)
					series.getData().get(i).setYValue(series.getData().get(i+1).getYValue());					
				series.getData().get(X_Resolution-1).setYValue(value);
								
			}	
	
	@Override
	public void updateTexts() 
	{
		txSeries.setName(Constants.getTransmitted());
		rxSeries.setName(Constants.getReceived());	
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
	public void newMessageStreamReaderThreadWorker(String message) {	}


	@Override
	public void newMessageManagementInterface(String message) 
	{
		if(message.matches(">BYTECOUNT:.*"))
		{
			String received = message.substring(message.indexOf(":")+1, message.indexOf(","));
			String sent = message.substring(message.indexOf(",")+1);
			received = String.format("%.2f",(((double)Integer.valueOf(received).intValue()/1024*8))).replace(',', '.');
			sent = String.format("%.2f",(((double)Integer.valueOf(sent).intValue()/1024*8))).replace(',', '.');
			
			this.updateBarChar(Double.valueOf(received), Double.valueOf(sent));
			this.shiftSeriesAndAddValue(rxSeries, rxNumberAxisX, Double.valueOf(received) - before_received);
			this.shiftSeriesAndAddValue(txSeries, txNumberAxisX, Double.valueOf(sent) - before_sent);

			before_sent = Double.valueOf(sent);
			before_received = Double.valueOf(received);
		}		
	}
	
	@SuppressWarnings("unchecked")
	private void updateBarChar(Double received, Double sent) 
	{
		this.rxTotalSeries.getData()
		.setAll(new XYChart.Data<Double, String>(received/ 1024. / 8, Constants.getReceived()));
	this.txTotalSeries.getData()
		.setAll(new XYChart.Data<Double, String>(sent / 1024. / 8, Constants.getTransmitted()));
	 //set first bar color    
	  barChartTotal.lookupAll(".default-color0.chart-bar")
      .forEach(n -> n.setStyle("-fx-bar-fill: rgb(115,194,251)"));
	  
	  barChartTotal.lookupAll(".default-color1.chart-bar")
      .forEach(n -> n.setStyle("-fx-bar-fill: rgb(255, 141, 25);"));		
	}
	@Override
	public void isConnected() 
	{
		// TODO Auto-generated method stub	
	}
	@Override
	public void isDisconnected() 
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void isConnecting() 
	{
		// TODO Auto-generated method stub	
	}

}
