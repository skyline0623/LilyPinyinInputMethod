package Building;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeiboCorpus {
	static void readXML(String xmlpath, String outputPath){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		File f = new File(outputPath);
		
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(xmlpath);
			
			Element root = dom.getDocumentElement();
			NodeList nl = root.getChildNodes();
			
			if(nl != null && nl.getLength() > 0){
				int len = nl.getLength();
				for(int i = 0; i < len; i++){
					Element item = (Element)nl.item(i);
					String article = item.getAttribute("article");
					Pattern pat = Pattern.compile("@[a-zA-z\u4e00-\u9fa5_0-9]+");
					Matcher m = pat.matcher(article);
					String s = m.replaceAll("");
					bw.write(s + "¡£");
				}
			}
			bw.close();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
				
	}
	public static void main(String[] args){
		
	}
}
