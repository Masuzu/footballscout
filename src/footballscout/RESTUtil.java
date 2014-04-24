package footballscout;

import java.util.List;
import java.util.ResourceBundle;

public class RESTUtil {
	private static RESTUtil instance;
	private XMLHelper player; // player's bio
	private String playerURL;
	
	private XMLHelper stat; // statistics of player per game
	private String statURL;
	
	private RESTUtil(){
		ResourceBundle rb = ResourceBundle.getBundle("config");
		// URL for the "players"
		playerURL = rb.getString("webservice.baseURL") +
				   		   rb.getString("webservice.api.players");
		// URL for the "stats" (template)
		statURL = rb.getString("webservice.baseURL") +
						 rb.getString("webservice.api.stats");
		player = new XMLHelper(playerURL);
	}
	
	public static RESTUtil getInstance(){
		if(instance==null){
			instance = new RESTUtil();
		}
		return instance;
	}
	
	// Get an (aggregated) statistic of a player
	public String getStat(String playerId, String item){
		return player.getStatById(playerId, item);
	}
	
	// Get the list of players
	public List<String> getPlayerList(){
		return player.getList("playerId");
	}
	
	// Get the statistic of all the games the player played
	// Example: 
	//	getStatList("1","numGoal") will return the goal(s) of that player's each game
	public List<String> getStatList(String playerId, String item){
		String _statURL = String.format(statURL, playerId);
		stat = new XMLHelper(_statURL);
		return stat.getList(item);
	}
	
	public static void main(String[] args){
		// Example of usage
		RESTUtil util = RESTUtil.getInstance();
		System.out.println(util.getStat("1262", "passPrecision"));
		System.out.println(util.getStatList("1262", "passPrecision"));
		System.out.println(util.getStatList("4124", "passPrecision"));
		System.out.println(util.getStat("1262", "passPrecision"));
	}
}
