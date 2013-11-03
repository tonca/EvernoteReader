import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
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

    private static String printNote(NodeList childNodes, String out) {
        int i = 0;
        while (childNodes.item(i) != null) {
            out = printNote(childNodes.item(i).getChildNodes(), out) + "\n";
            switch (childNodes.item(i).getNodeName()) {
                case "title":
                    out += childNodes.item(i).getNodeValue();
                    break;
                case "cdata":
                    out += childNodes.item(i).getNodeValue();
                    break;
                case "date":
                    out += "date: " + childNodes.item(i).getNodeValue();
                    break;
                case "latitude":
                    out += "latitude: " + childNodes.item(i).getNodeValue();
                    break;
                case "longitude":
                    out += "longitude: " + childNodes.item(i).getNodeValue();
                    break;
                default:
                    break;
            }
            if (childNodes.item(i).getNodeName().indexOf("#") != 0) {
                out += childNodes.item(i).getNodeName() + "\n";
            } else {
                out += childNodes.item(i).getNodeValue();
            }
            i++;
            System.out.println(i);
        }
        return out;
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
                    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("noteTest.txt"), "UTF-8");
                    DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    org.w3c.dom.Document doc = dBuilder.parse(file);
                    String text = null;
                    if (doc.hasChildNodes()) {
                        text = printNote(doc.getChildNodes(), text);
                        System.out.println(text);
                    }
                    out.write(text);
                    out.flush();
                    out.close();
                } catch (ParserConfigurationException | SAXException | IOException exception) {
                    JOptionPane.showMessageDialog(this, "Error while parsing the file!");
                }
            }
        }
    }
}
