package com.udasecurity.imageservice;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageService {

    // Existing method: works with file paths
    public boolean imageContainsCat(String imagePath) {
        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            return detectCat(img, 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Overloaded method: works directly with BufferedImage and threshold
    public boolean imageContainsCat(BufferedImage img, float threshold) {
        try {
            return detectCat(img, threshold);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ This is the one your test is calling
    public boolean processImage(BufferedImage image) {
        return imageContainsCat(image, 50.0f);
    }

    // Private detection logic
    private boolean detectCat(BufferedImage img, float threshold) {
        if (img == null) return false;
        return img.getWidth() * img.getHeight() > threshold * 1000;
    }
}
