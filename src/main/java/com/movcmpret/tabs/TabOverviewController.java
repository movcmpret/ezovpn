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
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.controlsfx.control.ToggleSwitch;

import com.movcmpret.constants.Constants;
import com.movcmpret.event.ConnectionChangedArgs;
import com.movcmpret.event.EventManager;
import com.movcmpret.interfaces.ConnectionChanged;
import com.movcmpret.interfaces.DefaultController;
import com.movcmpret.ovpn.config.NordVPNConfig;
import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.ovpn.connection.OVPNConnector;
import com.movcmpret.ovpn.tables.DefaultOVPNTable;
import com.movcmpret.ovpn.tables.NordVPNTable;
import com.movcmpret.ovpn.tables.OVPNTable;
import com.movcmpret.persistence.UserProfile;
import com.movcmpret.utility.AlertManager;
import com.movcmpret.utility.Logger;
import com.movcmpret.web.nordvpn.WebJsonParser;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TabOverviewController implements DefaultController, Initializable {
	
	//For NordVPN: Update the server load every 60 seconds.
	public static final int SERVERLOAD_THREAD_TIMEOUT = 60000;
	//ugly solution to provide the Stage
	public static TabOverviewController thisInstance = null;
	//ListData
	private ObservableList<OVPNConfig> overviewTableData;
	private List<OVPNConfig> rawOverviewTableData;
	
	private ConnectionChanged callback;

	private OVPNTable overviewTableView;	
	
	@FXML 
	private ToggleSwitch ButtonConnect;
	
	@FXML
	GridPane gridPaneOverview;

		//Connection Info / History
		@FXML
		GridPane connectionInformationPanel;
		
		@FXML
		Label connectedToLabel;
		
		@FXML
		Label connectedSinceLabel;
		
		@FXML
		Label connectedToResultLabel;
		
		@FXML
		Label connectedSinceResultLabel;
		
		@FXML
		Label connectionDateLabel;	
		
		@FXML
		Label connectionDateResultLabel;
		
		@FXML
		Label historyLabel;
		
		@FXML
		Button showLogButton;
		
		@FXML
		GridPane connectionInformationPanel2;
		
		@FXML
		Label receivedLabel;
		
		@FXML
		Label sentLabel;
		
		@FXML
		Label sentResultLabel;
		
		@FXML
		Label receivedResultLabel;
		

		
	private Thread serverLoadThread;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		InitializeComponent();
		//Register Handler
		EventManager.getInstance().registerController(this);
		thisInstance = this;
		
	}
	
	@FXML 
	 protected void showLogButtonOnAction(ActionEvent event) 
	{
		UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().showOnTop();
	}
	
	public void triggerConnectButton() 
	{
		this.ButtonConnect.setSelected(true);
		ConnectButtonOnClick(null);
	}
	
    @FXML 
    protected void ConnectButtonOnClick(MouseEvent event) {
       
    	if( this.overviewTableView.getSelectionModel().getSelectedItem() == null )
    	{
			AlertManager.showInfoToast(Constants.getImportManager_noConfigsSelected(), (Stage) ButtonConnect.getScene().getWindow());
			return;
    	}
    	
    	//Anti spamming and doubleclick protection
    	try {
			Thread.currentThread().sleep(200);
		} 
    	catch (InterruptedException e) {}
    	
    	//If connected OR Connecting: disconnect
    	if(UserProfile.getInstance().getConnectionInfo().getConnectionState().equals(ConnectionChangedArgs.CONNECTED)
    	|| UserProfile.getInstance().getConnectionInfo().getConnectionState().equals(ConnectionChangedArgs.CONNECTING))
    	{
    	EventManager.getInstance().fireConnectionChangedEvent(ConnectionChangedArgs.CONNECTING);
    	OVPNConnector.getInstance().disconnect();
    	this.ButtonConnect.setSelected(false);
    	return;
    	}
    	//Connect
    	OVPNConfig c = this.overviewTableView.getSelectionModel().getSelectedItem();
    	if(c != null)  		
    	{
        EventManager.getInstance().unRegisterConnectionChangedHandler(callback);
    	callback = new TabOverviewConnectionCallback(c);
        EventManager.getInstance().registerConnectionChangedHandler(callback);
        
        OVPNConnector.getInstance().ConnectWithCredentials( c );   
    	UserProfile.getInstance().getUserProfileData().setLastConfig(c);
    	}
    	}
    
    /**
     * Swaps the current tableView with a new one (ovpn.tables)
     * @param table
     */
    public void swapTable() 
    {
    	stopLoadFetchingThread();
    	if(UserProfile.getInstance().getUserProfileData().getOverviewTable().equals(NordVPNTable.class.getName()))
    	{
    		this.overviewTableView = new NordVPNTable();
        	startLoadFetchingThread();
    	}
    	else 
    	{
    		this.overviewTableView = new DefaultOVPNTable();
    	}
		this.gridPaneOverview.add(this.overviewTableView, 1, 1);
		GridPane.setColumnSpan(overviewTableView, Integer.MAX_VALUE);
		GridPane.setMargin(this.overviewTableView, new Insets(5,5,5,5));
		overviewTableView.setItems(overviewTableData);	
		updateTexts();
		refreshViews();	
    }
	
	public void InitializeComponent() 
	{ 		
		this.rawOverviewTableData = UserProfile.getInstance().getUserProfileData().getOverViewTab_rawOverviewTableData();
		//use swap table to initialize
		swapTable( );
		bind();
		updateTexts();
		refreshViews();	        	
	}
	@Override
	public void updateTexts() {
		
		this.overviewTableView.updateTexts();	
		//History
		this.connectedSinceLabel.setText(Constants.getConnectionHistory_ConnectedSince());
		this.connectedToLabel.setText(Constants.getConnectionHistory_ConnectedTo());
		this.connectionDateLabel.setText(Constants.getConnectionHistory_ConnectedAt());
		this.historyLabel.setText(Constants.getConnectionHistory_LastConnection());
		this.sentLabel.setText(Constants.getConnectionHistory_BytesSent());
		this.receivedLabel.setText(Constants.getConnectionHistory_BytesReceived());
		
		this.ButtonConnect.setText("");	
		Logger.LogDebug(this.getClass().getName() + " Texts updated");
	}
	@Override
	public void refreshViews() {
		overviewTableView.refresh();
		
	}
	@Override
	public void bind() {
		PopulateTableView();
		bindConnectionInformationPane();
		
	}
	/**
	 * Binds the last connection information to the Pane
	 */
	private void bindConnectionInformationPane() {
		this.connectionInformationPanel2.setVisible(false);
		this.connectionInformationPanel2.setVisible(false);
		
		//Bind "fresh" information from OVPNConnectionInfo
		int historyListSize = UserProfile.getInstance().getUserProfileData().getObservableConnectionInformationList().size();
		if (UserProfile.getInstance().getConnectionInfo().getConfig() != null) {
			this.connectedToResultLabel
					.setText(UserProfile.getInstance().getConnectionInfo().getConfig().getFileName());
			this.connectedSinceResultLabel.textProperty().bind(UserProfile.getInstance().getConnectionInfo().getTimeSpanProperty());
			this.connectionDateResultLabel.setText(UserProfile.getInstance().getConnectionInfo().getConnectionDateAsString());
			this.sentResultLabel.textProperty().bind(UserProfile.getInstance().getConnectionInfo().getSentBytesProperty());
			this.receivedResultLabel.textProperty().bind(UserProfile.getInstance().getConnectionInfo().getReceivedBytesProperty());
			this.connectionInformationPanel.setVisible(true);
			this.connectionInformationPanel2.setVisible(true);
		}
		//Bind "old" information from last ConnectionHistoryElement
		else if (historyListSize > 0) {
			this.connectedToResultLabel.setText(UserProfile.getInstance().getUserProfileData()
					.getObservableConnectionInformationList().get(historyListSize - 1).getConfig().getFileName());
			this.connectedSinceResultLabel.setText(UserProfile.getInstance().getUserProfileData()
					.getObservableConnectionInformationList().get(historyListSize - 1).getConnectionTimeDuration());
			this.connectionDateResultLabel.setText(UserProfile.getInstance().getUserProfileData()
					.getObservableConnectionInformationList().get(historyListSize - 1).getConnectionDateAsString());
			this.sentResultLabel.setText(UserProfile.getInstance().getUserProfileData()
					.getObservableConnectionInformationList().get(historyListSize - 1).getBytesSent());
			this.receivedResultLabel.setText(UserProfile.getInstance().getUserProfileData()
					.getObservableConnectionInformationList().get(historyListSize - 1).getBytesReceived());
			this.connectionInformationPanel.setVisible(true);
			//Set old console log output
			UserProfile.getInstance().getConnectionInfo().getConsoleOutputDialog().setLogText(UserProfile.getInstance()
					.getUserProfileData().getObservableConnectionInformationList().get(historyListSize - 1).getLog());
			this.connectionInformationPanel2.setVisible(true);
		}
	}

	public void setOverviewTableData(ArrayList<OVPNConfig> data) 
	{
	
		this.overviewTableData.clear();
		
		for(OVPNConfig config:data)
		{
			this.overviewTableData.add(config);
		}
		this.refreshViews();
	}
	
	public void PopulateTableView() {
    	
    	overviewTableData =  new ObservableListWrapper<OVPNConfig>(rawOverviewTableData); 
    	OVPNConfig lastConfig = UserProfile.getInstance().getUserProfileData().getLastConfig();   	
    	
    	overviewTableView.setPlaceholder(new Label(Constants.getNoDataLoaded()));
		overviewTableView.setItems(overviewTableData);	
		
    	if(lastConfig != null && overviewTableData.contains(lastConfig)) 
    	{
    		//Focus the last element
    		Platform.runLater(new Runnable()
    		{
	    		    @Override
	    		    public void run()
	    		    {
			        	int index = overviewTableData.indexOf(lastConfig);
			    		overviewTableView.scrollTo(index);
			    		overviewTableView.requestFocus();   		
			    		overviewTableView.getSelectionModel().select(index);
			    		overviewTableView.getSelectionModel().focus(index);
	    		    }
	    		});
        }
						
    }

		/*
		 * Getter/Setter
		 */
	
	public ObservableList<OVPNConfig> getOverviewTableData() {
		return overviewTableData;
	}
	
	public void setOverviewTableData(ObservableList<OVPNConfig> overviewTableData) {
		this.overviewTableData = overviewTableData;
	}
	
	public TableView getOverviewTableView() {
		return overviewTableView;
	}		
	
	public void setOverviewTableView(OVPNTable overviewTableView) {
		this.overviewTableView = overviewTableView;
	}
	
	public ToggleSwitch getButtonConnect() {
		return ButtonConnect;
	}

	public void setButtonConnect(ToggleSwitch buttonConnect) {
		ButtonConnect = buttonConnect;
	}

	/**
	 * Sets the connection-button enabled / disabled.
	 * In relation with {@code OSBridge} 
	 *  
	 * TODO: Clean this mess up. 
	 * @author movcmpret
	 *
	 */
		private class TabOverviewConnectionCallback implements ConnectionChanged{

			Timer timer;
			boolean timesUp;
			OVPNConfig config;
			
			public TabOverviewConnectionCallback(OVPNConfig conf) 
			{
				timer = new Timer();
				this.config = conf;
			}
			
			/** TODO
			 * Gets sometimes called from another thread - so we use this ugly workaround.
			 */
			@Override
			public void isConnected() 
			{
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						bindConnectionInformationPane();
						ButtonConnect.setDisable(false);
						timesUp = false;
					AlertManager.showInfoToast(String.format(Constants.getConsoleOutputDialog_Connected(),
							config.getConfigFile().getName()), (Stage) ButtonConnect.getScene().getWindow());
				}
			});
			}
			
			public void isConnecting() 
			{
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						bindConnectionInformationPane();
						ButtonConnect.setDisable(true);
						timesUp = true;
						timer.schedule(getTask(), UserProfile.getInstance().getUserProfileData().getTimeout() );
					}	});			
			}

			@Override
			public void isDisconnected() 
			{	Platform.runLater(new Runnable(){
				@Override
				public void run() {
					ButtonConnect.setDisable(false);
					ButtonConnect.setSelected(false);
					timesUp = false;
					AlertManager.showInfoToast(String.format(Constants.getConsoleOutputDialog_Disconnected(),
							config.getConfigFile().getName()), (Stage) ButtonConnect.getScene().getWindow());
				}
			});
			
			}
			//Connection Timeout timer 
		private TimerTask getTask() {
			return new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (timesUp) {
								AlertManager.showErrorToast(Constants.getConsoleOutputDialog_Timeout(),
										(Stage) ButtonConnect.getScene().getWindow());
								OVPNConnector.getInstance().disconnect();
								ButtonConnect.setDisable(false);
							}
						}
					});
				};
				};
			}
		}
		
	/**
	 * Receives the hashmap (server domain - server load) from
	 * startLoadFetchingThread and sets the new load into the NordVPN configs
	 * 
	 * @param map
	 */
		private void refreshLoads(HashMap<String, Integer> map) 
		{
			for(OVPNConfig config : this.overviewTableData)
			{
				//Recheck for NordVPNConfig
				if(config instanceof NordVPNConfig)
				{
					NordVPNConfig nordConfig = (NordVPNConfig)config;
					
					if( map.containsKey(nordConfig.getDomain()) )
					{
						nordConfig.setLoad(map.get(nordConfig.getDomain()).intValue());
					}
				}
				refreshViews();
			}
		}
		
		private void stopLoadFetchingThread()
		{
			if (serverLoadThread != null)
			{
			serverLoadThread.interrupt();
			}
		}
		
		//Let this thread run stealth
		/**
		 * Inits and starts the thread, which downloads the current serverloads for NordVPN servers
		 */
		private void startLoadFetchingThread()
		{
			serverLoadThread = new Thread(() -> {
				boolean isFirstLoop = true;
				while(true) {
			    try {
			    	if(!isFirstLoop)
				    Thread.sleep(SERVERLOAD_THREAD_TIMEOUT);
			    	isFirstLoop = false;
			    	HashMap<String, Integer> map = WebJsonParser.getInstance().getNordVPNLoadInfo();
			    	Logger.LogInfo( this.getClass().getName() +": Fetched new server load data.");
			    	Platform.runLater(new Runnable(){
						@Override
						public void run() {
					    	refreshLoads(map);
							}	
						});			    	

			    } catch (InterruptedException e) {
			        Logger.LogDebug( this.getClass().getName() +": server load Thread interrupted." + e);
			        return;
			    } catch (Exception e) {
			    	Logger.LogDebug( this.getClass().getName() +": Minor Error: Cannot fetch server load updates from NordVPN servers " + e);
				}

				}
			});
			serverLoadThread.start();
		}
		
		
	
}
