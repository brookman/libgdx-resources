import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class ScreenCapture extends JFrame {
   private static final long serialVersionUID = 6003600434872530363L;

   private GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
   private Robot[] robots = new Robot[screens.length];

   private BufferedImage all;

   private JTextField time = new JTextField();
   private JTextArea text = new JTextArea();
   private JButton button;

   private Color color = new Color(185, 151, 121);

   private int imageCount = 0;

   public ScreenCapture(final long interval, final String format, final long deadline, boolean onTop) {
      super("Ludum Dare T00L");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setAlwaysOnTop(onTop);

      try {
         setIconImage(new ImageIcon(ScreenCapture.class.getResource("/icon.png")).getImage());
      } catch (Exception e) {
         // NOP
      }

      setLayout(new MigLayout("ins 0, gap 0", "[grow][p]", "[p][grow][p]"));

      add(new Banner(), "span 2, growx, wrap");

      text.setBorder(BorderFactory.createLineBorder(color, 20));
      text.setFont(new Font("Lucida Console", Font.PLAIN, 80));
      text.setLineWrap(true);
      text.setWrapStyleWord(false);
      text.setBackground(color);
      add(text, "span 2, grow, wmin 10, wrap");

      time.setFocusable(false);
      time.setEditable(false);
      time.setBorder(BorderFactory.createLineBorder(color, 8));
      time.setFont(new Font("Lucida Console", Font.PLAIN, 20));
      time.setBackground(color);
      add(time, "growx");

      button = new JButton("Make movie!");
      button.setBackground(color);
      button.setOpaque(true);
      button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               makeMovie(format);
            } catch (URISyntaxException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });
      add(button, "growy");

      setSize(800, 600);
      setLocationRelativeTo(null);
      setVisible(true);

      try {
         init();
      } catch (Exception e) {
         e.printStackTrace();
      }

      File folder = new File("screens");
      if (!folder.exists()) {
         folder.mkdir();
      }
      while (new File(getName(imageCount, format)).exists()) {
         imageCount++;
      }

      final PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder().appendDays().appendSuffix(" day", " days").appendSeparator(" ").appendHours().appendSuffix(" hour", " hours")
            .appendSeparator(" ").appendMinutes().appendSuffix(" minute", " minutes").appendSeparator(" ").appendSeconds().appendSuffix(" second", " seconds").toFormatter();

      new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  capture(format);
                  Thread.sleep(interval);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      }).start();

      new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  text.getCaret().setVisible(false);
                  time.setText("Time left: " + daysHoursMinutes.print(new Period(System.currentTimeMillis(), deadline)));
                  Thread.sleep(300);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      }).start();
   }

   private void init() throws AWTException {
      for (int i = 0; i < screens.length; i++) {
         robots[i] = new Robot(screens[i]);
      }

      int totalWidth = 0;
      int maxHeight = 0;

      for (GraphicsDevice screen : screens) {
         DisplayMode mode = screen.getDisplayMode();
         totalWidth += mode.getWidth();
         maxHeight = Math.max(maxHeight, mode.getHeight());
      }

      all = new BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
   }

   private void capture(String format) throws AWTException, IOException {
      int currentX = 0;
      for (int i = 0; i < screens.length; i++) {
         GraphicsDevice screen = screens[i];
         DisplayMode mode = screen.getDisplayMode();
         BufferedImage capture = robots[i].createScreenCapture(new Rectangle(0, 0, mode.getWidth(), mode.getHeight()));
         all.getGraphics().drawImage(capture, currentX, 0, null);
         currentX += mode.getWidth();
      }

      String name = getName(imageCount, format);
      imageCount++;
      ImageIO.write(all, format, new File(name));
   }

   private String getName(int number, String format) {
      String countString = "" + number;
      while (countString.length() < 9) {
         countString = "0" + countString;
      }
      return "screens/" + countString + "." + format;
   }

   private class Banner extends JPanel {
      private static final long serialVersionUID = 1575365053179070744L;

      private Image img;

      public Banner() {
         try {
            img = new ImageIcon(ScreenCapture.class.getResource("/banner.png")).getImage();
         } catch (Exception e) {
            img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
         }
         setOpaque(true);
      }

      @Override
      public void paint(Graphics g) {
         super.paint(g);
         g.setColor(new Color(185, 151, 121));
         g.fillRect(0, 0, getWidth(), img.getHeight(null));
         g.drawImage(img, (getWidth() - img.getWidth(null)) / 2, 0, null);
      }

      @Override
      public Dimension getPreferredSize() {
         return new Dimension(img.getWidth(null), img.getHeight(null));
      }

   }

   private void makeMovie(String fileEnding) throws URISyntaxException, IOException {
      File temp = File.createTempFile("x264_temp", ".exe");
      temp.deleteOnExit();

      InputStream stream = ScreenCapture.class.getResource("/x264.exe").openStream();
      FileOutputStream out = new FileOutputStream(temp);

      byte[] buffer = new byte[200000];

      int len = 0;

      while ((len = stream.read(buffer)) != -1) {
         out.write(buffer, 0, len);
      }
      out.close();
      stream.close();

      final Process p = Runtime.getRuntime().exec(temp.getAbsolutePath() + " --crf 24 -o movie.mkv " + new File(".").getAbsolutePath() + "/screens/%09d." + fileEnding + " --fps 30");

      new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               button.setEnabled(false);
               button.setText("Processing...");
               System.out.println("waiting...");
               p.waitFor();
               System.out.println("finished");
               JOptionPane.showMessageDialog(ScreenCapture.this, "Finished!");
               button.setEnabled(true);
               button.setText("Make movie!");
            } catch (InterruptedException e) {
            }
         }
      }).start();
   }

   public static void main(String[] args) {
      try {
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("Nimbus")) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception e) {
         // Nimbus is not available
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception e2) {
            // NOP
         }
      }

      String intervalString = JOptionPane.showInputDialog("Screen capture interval (seconds)", "10");

      long interval = 10000;

      try {
         interval = Integer.parseInt(intervalString) * 1000;
      } catch (Exception e) {
         // NOP
      }

      Object formatObject = JOptionPane.showInputDialog(null, "Select an image format:", "Image format", 1, null, new String[] { "JPG (recommended)", "PNG", "BMP" }, null);

      String format = "JPG";

      try {
         format = formatObject.toString().substring(0, 3).toLowerCase();
      } catch (Exception e) {
         // NOP
      }

      String deadlineString = JOptionPane.showInputDialog("Deadline (UNIX timestamp, seconds)", "1346029200");

      long deadline = 1346029200000L;

      try {
         deadline = Long.parseLong(deadlineString) * 1000L;
      } catch (Exception e) {
         // NOP
      }

      boolean onTop = JOptionPane.showConfirmDialog(null, "Always on top?", "Question", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
      new ScreenCapture(interval, format, deadline, onTop);

   }
}
