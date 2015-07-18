package cambrian.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class Images {

    public static int rgba(int r, int g, int b, int a) {
        return 256 * 256 * 256 * a + 256 * 256 * r + 256 * g + b;
    }

    public static int legendScalar(double val) {
        int gray = (int)(val);
        return rgba(gray, gray, gray, 255);
    }

    public static int legendVector(double val) {
        int gray;
        if(val > 0) {
            gray = (int) (val * 30);
            return rgba(gray, gray / 2, gray / 2, 255);
        } else {
            gray = (int) (- val * 30);
            return rgba(gray / 2, gray / 2, gray, 255);
        }
    }

    public static void genPNG(String type, String filename, double[] values) {
        int size = values.length;
        int width = (int)Math.sqrt(size);

        int[] pixels = new int[size];
        if("scalar".equals(type)) {
            for (int i = 0; i < size; i++) {
                pixels[i] = legendScalar(values[i]);
            }
        } else {
            for (int i = 0; i < size; i++) {
                pixels[i] = legendVector(values[i]);
            }
        }

        BufferedImage img = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        img.getRaster().setDataElements(0, 0, width, width, pixels);
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        IIOImage iioimage = new IIOImage(img, null, null);

        try {
            FileOutputStream fos = new FileOutputStream(filename);

            ImageOutputStream os = ImageIO.createImageOutputStream(fos);
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality((float) 0.8);
            }
            if (param.canWriteProgressive()) {
                param.setProgressiveMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
            }
            writer.setOutput(os);
            writer.write(null, iioimage, param);
            writer.dispose();
            os.close();
        } catch (IOException ioe) {
        }

    }
}
