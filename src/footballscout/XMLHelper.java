package footballscout;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper {
	private Document doc;
	private static final String QUERY_ITEM = "//%s[../playerId=%s]";
	private static final String QUERY_LIST = "//%s";
	
	public XMLHelper(String url){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			URL u = new URL(url);
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestProperty("Accept", "application/xml");
			c.setRequestProperty("Content-Type", "application/xml");
			doc = db.parse(c.getInputStream());
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getStatById(String playerId, String colName){
		String query = String.format(QUERY_ITEM,colName,playerId);
		String result = null;
		// XPath
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try{
			XPathExpression exp = xpath.compile(query);
			result = (String) exp.evaluate(doc, XPathConstants.STRING);
		}catch(XPathExpressionException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getList(String colName){
		List<String> result = new ArrayList<String>();
		String query = String.format(QUERY_LIST,colName);
		// XPath
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try{
			XPathExpression exp = xpath.compile(query);
			NodeList list = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			for(int i = 0; i< list.getLength();i++){
				Node node = list.item(i);
				result.add(node.getTextContent());
			}
		}catch(XPathExpressionException e){
			e.printStackTrace();
		}
		return result;
	}
		
}
