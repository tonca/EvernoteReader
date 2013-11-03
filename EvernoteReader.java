import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Antonio
 */

public class Enex2Text extends JFrame implements WindowListener, ActionListener {

    private JLabel text;
    private final JButton convert, select;
    private JFileChooser fc = null;
    private final Dimension screenSize;
    private File file = null;

    public static void main(String[] args) {
        new Enex2Text();
    }

    public Enex2Text() {
        super("Enex2Text");
        setSize(200, 120);
        screenSize = getToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
        setLayout(new GridLayout(3, 1));
        convert = new JButton("Convert!");
        convert.addActionListener(this);
        select = new JButton("Select *.enex");
        select.addActionListener(this);
        text = new JLabel("Select a file...");
        text.setForeground(Color.red);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        addWindowListener(this);
        add(select);
        add(convert);
        add(text);
        setResizable(false);
        setVisible(true);
    }

    private void XMLParser(NodeList childNodes, OutputStreamWriter out) {
        String extracted = "";
        for (int i = 0; i < childNodes.getLength(); i++) {
            try {
                switch (childNodes.item(i).getNodeName()) {
                    case "title":
                        out.append("<div id='title'>" + childNodes.item(i).getTextContent() + "</div>");
                        break;
                    case "content":
                        extracted = childNodes.item(i).getTextContent();
                        out.append("<div id='content'>" + extracted.substring(extracted.indexOf("<div>") + 5, extracted.length() - 27) + "</div>");
                        break;
                    case "extracted":
                        extracted = childNodes.item(i).getTextContent();
                        out.append("<div id='extracted'>Created: " + extracted.substring(9, 11) + ":" + extracted.substring(11, 13) + ":" + extracted.substring(13, 15) + "   " + extracted.substring(6, 8) + "/" + extracted.substring(4, 6) + "/" + extracted.substring(0, 4) + "</div>");
                        break;
                    case "updated":
                        extracted = childNodes.item(i).getTextContent();
                        out.append("<div id='updated'>Updated: " + extracted.substring(9, 11) + ":" + extracted.substring(11, 13) + ":" + extracted.substring(13, 15) + "   " + extracted.substring(6, 8) + "/" + extracted.substring(4, 6) + "/" + extracted.substring(0, 4) + "</div>");
                        break;
                    case "latitude":
                        out.append("<div id='latitude'>Latitude: " + childNodes.item(i).getTextContent() + "</div>");
                        break;
                    case "longitude":
                        out.append("<div id='longitude'>Longitude: " + childNodes.item(i).getTextContent() + "</div><br />");
                        break;
                    default:
                }
                XMLParser(childNodes.item(i).getChildNodes(), out);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(this, "Error while parsing the file!");
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(select)) {
            fc = new JFileChooser();
            int rVal = fc.showOpenDialog(this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                text.setText(fc.getSelectedFile().getName());
            }
        } else if (e.getSource().equals(convert)) {
            if (file == null) {
                JOptionPane.showMessageDialog(this, "Please select a file to convert!");
            } else {
                try {
                    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file.getName() + ".html"), "UTF-8");
                    DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    org.w3c.dom.Document doc = dBuilder.parse(file);
                    if (doc.hasChildNodes()) {
                        out.append("<html><head><title>" + file.getName() + "</title><meta charset=\"UTF-8\"></head><body>");
                        XMLParser(doc.getChildNodes(), out);
                        out.append("</body></html>");
                    }
                    out.flush();
                    out.close();
                    text.setText("Select a file...");
                    file = null;
                    JOptionPane.showMessageDialog(this, "Converted!");
                } catch (ParserConfigurationException | SAXException | IOException exception) {
                    JOptionPane.showMessageDialog(this, "Error while parsing the file!");
                }
            }
        }
    }
}
