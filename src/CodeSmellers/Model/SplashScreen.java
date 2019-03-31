package CodeSmellers.Model;
 
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SplashScreen extends JFrame{

    private JLabel imglabel;
    private ImageIcon img;
    private static JProgressBar pbar;
    Thread t = null;
    JPanel pane = new JPanel();
    
 public SplashScreen(){
	 
     super("Splash");
     
     setSize(730, 730);
     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     setLocationRelativeTo(null);
     setUndecorated(true);
     
     img = new ImageIcon(getClass().getResource("Skunk.png"));
     imglabel = new JLabel(img);
     imglabel.setBounds(0, 0, 730, 730);
     
     Container con = this.getContentPane();
     pane.add(imglabel);
	 pane.setBounds(0, 0, 730, 730);
	 pane.setBackground(new Color(255, 255, 255));
	 con.add(pane);
	 
     setLayout(null);
     
     // Set text colour of progress bar 
     UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
     UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
     
     pbar = new JProgressBar();
     pbar.setMinimum(0);
     pbar.setMaximum(100);
     pbar.setStringPainted(true);
     pbar.setForeground(Color.BLACK);
     pbar.setBackground(Color.WHITE);
     pbar.setPreferredSize(new Dimension(310, 30));
     pbar.setBounds(0, 290, 404, 20);
     pbar.setLocation(150, 700);
     con.add(pbar);
     
     setVisible(true);

     Thread t = new Thread() {

         public void run() {
             int i = 0;
             while (i <= 100) {
                 pbar.setValue(i);
                 try {
                     sleep(20);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 i++;
             }
         }
     };
     
     t.start();
}
}
