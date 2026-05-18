package nanogridgame;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class NanoGridFile implements Serializable {

    public NanoGridGame Game;

    NanoGridFile() {
    }

    NanoGridFile(NanoGridGame game) {
        Game = game;
    }

    public void serialize(File output) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.newDocument();

            org.w3c.dom.Element rootElement = doc.createElement("NanoGame");
            doc.appendChild(rootElement);

            NanoGridFile.this.serialize(Game.getSettings(), rootElement, doc);
            org.w3c.dom.Element e = doc.createElement("Board");
            rootElement.appendChild(e);
            NanoGridFile.this.serialize(Game.getBoard().Board, e, doc);
            e = doc.createElement("PlayColumns");
            rootElement.appendChild(e);
            NanoGridFile.this.serialize(Game.getPlayColumns(), e, doc);
            e = doc.createElement("PlayRows");
            rootElement.appendChild(e);
            NanoGridFile.this.serialize(Game.getPlayRows(), e, doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void serialize(NanoGridParameters settings, Element root, org.w3c.dom.Document doc) {
        org.w3c.dom.Element e = doc.createElement("Settings");
        root.appendChild(e);
        addValue(settings.Columns, "Columns", e, doc);
        addValue(settings.Rows, "Rows", e, doc);
        addValue(settings.MaxColumnSquares, "MaxColumnSquares", e, doc);
        addValue(settings.RowBreakChance, "RowBreakChance", e, doc);
    }

    private void addValue(int val, String name, Element parent, Document doc) {
        org.w3c.dom.Element child = doc.createElement(name);
        child.appendChild(doc.createTextNode(String.valueOf(val)));
        parent.appendChild(child);
    }

    private void serializeColumn(char[] col, Element parent, Document doc) {
        org.w3c.dom.Element cols = doc.createElement("Values");
        parent.appendChild(cols);
        String row = GetRowString(col);
        Text txt = doc.createTextNode(String.valueOf(row));
        cols.appendChild(txt);
        parent.appendChild(cols);
    }

    private String GetRowString(char[] col) {
        StringBuilder sb = new StringBuilder();
        for (char ch : col) {
            sb.append(ch == 0 ? '_' : ch);
        }
        return sb.toString();
    }

    void deserialize(File loadFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(loadFile);

            NodeList settings = doc.getElementsByTagName("Settings");
            Game.updateSettings(deserializeSettings(settings.item(0)));

            NodeList board = doc.getElementsByTagName("Board");
            Game.setBoard(deserializeBoard(board.item(0)));

            NodeList cols = doc.getElementsByTagName("PlayColumns");
            Game.setPlayColumns(deserializeBoard(cols.item(0)));

            NodeList rows = doc.getElementsByTagName("PlayRows");
            Game.setPlayRows(deserializeBoard(rows.item(0)));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            for (StackTraceElement st : ex.getStackTrace()) {
                System.err.println(st);
            }
        }
    }

    private void serialize(char[][] board, Element root, Document doc) {
        for (char[] col : board) {
            serializeColumn(col, root, doc);
        }
    }

    private NanoGridParameters deserializeSettings(Node lst) {
        NanoGridParameters p = new NanoGridParameters();
        Node n = lst.getFirstChild();
        while (n != null) {
            String name = n.getNodeName();
            String val = n.getTextContent();
            if ("Columns".equals(name)) {
                p.Columns = Integer.parseInt(val);
            } else if ("Rows".equals(name)) {
                p.Rows = Integer.parseInt(val);
            } else if ("MaxColumnSquares".equals(name)) {
                p.MaxColumnSquares = Integer.parseInt(val);
            } else if ("MaxRowSquares".equals(name)) {
                p.MaxRowSquares = Integer.parseInt(val);
            } else if ("RowBreakChance".equals(name)) {
                p.RowBreakChance = Integer.parseInt(val);
            }
            n = n.getNextSibling();
        }
        return p;
    }

    private char[][] deserializeBoard(Node lst) {
        ArrayList<char[]> outter = new ArrayList<>();
        char[] str = null;
        Node n = lst.getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String val = n.getTextContent();
                str = val.toCharArray();
                outter.add(str);
            }
            n = n.getNextSibling();
        }
        char[][] ary = new char[outter.size()][str.length];
        return outter.toArray(ary);
    }
}
