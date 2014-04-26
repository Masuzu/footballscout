package footballscout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindow extends Application {

	Group m_Root = new Group();
	private ComboBox m_FilterPlayerPositionComboBox = null;
	private Button m_FilterPlayerPositionButton = new Button();
	private ProgressBar m_ProgressBar = new ProgressBar();
	private VBox m_ProgressBarVBox = new VBox();
	private TeamRoster m_TeamRoaster = null;
	private Semaphore m_PlayerLoadingComplete = new Semaphore(0);
	private Scene m_Scene;

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
		m_Scene = new Scene(m_Root, 800, 600, Color.WHITE);

		// Filter combo box
		CreateFilterPlayerPositionPane();

		// Team roster
		LoadTeamRoster();

		primaryStage.setScene(m_Scene);
		primaryStage.show();
	}

	private void LoadTeamRoster()
	{
		m_TeamRoaster = new TeamRoster(m_Scene, m_ProgressBar);
		m_TeamRoaster.setLayoutX(50);
		m_TeamRoaster.setLayoutY(50);
		m_Root.getChildren().add(m_TeamRoaster);
		final int totalNumPlayers = m_TeamRoaster.GetTotalNumPlayers();

		new Thread() {
			// runnable for that thread
			public void run() {

				for(int i=0; i<totalNumPlayers/10; ++i)
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

				for(int i=0; i<totalNumPlayers/10; ++i)
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

		m_ProgressBar.setPrefSize(600, 50);
		Label l = new Label("Loading data");
		l.setStyle("-fx-font-size: 30pt");
		m_ProgressBarVBox = new VBox();
		m_ProgressBarVBox.getChildren().addAll(l, m_ProgressBar);
		m_ProgressBarVBox.setLayoutX(100);
		m_ProgressBarVBox.setLayoutY(250);
		m_ProgressBarVBox.setAlignment(Pos.CENTER);
		m_Root.getChildren().add(m_ProgressBarVBox);
	}
}