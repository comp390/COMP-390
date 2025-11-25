import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This class holds the disign system constants in one place.
 */
public class Style {
    // COLORS
    // backgroud color for window
    public static final  Color APP_BACKGROUD = new Color(247,249,252);

    // backgroud color for cards
    public static final Color CARD_BACKGROUD = Color.WHITE;

    // action blue
    public static final Color BLUE = new Color(84,105,212);

    //darker color for text
    public static final Color TEXT_DARK = new Color(50,50,93);

    //Green
    public static final Color GREEN = new Color(92,175,90);

    // lighter color
    public static final Color TEXT_GRAY = new Color(95,95,147);

    public static final Color DARK_GRAY = new Color(128,128,150);

    // light color for border
    public static final  Color BORDER_GRAY = new Color(225,225,230);

    // red for errors
    public static final Color ERROR_RED = new Color(205,61,100);

    // FONTS
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FONT_LABEL  = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_REGULAR= new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 12);

    // BORDERS
    public static final Border PADDING_LARGE = BorderFactory.createEmptyBorder(40,40,40,40);
    public static final Border PADDING_MEDIUM = BorderFactory.createEmptyBorder(10,15,10,15);
}
