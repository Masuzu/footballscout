package footballscout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class MatchParser
{
	/** Key : match ID <br/>
	 * Value : team1 ID - team2 ID */
	HashMap<String, String> m_Matches = new HashMap<String, String>();

	public MatchParser()
	{

		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("data/Player-Pass.csv")));
		String line;

		try {

			while ((line = br.readLine()) != null)
			{
				// process the line
				String [] l = line.split("\t");
				if(m_Matches.containsKey(l[0]))
				{
					String teamID = m_Matches.get(l[0]);
					if(!teamID.contains("-"))
					{
						if(!teamID.equals(l[1]))
							m_Matches.put(l[0], teamID + "-" + l[1]);
					}
				}
				else
					m_Matches.put(l[0], l[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//for(Map.Entry<String, String> entry : m_Matches.entrySet())
		//System.out.println(entry.getKey() + " " + entry.getValue());
	}

	public static void main(String[] args) {
		new MatchParser();
	}

}