package footballscout;

import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class TeamRoster extends Parent{

	private RESTUtil m_RESTUtil = RESTUtil.getInstance();
	private GridPane m_RadioButtonGridPane = new GridPane();
	private GridPane m_DataGridPane = new GridPane();
	/** Number of players whose detailed data is displayed */
	private int m_NumPlayers = 0;
	private ScrollPane m_RadioButtonScrollPane = new ScrollPane();
	private ScrollPane m_DataScrollPane = new ScrollPane();
	private int m_NumSelectedRadioButtons = 0;
	private HashMap<String, GridPane> m_MapDetailedDataGridPane = new HashMap<String, GridPane>();
	/** m_TotalNumPlayers = m_PlayerList.size() */
	private int m_TotalNumPlayers = 0;
	
	/** List of player IDs */
	private List<String> m_PlayerList;
	
	/** Displays the calculated indicators for players for comparison */
	private SpiderWebChart m_PlayerIndicatorsChart = null;   
	/** Stores the players who have their indicators displayed in m_PlayerIndicatorsChart */
	private Vector<String> m_PlayerIndicatorsDisplayed = new Vector<String>();
	
	private GridPane CreateDetailedDataGridPane(final String name, final String playerID, final RadioButton rb)
	{
		//==========================================================
		// Create the list of data to display for the current player

		final GridPane detailedDataGridPane = new GridPane();
		detailedDataGridPane.setStyle("-fx-border-color: #ffffff;" + 
				"-fx-border-width: 1px;" + 
				"-fx-border-radius: 4;" +
				"-fx-background-radius: 4;" +
				"-fx-background-color: linear-gradient(to bottom, #b5bcc6, #dee3e4);" +
				"-fx-padding: 15;");

		int rowPositionForData = 0;

		m_MapDetailedDataGridPane.put(name, detailedDataGridPane);
	        
		ImageView imageDecline = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("data/close_icon.png")));
		imageDecline.setFitHeight(25);
		imageDecline.setFitWidth(25);
		Button closeButton = new Button();
		closeButton.setGraphic(imageDecline);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				int removedColumnIndex = m_DataGridPane.getColumnIndex(detailedDataGridPane);
				m_DataGridPane.getChildren().remove(detailedDataGridPane);
				for(int i=removedColumnIndex+1; i<m_NumSelectedRadioButtons; ++i)
					m_DataGridPane.setColumnIndex(m_DataGridPane.getChildren().get(i), i-1);

				--m_NumSelectedRadioButtons;
				rb.setSelected(false);
			}
		});
		detailedDataGridPane.add(closeButton, 1,0);

		Label l1 = new Label("Name ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		Label l2 = new Label(name);
		l2.setStyle("-fx-font-size: 16pt");
		HBox hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		ImageView playerPicture = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("data/default_profile_picture.jpg")));
		playerPicture.setFitHeight(50);
		playerPicture.setPreserveRatio(true);
		detailedDataGridPane.add(playerPicture, 0, rowPositionForData++);

		l1 = new Label("Team name ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "teamName"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);
		++rowPositionForData;


		l1 = new Label("Total time played ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "timePlayed"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		Button btn = new Button("Pass precision");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				LineChart chart = new LineChart(name + " - Pass precision", "Match", "Pass precision", "");
				List<String> passPrecision = m_RESTUtil.getStatList(playerID, "passPrecision");
				int match = 1;
				for(String value : passPrecision)
					chart.AddData(Double.parseDouble(value), name, match++);
			}
		});
		detailedDataGridPane.add(btn, 0, rowPositionForData++);

		btn = new Button("Shot precision");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				LineChart chart = new LineChart(name + " - Shot precision", "Match", "Shot precision", "");
				List<String> passPrecision = m_RESTUtil.getStatList(playerID, "shotPrecision");
				int match = 1;
				for(String value : passPrecision)
					chart.AddData(Double.parseDouble(value), name, match++);
			}
		});
		detailedDataGridPane.add(btn, 0, rowPositionForData++);

		btn = new Button("Co-players");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO
			}
		});
		detailedDataGridPane.add(btn, 0, rowPositionForData++);

		l1 = new Label("T-shirt number ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "jerseyNumber"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		l1 = new Label("Favorite position ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "position"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		l1 = new Label("Number of yellow cards ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "yellowcard"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		l1 = new Label("Number of red cards ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "redcard"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		l1 = new Label("Number of injuries ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "injury"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		l1 = new Label("Number of balls touched ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "ballTouched"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		l1 = new Label("Number of matches ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		l2 = new Label(m_RESTUtil.getStat(playerID, "appearance"));
		l2.setStyle("-fx-font-size: 16pt");
		hbox = new HBox();
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, rowPositionForData++);

		btn = new Button("Average zone");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// TODO
			}
		});
		detailedDataGridPane.add(btn, 0, rowPositionForData++);
		
		btn = new Button("Indicators");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(m_PlayerIndicatorsChart == null)
					m_PlayerIndicatorsChart = new SpiderWebChart("Player indicators", "");
				if(!m_PlayerIndicatorsDisplayed.contains(name))
				{
					m_PlayerIndicatorsDisplayed.add(name);
					m_PlayerIndicatorsChart.AddData(Double.parseDouble(m_RESTUtil.getStat(playerID, "attack")), name, "Attack");
					m_PlayerIndicatorsChart.AddData(Double.parseDouble(m_RESTUtil.getStat(playerID, "defense")), name, "Defense");
					m_PlayerIndicatorsChart.AddData(Double.parseDouble(m_RESTUtil.getStat(playerID, "skill")), name, "Skill");
					m_PlayerIndicatorsChart.AddData(Double.parseDouble(m_RESTUtil.getStat(playerID, "physique")), name, "Physique");
					m_PlayerIndicatorsChart.AddData(Double.parseDouble(m_RESTUtil.getStat(playerID, "teamwork")), name, "Teamwork");
				}
				else
				{
					m_PlayerIndicatorsDisplayed.remove(name);
					m_PlayerIndicatorsChart.RemoveSeries(name);
				}
				if(m_PlayerIndicatorsDisplayed.isEmpty())
				{
					m_PlayerIndicatorsChart.Close();
					m_PlayerIndicatorsChart = null;
				}
			}
		});
		detailedDataGridPane.add(btn, 0, rowPositionForData++);

		//======================//
		// Drag and drop events //
		//======================//

		// See http://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm#BABHDFEJ

		// Starting the Drag-and-Drop Gesture on a Source
		detailedDataGridPane.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				/* drag was detected, start a drag-and-drop gesture*/
				/* allow any transfer mode */
				Dragboard db = detailedDataGridPane.startDragAndDrop(TransferMode.ANY);

				/* Put a string on a dragboard */
				ClipboardContent content = new ClipboardContent();
				content.putString(name);
				db.setContent(content);

				event.consume();
			}
		});

		// Handling a DRAG_OVER Event on a Target
		detailedDataGridPane.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data is dragged over the target */
				/* accept it only if it is not dragged from the same node 
				 * and if it has a string data */
				if (event.getGestureSource() != detailedDataGridPane &&
						event.getDragboard().hasString()) {
					/* allow for both copying and moving, whatever user chooses */
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}

				event.consume();
			}
		});


		// Providing Visual Feedback by a Gesture Target
		detailedDataGridPane.setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* the drag-and-drop gesture entered the target */
				/* show to the user that it is an actual gesture target */
				if (event.getGestureSource() != detailedDataGridPane &&
						event.getDragboard().hasString()) {
					detailedDataGridPane.setStyle("-fx-background-color:#87CEEB;");
				}

				event.consume();
			}
		});

		detailedDataGridPane.setOnDragExited(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* mouse moved away, remove the graphical cues */
				detailedDataGridPane.getStyleClass().removeAll();
				detailedDataGridPane.setStyle("-fx-border-color: #ffffff;" + 
						"-fx-border-width: 1px;" + 
						"-fx-border-radius: 4;" +
						"-fx-background-radius: 4;" +
						"-fx-background-color: linear-gradient(to bottom, #b5bcc6, #dee3e4);" +
						"-fx-padding: 15;");
				event.consume();
			}
		});

		// Handling a DRAG_DROPPED Event on a Target
		detailedDataGridPane.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data dropped */
				/* if there is a string data on dragboard, read it and use it */
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasString())
				{
					GridPane sourcePane = m_MapDetailedDataGridPane.get(db.getString());
					int sourceIndex = m_DataGridPane.getColumnIndex(sourcePane);
					int targetIndex = m_DataGridPane.getColumnIndex(detailedDataGridPane);
					System.out.println("Permuting pane " + sourceIndex + " with pane " + targetIndex);
					m_DataGridPane.setColumnIndex(sourcePane, targetIndex);
					m_DataGridPane.setColumnIndex(detailedDataGridPane, sourceIndex);
					success = true;
				}
				/* let the source know whether the string was successfully 
				 * transferred and used */
				event.setDropCompleted(success);

				event.consume();
			}
		});

		// We're done with drag and drop events
		//=====================================

		return detailedDataGridPane;
	}

	public void AddPlayer(final String playerID)
	{
		//=============================================
		// Add the player to the radio button grid pane

		final RadioButton rb = new RadioButton();
		rb.setFocusTraversable(false);
		rb.setSelected(false);

		final String name = m_RESTUtil.getStat(playerID, "playerName");

		// Radio button event
		rb.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				//Execute some code here for the event.
				if(rb.isSelected())
				{
					System.out.println(name + " selected");
					m_DataGridPane.add(CreateDetailedDataGridPane(name, playerID, rb), m_NumSelectedRadioButtons, 0);
					++m_NumSelectedRadioButtons;
				}
				else
				{    		
					System.out.println(name + " deselected");
					GridPane detailedDataGridPane = m_MapDetailedDataGridPane.get(name);
					int removedColumnIndex = m_DataGridPane.getColumnIndex(detailedDataGridPane);
					m_DataGridPane.getChildren().remove(detailedDataGridPane);
					for(int i=removedColumnIndex+1; i<m_NumSelectedRadioButtons; ++i)
						m_DataGridPane.setColumnIndex(m_DataGridPane.getChildren().get(i), i-1);

					--m_NumSelectedRadioButtons;
				}
			}
		});

		Label label = new Label(name);

		m_RadioButtonGridPane.add(rb, 0, m_NumPlayers);
		//m_RadioButtonGridPane.add(playerPicture, 1, m_NumPlayers);
		m_RadioButtonGridPane.add(label, 1, m_NumPlayers);
		++m_NumPlayers;
	}

	private int m_LoadNextPlayerIndex = -1;
	void LoadNextPlayer()
	{
		++m_LoadNextPlayerIndex;
		AddPlayer(m_PlayerList.get(m_LoadNextPlayerIndex));
	}

	/** Creates the GUI layout to display the players (radio button scroll pane on the left,
	 * and data scroll pane to display detailed data on the right).
	 * Also retrieves the data from the player base. However, the constructor does not
	 * populates the scroll panes. To do so you need to call LoadNextPlayer <i>m_TotalNumPlayers</i> times.
	 * We pass the scene graph as argument to allow automatic resizing.*/
	public TeamRoster(Scene scene, final ProgressBar pb)
	{
		// Retrieve the list of players from the XML
		m_PlayerList = m_RESTUtil.getPlayerList();
		m_TotalNumPlayers = m_PlayerList.size();

		m_RadioButtonScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		m_RadioButtonScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		m_RadioButtonScrollPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.1875));
		m_RadioButtonScrollPane.prefHeightProperty().bind(scene.heightProperty().multiply(0.67));
		m_RadioButtonScrollPane.setContent(m_RadioButtonGridPane);

		m_DataScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		m_DataScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		m_DataScrollPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.6875));
		m_DataScrollPane.prefHeightProperty().bind(scene.heightProperty().multiply(0.67));
		m_DataScrollPane.layoutXProperty().bind(scene.widthProperty().multiply(0.1875));
		m_DataScrollPane.setContent(m_DataGridPane);
		m_DataGridPane.setGridLinesVisible(true);
		
		this.getChildren().add(m_RadioButtonScrollPane);
		this.getChildren().add(m_DataScrollPane);
	}

	public int GetTotalNumPlayers()	{return m_TotalNumPlayers;}
}
