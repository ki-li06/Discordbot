package JDA.util;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TabelleFile {
    private JTable table;
    private String[] headers;
    private String[][] data;

    public TabelleFile(String[] headers, String[][] data) {
        if(headers.length != data[0].length){
            System.out.println("FEHLER -------------- Die eingaben haben unterschiedliche länge " + headers.length + " | " + data[0].length);
        }
        this.headers = headers;
        this.data = data;
        table = new JTable(data, headers);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMinWidth(150);
        }
    }
    public void writeFile (String name){
        JTableHeader header = table.getTableHeader();
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(new JScrollPane(table));
        jFrame.setSize((headers.length+1) * 150, data[0].length * 40);
        jFrame.setVisible(true);
        int w = Math.max(table.getWidth(), header.getWidth());
        int h = table.getHeight() + header.getHeight();
        jFrame.setVisible(false);

        //System.out.println("table.width: " + table.getWidth());
        //System.out.println("header.width: " + header.getWidth());
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        header.paint(g2);
        g2.translate(0, header.getHeight());
        table.paint(g2);
        g2.dispose();
        try
        {
            ImageIO.write(bi, "png", new File(name));
        }
        catch(IOException ioe)
        {
            System.out.println("write: " + ioe.getMessage());
        }
        jFrame.dispose();
    }
}