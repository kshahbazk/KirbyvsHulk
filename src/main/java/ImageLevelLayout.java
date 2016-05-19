import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by johnfranklin on 5/15/16.
 */
public class ImageLevelLayout extends LevelLayout {
    //How will this be defined?
    //for red value:
    //0 - 11 is sunset tiles:
    //12- 17 is clouds tiles; 13 + 14 = large cloud, 15 is blank blue, 16 is small cloud.
    //255 is portal.
    //200 is laser enemy left.
    //220 is laser enemy right.
    //anything else is just passed to the image array.
    //Protagonist start? = 247
    public ImageLevelLayout(String filepath)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int w = image.getWidth();
        int h = image.getHeight();
        super.layout = new int[w][h];
        super.enemylayout = new Sprite[w][h];//defined like this because this should be separate from the background.
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                Color c = new Color(image.getRGB(x, y));
                //System.out.println(c);
                super.layout[y][x] = c.getRed()/15;
                if(c.getRed() == 226) {
                    super.enemylayout[y][x] = new Portal(c.getBlue(),c.getGreen());

                }
                else if(c.getRed() == 230) {
                    super.enemylayout[y][x] = new LaserEnemy(false);

                }
                else if(c.getRed() == 235) {
                    super.enemylayout[y][x] = new LaserCount();

                }
                else if(c.getRed() == 239) {
                    super.enemylayout[y][x] = new LaserEnemy(true);

                }
                else if(c.getRed() == 247)
                {
                    //System.out.println("Start Found!");
                    super.startlocation = new int[]{y,x};
                }
            }
        }

    }

}
