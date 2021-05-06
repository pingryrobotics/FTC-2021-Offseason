package Merlin;

import android.graphics.Bitmap;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Vuforia;

/**
 * https://github.com/bchay/ftc_app/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/VuMarkReader.java
 */
public class BitmapRetriever {

    private Vuforia vuforiaWrapper;
    Image rgb;

    public BitmapRetriever(Vuforia vuforiaWrapper) {
        this.vuforiaWrapper = vuforiaWrapper;
    }

    public Bitmap getBitmap() throws InterruptedException {
        VuforiaLocalizer vuforia = vuforiaWrapper.getVuforiaLocalizer();
        Bitmap bitmap = null;

        VuforiaLocalizer.CloseableFrame frame = null;
        frame = vuforia.getFrameQueue().take();
//        for (int i = 0; i < frame.getNumImages(); i++) {
        if (frame.getNumImages() > 0) {
            Image img = frame.getImage(0);
            if (img.getFormat() == PIXEL_FORMAT.RGB565) {
                rgb = frame.getImage(0);
                bitmap = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
                bitmap.copyPixelsFromBuffer(rgb.getPixels());
            }
        }

        return bitmap;
    }
}
