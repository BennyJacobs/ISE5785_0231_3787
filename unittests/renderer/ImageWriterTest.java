package renderer;

import primitives.Color;
import renderer.ImageWriter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {
    @Test
    void writeImageTest() {
        final int nX = 800;
        final int nY = 500;
        ImageWriter imageWriter = new ImageWriter(nX, nY);
        final Color gridColor = new Color(255, 0, 0);
        final Color viewPlaneColor = new Color(255, 255, 255);
        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {
                if (x%50 == 0 || y%50 == 0)
                    imageWriter.writePixel(x, y,gridColor);
                else
                    imageWriter.writePixel(x, y,viewPlaneColor);
            }
        }
        imageWriter.writeToImage("testGrid");
    }

}