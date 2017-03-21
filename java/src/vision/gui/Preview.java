package vision.gui;

import vision.constants.Constants;
import vision.rawInput.RawInputListener;
import vision.tools.ColoredPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/** Created by Simon Rovder */
public class Preview extends JFrame implements RawInputListener {

  public static final Preview preview = new Preview();
  public JLabel imageLabel;
  private ArrayList<PreviewSelectionListener> listeners;
  private BufferedImage drawnImage;
  private BufferedImage originalImage;

  private Preview() {
    super("Preview");

    this.listeners = new ArrayList<PreviewSelectionListener>();

    this.setSize(Constants.INPUT_WIDTH, Constants.INPUT_HEIGHT + 20);
    this.setResizable(false);
    this.imageLabel = new JLabel();
    this.getContentPane().add(this.imageLabel);

    this.imageLabel.addMouseListener(
        new MouseListener() {
          @Override
          public void mouseClicked(MouseEvent e) {
            Preview.selection(e.getX(), e.getY());
          }

          @Override
          public void mouseEntered(MouseEvent arg0) {}

          @Override
          public void mouseExited(MouseEvent arg0) {}

          @Override
          public void mousePressed(MouseEvent arg0) {}

          @Override
          public void mouseReleased(MouseEvent arg0) {}
        });
    this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    this.setVisible(false);
  }

  public static void addSelectionListener(PreviewSelectionListener listener) {
    Preview.preview.listeners.add(listener);
  }

  public static void flushToLabel() {
    if (Constants.GUI)
      Preview.preview.imageLabel.getGraphics().drawImage(Preview.preview.drawnImage, 0, 0, null);
  }

  public static Graphics getImageGraphics() {
    if (Preview.preview.drawnImage == null) {
      return null;
    }
    return Preview.preview.drawnImage.getGraphics();
  }

  private static void selection(int x, int y) {

    if (Preview.preview.originalImage == null) return;

    ColoredPoint cp = new ColoredPoint(x, y, new Color(Preview.preview.originalImage.getRGB(x, y)));
    for (PreviewSelectionListener psl : Preview.preview.listeners) {
      psl.previewClickHandler(cp);
    }
  }

  static BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }

  public void nextFrame(BufferedImage bi, long time) {
    Preview.preview.originalImage = bi;
    Preview.preview.drawnImage = deepCopy(bi);
  }
}
