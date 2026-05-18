package nanogridgame;

import java.io.File;
import java.io.IOException;
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

public class NanoGridFile {

    private static final int SAVE_VERSION = 2;

    private NanoGridGame game;

    NanoGridFile() {
    }

    NanoGridFile(NanoGridGame game) {
        this.game = game;
    }

    public void serialize(File output) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("NanoGame");
            rootElement.setAttribute("version", String.valueOf(SAVE_VERSION));
            doc.appendChild(rootElement);

            serializeSettings(game.getSettings(), rootElement, doc);
            Element e = doc.createElement("Board");
            rootElement.appendChild(e);
            serializeBoard(game.getBoard().getColumns(), e, doc);
            e = doc.createElement("PlayColumns");
            rootElement.appendChild(e);
            serializeBoard(game.getPlayColumns(), e, doc);
            e = doc.createElement("PlayRows");
            rootElement.appendChild(e);
            serializeBoard(game.getPlayRows(), e, doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
        } catch (Exception ex) {
            throw new IOException("Failed to save game: " + ex.getMessage(), ex);
        }
    }

    private void serializeSettings(NanoGridParameters p, Element root, Document doc) {
        Element e = doc.createElement("Settings");
        root.appendChild(e);
        addValue(p.getColumns(), "Columns", e, doc);
        addValue(p.getRows(), "Rows", e, doc);
        addValue(p.getMaxColumnSquares(), "MaxColumnSquares", e, doc);
        addValue(p.getMaxRowSquares(), "MaxRowSquares", e, doc);
        addValue(p.getRowBreakChance(), "RowBreakChance", e, doc);
    }

    private void addValue(int val, String name, Element parent, Document doc) {
        Element child = doc.createElement(name);
        child.appendChild(doc.createTextNode(String.valueOf(val)));
        parent.appendChild(child);
    }

    private void serializeColumn(char[] col, Element parent, Document doc) {
        Element cols = doc.createElement("Values");
        String row = getRowString(col);
        Text txt = doc.createTextNode(String.valueOf(row));
        cols.appendChild(txt);
        parent.appendChild(cols);
    }

    private String getRowString(char[] col) {
        StringBuilder sb = new StringBuilder();
        for (char ch : col) {
            sb.append(ch == 0 ? '_' : ch);
        }
        return sb.toString();
    }

    void deserialize(File loadFile) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(loadFile);

            NodeList settingsNodes = doc.getElementsByTagName("Settings");
            game.updateSettings(deserializeSettings(settingsNodes.item(0)));

            NodeList board = doc.getElementsByTagName("Board");
            game.setBoard(deserializeBoard(board.item(0)));

            NodeList cols = doc.getElementsByTagName("PlayColumns");
            game.setPlayColumns(deserializeBoard(cols.item(0)));

            NodeList rows = doc.getElementsByTagName("PlayRows");
            game.setPlayRows(deserializeBoard(rows.item(0)));
        } catch (Exception ex) {
            throw new IOException("Failed to load game: " + ex.getMessage(), ex);
        }
    }

    private void serializeBoard(char[][] board, Element root, Document doc) {
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
                p.setColumns(Integer.parseInt(val));
            } else if ("Rows".equals(name)) {
                p.setRows(Integer.parseInt(val));
            } else if ("MaxColumnSquares".equals(name)) {
                p.setMaxColumnSquares(Integer.parseInt(val));
            } else if ("MaxRowSquares".equals(name)) {
                p.setMaxRowSquares(Integer.parseInt(val));
            } else if ("RowBreakChance".equals(name)) {
                p.setRowBreakChance(Integer.parseInt(val));
            }
            n = n.getNextSibling();
        }
        return p;
    }

    private char[][] deserializeBoard(Node lst) throws IOException {
        ArrayList<char[]> outer = new ArrayList<>();
        Node n = lst.getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String val = n.getTextContent();
                outer.add(val.toCharArray());
            }
            n = n.getNextSibling();
        }
        if (outer.isEmpty()) {
            throw new IOException("Board data is missing or empty in save file.");
        }
        char[][] ary = new char[outer.size()][outer.get(0).length];
        return outer.toArray(ary);
    }
}
