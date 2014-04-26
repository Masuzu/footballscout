package footballscout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TeamParser {
	HashMap<String, String> m_Teams = new HashMap<String, String>();
		
	public TeamParser()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("data/Team.csv")));
		String line;
		try {
			boolean bDataFound = false;
			while ((line = br.readLine()) != null)
			{
				// process the line
				String [] l = line.split(",");
				m_Teams.put(l[0], l[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
