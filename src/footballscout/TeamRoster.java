package footballscout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

	private GridPane m_RadioButtonGridPane = new GridPane();
	private GridPane m_DataGridPane = new GridPane();
	private int m_NumPlayers = 0;
	private ScrollPane m_RadioButtonScrollPane = new ScrollPane();
	private ScrollPane m_DataScrollPane = new ScrollPane();
	private int m_NumSelectedRadioButtons = 0;
	private HashMap<String, GridPane> m_MapDetailedDataGridPane = new HashMap<String, GridPane>();
	
	public void AddPlayer(String name, String picture)
	{
		ImageView playerPicture = new ImageView(new Image(new File(picture).toURI().toString()));
		playerPicture.setFitHeight(50);
		playerPicture.setPreserveRatio(true);

		final RadioButton rb = new RadioButton();
		rb.setFocusTraversable(false);
		rb.setSelected(false);
		rb.setUserData(name);

		// Create the list of data to display for the current player
		final GridPane detailedDataGridPane = new GridPane();
		HBox hbox = new HBox();
		Label l1 = new Label("Name ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		Label l2 = new Label(name);
		l2.setStyle("-fx-font-size: 16pt");
		hbox.getChildren().addAll(l1, l2);
		detailedDataGridPane.add(hbox, 0, 0);

		ImageView imageDecline = new ImageView(new Image(new File("images/close_icon.png").toURI().toString()));
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
		
		l1 = new Label("Total time played ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 1);

		Button btn = new Button("Pass precision");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO
			}
		});
		detailedDataGridPane.add(btn, 0, 2);

		btn = new Button("Shot precision");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO
			}
		});
		detailedDataGridPane.add(btn, 0, 3);

		btn = new Button("Co-players");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO
			}
		});
		detailedDataGridPane.add(btn, 0, 4);

		l1 = new Label("T-shirt number ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 5);

		l1 = new Label("Favorite position ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 6);

		l1 = new Label("Number of yellow cards ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 7);

		l1 = new Label("Number of red cards ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 8);

		l1 = new Label("Number of injuries ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 9);

		l1 = new Label("Number of balls touched ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 10);

		l1 = new Label("Number of matches ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 11);

		btn = new Button("Average zone");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO
			}
		});
		detailedDataGridPane.add(btn, 0, 12);
		detailedDataGridPane.setStyle("-fx-background-color:#F5F5F5;");

		m_MapDetailedDataGridPane.put(name, detailedDataGridPane);
		
		//======================//
		// Drag and drop events //
		//======================//

		// See http://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm#BABHDFEJ

		// Starting the Drag-and-Drop Gesture on a Source
		final String _name = name;
		detailedDataGridPane.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				/* drag was detected, start a drag-and-drop gesture*/
				/* allow any transfer mode */
				Dragboard db = detailedDataGridPane.startDragAndDrop(TransferMode.ANY);

				/* Put a string on a dragboard */
				ClipboardContent content = new ClipboardContent();
				content.putString(_name);
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
				detailedDataGridPane.setStyle("-fx-background-color:#F5F5F5;");
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

		// Radio button event
		rb.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				//Execute some code here for the event.
				if(rb.isSelected())
				{
					System.out.println(_name + " selected");
					m_DataGridPane.add(detailedDataGridPane, m_NumSelectedRadioButtons, 0);
					++m_NumSelectedRadioButtons;
				}
				else
				{    		
					System.out.println(_name + " deselected");
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
		m_RadioButtonGridPane.add(playerPicture, 1, m_NumPlayers);
		m_RadioButtonGridPane.add(label, 2, m_NumPlayers);
		++m_NumPlayers;
	}

	public TeamRoster(){
		AddPlayer("p1", "images/p1.jpg");
		AddPlayer("p2", "images/p2.jpg");
		for(int i=3;i<20;++i)
			AddPlayer("p" + i, "images/default_profile_picture.jpg");

		m_RadioButtonScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		m_RadioButtonScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		m_RadioButtonScrollPane.setPrefSize(150, 400);
		m_RadioButtonScrollPane.setContent(m_RadioButtonGridPane);

		m_DataScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		m_DataScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		m_DataScrollPane.setPrefSize(550, 400);
		m_DataScrollPane.setLayoutX(150);
		m_DataScrollPane.setContent(m_DataGridPane);
		m_DataGridPane.setGridLinesVisible(true);

		this.getChildren().add(m_RadioButtonScrollPane);
		this.getChildren().add(m_DataScrollPane);
	}
}
