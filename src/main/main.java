/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author Elizabeth Stiles
 */
public class main {
        static Connection con = null;
        static JFrame frame = new JFrame("Image Loader");        
        static JTabbedPane tabbedPane = new JTabbedPane();
        static JScrollPane scrollPane1 = new JScrollPane();
        static JScrollPane scrollPane2 = new JScrollPane();
        static JPanel imagePanel1 = new JPanel();
        static JPanel imagePanel2 = new JPanel();

        public static void databaseConnection(){       
            String sqlURL = "jdbc:mysql://cstnt.tstc.edu:3306/";
            String username = "noobmaster";
            String password = "noobery";

            try {
                con=DriverManager.getConnection(sqlURL, username, password);
            } catch(SQLException x){
                x.printStackTrace();
            }
        }
            
        public static void getFruitImages(){
            try {
                PreparedStatement ps = con.prepareStatement("SELECT img FROM `threadingexam`.`fruit`");
                ResultSet rs = ps.executeQuery();
                
                BoxLayout boxLayout = new BoxLayout(imagePanel2, BoxLayout.Y_AXIS);
                imagePanel2.setLayout(boxLayout);

                while (rs.next()) {
                    Blob imgBlob = rs.getBlob("img"); 

                    byte[] imageBytes = imgBlob.getBytes(1, (int) imgBlob.length());  

                    //Use the byte array to create an image
                    ImageIcon img = new ImageIcon(imageBytes);

                    JPanel imgPanel = new JPanel();
                    int h = img.getIconHeight();
                    int w = img.getIconWidth();
                    double newHeight = 500/((double)w/(double)h);
                    img.setImage(img.getImage().getScaledInstance(500,(int)newHeight , 0));
                    JLabel label = new JLabel(img);            
                    Insets insets = imgPanel.getInsets();
                    Dimension size = label.getPreferredSize();
                    label.setBounds(25 + insets.left, 5 + insets.top,
                     size.width, size.height);
                    imgPanel.add(label);
                    imagePanel2.add(imgPanel);
                    imagePanel2.revalidate();
                    imagePanel2.repaint();
                }
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
        public static void getVegImages(){
            try {
                PreparedStatement ps = con.prepareStatement("SELECT img FROM `threadingexam`.`vegetable`");
                ResultSet rs = ps.executeQuery();
                
                BoxLayout boxLayout = new BoxLayout(imagePanel1, BoxLayout.Y_AXIS);
                imagePanel1.setLayout(boxLayout);

                while (rs.next()) {
                    Blob imgBlob = rs.getBlob("img"); 

                    byte[] imageBytes = imgBlob.getBytes(1, (int) imgBlob.length());  

                    //Use the byte array to create an image
                    ImageIcon img = new ImageIcon(imageBytes);

                    JPanel imgPanel = new JPanel();
                    int h = img.getIconHeight();
                    int w = img.getIconWidth();
                    double newHeight = 500/((double)w/(double)h);
                    img.setImage(img.getImage().getScaledInstance(500,(int)newHeight , 0));
                    JLabel label = new JLabel(img);            
                    Insets insets = imgPanel.getInsets();
                    Dimension size = label.getPreferredSize();
                    label.setBounds(25 + insets.left, 5 + insets.top,
                     size.width, size.height);
                    imgPanel.add(label);
                    imagePanel1.add(imgPanel);
                    imagePanel1.revalidate();
                    imagePanel1.repaint();
                }                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
        private static void createGUI(){
            frame.setVisible(true);
            frame.setSize(570,500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            scrollPane1 = new JScrollPane(imagePanel1,      
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            scrollPane2 = new JScrollPane(imagePanel2,      
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            tabbedPane.addTab("Vegetables", null, scrollPane1,
              "Shows Vegetables");              
            tabbedPane.addTab("Fruits", null, scrollPane2,
              "Shows Fruits");  
            frame.add(tabbedPane);            
        }
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        databaseConnection();
        createGUI(); 
        Thread t = new Thread(() -> {
            getFruitImages();
        });
        Thread o = new Thread(() -> {
            getVegImages();          
        });        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {    
            public void run() {
                o.start(); 
                t.start();
            }  
        });  
    }    
}
