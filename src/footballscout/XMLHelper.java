package footballscout;

import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XMLHelper {
	private static XMLHelper instance;
	private static Document doc;
	private static final String QUERY_TEMPLATE = "//%s[../playerId=%s]";
	
	private XMLHelper(){
		ResourceBundle rb = ResourceBundle.getBundle("config");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			URL url = new URL(rb.getString("webservice.url"));
			doc = db.parse(url.openStream());
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getStatById(String playerId, String item){
		String query = String.format(QUERY_TEMPLATE,item,playerId);
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
	
	public static XMLHelper getInstance(){
		if(instance==null){
			instance = new XMLHelper();
		}
		return instance;
	}
	
	public static void main(String args[]){
		// XMLHelper example
		XMLHelper x = XMLHelper.getInstance();
		System.out.println(x.getStatById("45664", "position"));
	}
}
