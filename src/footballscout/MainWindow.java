package footballscout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindow extends Application {

	Group m_Root = new Group();
	private ComboBox m_FilterPlayerPositionComboBox = null;
	private Button m_FilterPlayerPositionButton = new Button();
	private ProgressBar m_ProgressBar = new ProgressBar();
	private VBox m_ProgressBarVBox = new VBox();
	private TeamRoster m_TeamRoaster = null;
	private Semaphore m_PlayerLoadingComplete;

	public static void main(String[] args) {
		final String[] _args = args;

		Thread t = new Thread(new Runnable() {	
			public void run()
			{
				Application.launch(MainWindow.class, _args);
			}});  t.start();
	}

	private void CreateFilterPlayerPositionPane()
	{
		// From http://www.soccer-for-parents.com/soccer-positions.html
		ObservableList<String> options = 
				FXCollections.observableArrayList(
						"Goalkeeper",
						"Attacking Midfielder",
						"Central Defender",
						"Central Forward",
						"Central Midfielder",
						"Defensive Midfielder",
						"Fullback",
						"Defender",
						"Midfielder",
						"Striker"
						);
		m_FilterPlayerPositionComboBox = new ComboBox(options);

		GridPane gridPaneFilterComboBox = new GridPane();
		gridPaneFilterComboBox.setVgap(4);
		gridPaneFilterComboBox.setHgap(10);
		gridPaneFilterComboBox.setPadding(new Insets(5, 5, 5, 5));
		gridPaneFilterComboBox.add(new Label("Filter by player position: "), 0, 0);
		gridPaneFilterComboBox.add(m_FilterPlayerPositionComboBox, 1, 0);
		m_FilterPlayerPositionButton.setText("Filter");
		m_FilterPlayerPositionButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (m_FilterPlayerPositionComboBox.getValue() != null && !m_FilterPlayerPositionComboBox.getValue().toString().isEmpty())
					System.out.println("Applying filter " + m_FilterPlayerPositionComboBox.getValue().toString());
			}
		});
		gridPaneFilterComboBox.add(m_FilterPlayerPositionButton, 2, 0);		
		m_Root.getChildren().add(gridPaneFilterComboBox);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Football scout");
		Scene scene = new Scene(m_Root, 800, 600, Color.WHITE);

		Button btn = new Button();
		btn.setLayoutX(700);
		btn.setLayoutY(500);
		btn.setText("Test button");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				System.out.println("Test button");
			}
		});
		m_Root.getChildren().add(btn);

		btn = new Button();
		btn.setLayoutX(700);
		btn.setLayoutY(530);
		btn.setText("Test pie chart");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				PieGraph p = new PieGraph("Test pie graph", "");
				p.AddData("Category1",43.2);
				p.AddData("Category2",27.9);
				p.AddData("Category3",79.5);
			}
		});
		m_Root.getChildren().add(btn);

		btn = new Button();
		btn.setLayoutX(700);
		btn.setLayoutY(560);
		btn.setText("Test line chart");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				LineChart p = new LineChart("Line Chart Demo", "Type", "Value", "Line Chart Demo");
			}
		});
		m_Root.getChildren().add(btn);

		// Filter combo box
		CreateFilterPlayerPositionPane();

		// Team roster
		LoadTeamRoster();

		m_ProgressBar.setPrefSize(600, 50);
		Label l = new Label("Loading data");
		l.setStyle("-fx-font-size: 30pt");
		m_ProgressBarVBox = new VBox();
		m_ProgressBarVBox.getChildren().addAll(l, m_ProgressBar);
		m_ProgressBarVBox.setLayoutX(100);
		m_ProgressBarVBox.setLayoutY(250);
		m_ProgressBarVBox.setAlignment(Pos.CENTER);
		m_Root.getChildren().add(m_ProgressBarVBox);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/** TODO the goal would be to display a progress bar updating a the same time as the players are laoded
	 * Not working at all for now :/ No idea why so far
	 * 
	 * NVM scratch that the stuff above works
	 * Delete this later on*/
	private void LoadTeamRoster()
	{
		m_TeamRoaster = new TeamRoster(m_ProgressBar);
		m_TeamRoaster.setLayoutX(50);
		m_TeamRoaster.setLayoutY(50);
		m_Root.getChildren().add(m_TeamRoaster);
		final int totalNumPlayers = m_TeamRoaster.GetTotalNumPlayers();
		m_PlayerLoadingComplete = new Semaphore(0);
		
		new Thread() {
			// runnable for that thread
			public void run() {
				
				for(int i=0; i<totalNumPlayers; ++i)
				{
					final double progress = (double)(i+1)/totalNumPlayers;
					Platform.runLater(new Runnable() {
						public void run() {
							m_TeamRoaster.LoadNextPlayer();
							m_ProgressBar.setProgress(progress);
							m_PlayerLoadingComplete.release();
						}
					});
				}
			}
		}.start();
		
		// Wait for player loading completion
		new Thread() {
			// runnable for that thread
			public void run() {
				
				for(int i=0; i<totalNumPlayers; ++i)
				{
					try {
						m_PlayerLoadingComplete.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				m_ProgressBarVBox.setVisible(false);
			}
		}.start();
	}
}