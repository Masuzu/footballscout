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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class TeamRoster extends Parent{

	private GridPane m_RadioButtonGridPane = new GridPane();
	private GridPane m_DataGridPane = new GridPane();
	private int m_NumPlayers = 0;
	private ScrollPane m_RadioButtonScrollPane = new ScrollPane();
	private ScrollPane m_DataScrollPane = new ScrollPane();
	private int m_NumSelectedRadioButtons = 0;
	
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
		
		l1 = new Label("Total time played ");
		l1.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(l1, 0, 1);
		
		Button btn = new Button("Pass precision");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(btn, 0, 2);
		
		btn = new Button("Shot precision");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
		detailedDataGridPane.add(btn, 0, 3);
		
		btn = new Button("Co-players");
		btn.setStyle("-fx-font-size: 16pt;-fx-font-weight: bold");
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
		detailedDataGridPane.add(btn, 0, 12);
		
		final String _name = name; 
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
