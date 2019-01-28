import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *  This class shows the setup for drawing animated images using Java Graphics2D.
 *  The drawing code goes in the paintComponent() method.  When the program
 *  is run, the drawing is shown in a window on the screen.
 */
public class JavaGraphics extends JPanel {

    private ImageTemplate myImages;
    private int frameNumber;

    private static int translateX = 0;
    private static int translateY = 0;
    private static double rotation = 0.0;
    private static double scaleX = 1.0;
    private static double scaleY = 1.0;


    private JavaGraphics() {
        setPreferredSize(new Dimension(800, 600));
        myImages = new ImageTemplate();
    }

    /**
     * This main() routine makes it possible to run the class JavaGraphics
     * as an application.  It simply creates a window that contains a panel
     * of type JavaGraphics.  The program ends when the user closes the
     * window by clicking its close box.
     */
    public static void main(String[] args) {
        JFrame window;
        window = new JFrame("Project 1: Lance Gundersen");
        final JavaGraphics panel = new JavaGraphics();
        window.setContentPane(panel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setResizable(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screen.width - window.getWidth()) / 2, (screen.height - window.getHeight()) / 2);
        Timer animationTimer;

        animationTimer = new Timer(1600, arg0 -> {
            if (panel.frameNumber > 6) {
                panel.frameNumber = 0;
            } else {
                panel.frameNumber++;
            }
            panel.repaint();
        });
        window.setVisible(true);
        animationTimer.start();
    }

    /**
     * The paintComponent method draws the content of the JPanel.  The parameter
     * is a graphics context that can be used for drawing on the panel.
     */
    protected void paintComponent(Graphics g) {

        BufferedImage arrowImage = myImages.getImage(ImageTemplate.arrow);
        BufferedImage thumbImage = myImages.getImage(ImageTemplate.thumb);
        BufferedImage cupImage = myImages.getImage(ImageTemplate.cup);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        applyWindowToViewportTransformation(g2, -75, 75, -75, 75, true);

        AffineTransform savedTransform = g2.getTransform();

        switch (frameNumber) {
            case 0:
                translateX = 0;
                translateY = 0;
                scaleX = 1.0;
                scaleY = 1.0;
                rotation = 0;
                break;
            case 1:
                translateX += -5;
                break;
            case 2:
                translateY += 7;
                break;
            case 3:
                rotation += 45 * Math.PI / 180.0;
                break;
            case 4:
                rotation += -90 * Math.PI / 180.0;
                break;
            case 5:
                scaleX = 2.0;
                break;
            case 6:
                scaleY = 0.5;
                break;
        }

        g2.translate(translateX, translateY);
        g2.rotate(rotation);
        g2.scale(scaleX, scaleY);
        g2.drawImage(arrowImage, 0, 0, this);
        g2.setTransform(savedTransform);

        g2.translate(translateX, translateY);
        g2.translate(-50, -25);
        g2.rotate(rotation);
        g2.scale(scaleX, scaleY);
        g2.drawImage(thumbImage, 0, 0, this);
        g2.setTransform(savedTransform);

        g2.translate(translateX, translateY);
        g2.translate(50, 25);
        g2.rotate(rotation);
        g2.scale(scaleX, scaleY);
        g2.drawImage(cupImage, 0, 0, this);
        g2.setTransform(savedTransform);
    }

    /**
     * Applies a coordinate transform to a Graphics2D graphics context.  The upper
     * left corner of the viewport where the graphics context draws is assumed to
     * be (0,0).  The coordinate transform will make a requested view window visible
     * in the drawing area.  The requested limits might be adjusted to preserve the
     * aspect ratio.
     *     This method sets the value of the global variable pixelSize, which is defined as the
     * maximum of the width of a pixel and the height of a pixel as measured in the
     * coordinate system.  (If the aspect ratio is preserved, then the width and
     * height will agree.
     * @param g2 The drawing context whose transform will be set.
     * @param left requested x-value at left of drawing area.
     * @param right requested x-value at right of drawing area.
     * @param bottom requested y-value at bottom of drawing area; can be less than
     *     top, which will reverse the orientation of the y-axis to make the positive
     *     direction point upwards.
     * @param top requested y-value at top of drawing area.
     * @param preserveAspect if preserveAspect is false, then the requested view window
     *     rectangle will exactly fill the viewport; if it is true, then the limits will be
     *     expanded in one direction, horizontally or vertically, if necessary, to make the
     *     aspect ratio of the view window match the aspect ratio of the viewport.
     *     Note that when preserveAspect is false, the units of measure in the horizontal
     *     and vertical directions will be different.
     */
    private void applyWindowToViewportTransformation(Graphics2D g2, double left, double right, double bottom, double top, boolean preserveAspect) {
        int width = getWidth();
        int height = getHeight();
        if (preserveAspect) {
            double displayAspect = Math.abs((double) height / width);
            double requestedAspect = Math.abs((bottom - top) / (right - left));
            if (displayAspect > requestedAspect) {
                double excess = (bottom - top) * (displayAspect / requestedAspect - 1);
                bottom += excess / 2;
                top -= excess / 2;
            } else if (displayAspect < requestedAspect) {
                double excess = (right - left) * (requestedAspect / displayAspect - 1);
                right += excess / 2;
                left -= excess / 2;
            }
        }
        g2.scale(width / (right - left), height / (bottom - top));
        g2.translate(-left, -top);
    }

}
