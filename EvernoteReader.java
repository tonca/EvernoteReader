/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evernotereader;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Antonio
 */
public class EvernoteReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            
        FileOutputStream fos = new FileOutputStream("/Users/Antonio/Desktop/notetest1.txt");
        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
        
	File file = new File("/Users/Antonio/Desktop/backup.xml");
 
	DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                             .newDocumentBuilder();
 
	Document doc = dBuilder.parse(file);
         
        String text = null;
	if (doc.hasChildNodes()) {
 
		text = printNote(doc.getChildNodes(), text);
                System.out.println(text); 
	}
        
        out.write(text);
 
    } catch (ParserConfigurationException | SAXException | IOException e) {
	System.out.println("eccezione!");
    }
 
    }

    private static String printNote(NodeList childNodes, String out) {
        int i=0; 
        while(childNodes.item(i) != null) {
            out = printNote(childNodes.item(i).getChildNodes(), out)+"\n";

            /*switch(childNodes.item(i).getNodeName()) {
                case "title": out = out+childNodes.item(i).getNodeValue();
                case "cdata": out = out+childNodes.item(i).getNodeValue();
                case "date": out = out+"date: "+childNodes.item(i).getNodeValue();
                case "latitude": out = out+"latitude: "+childNodes.item(i).getNodeValue();
                case "longitude": out = out+"longitude: "+childNodes.item(i).getNodeValue();
            }*/
            
            if(childNodes.item(i).getNodeName().indexOf("#")!=0) {
                out = out+childNodes.item(i).getNodeName()+"\n";
            } else {
                out = out+childNodes.item(i).getNodeValue();  
            }
            
            
            i++;
            System.out.println(i);
        }
        return out;
    }
    
}
