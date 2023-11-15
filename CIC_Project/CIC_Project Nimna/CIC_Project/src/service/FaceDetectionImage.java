package service;

import org.opencv.core.*;

import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class FaceDetectionImage {
    public static void main(String[] args) throws IOException {
        // Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Reading the Image from the file and storing it in to a Matrix object
        String file = "E:/OpenCV/chap23/5555.JPG";
        Mat src = Imgcodecs.imread(file);

        // Instantiating the CascadeClassifier
        String xmlFile = "lbpcascade_frontalface.xml";

        CascadeClassifier classifier = new CascadeClassifier(xmlFile);
        boolean load = classifier.load("C:\\Users\\Pasindu Sampath\\Desktop\\Face\\untitled\\src\\service\\lbpcascade_frontalface.xml");
        System.out.println(load);

        // Detecting the face in the snap
        MatOfRect faceDetections = new MatOfRect();

        classifier.detectMultiScale(src, faceDetections);
        System.out.println(String.format("Detected %s faces",
                faceDetections.toArray().length));

        Rect[] rects = faceDetections.toArray();


        // Drawing boxes
        /*for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(
                    src,                                               // where to draw the box
                    new Point(rect.x, rect.y),                            // bottom left
                    new Point(rect.x + rect.width, rect.y + rect.height), // top right
                    new Scalar(0, 0, 255),
                    3                                                     // RGB colour
            );

        }*/

        int i = 0;
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0)); // frame is Mat
            Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
            Mat image_roi = new Mat(src, rectCrop);
            String f = "E:/OpenCV/chap23/face2.jpg";
            Mat s = Imgcodecs.imread(f);
            MatOfByte mob = new MatOfByte();
            boolean imencode = Imgcodecs.imencode(".jpg", s, mob);
            byte[] bytes = mob.toArray();
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes));
            MatOfByte m = new MatOfByte();
            Imgcodecs.imencode(".jpg", image_roi, m);

            byte[] bytes1 = m.toArray();
            BufferedImage read1 = ImageIO.read(new ByteArrayInputStream(bytes1));

            //System.out.println(bytes.length +" : "+bytes1.length);
            boolean compare = compare(read, read1);
            System.out.println(compare);


            Imgcodecs.imwrite("E:/OpenCV/chap23/facet" + i + ".jpg", image_roi);
            i++;
        }


        // Writing the image
        //Imgcodecs.imwrite("E:/OpenCV/chap23/facedetect_output1.jpg", src);

        System.out.println("Image Processed");
    }

    public static boolean compare(BufferedImage img1, BufferedImage img2) {
         //img1 = new BufferedImage(236, 236, img1.getType());
         //img2 = new BufferedImage(236, 236, img2.getType());
        int w1 = img1.getWidth();
        int w2 = img2.getWidth();
        int h1 = img1.getHeight();
        int h2 = img2.getHeight();
        //(w1 != w2) || (h1 != h2)
        if (false) {
            System.out.println("Both images should have same dimwnsions");
        } else {
            try {
                long diff = 0;
                for (int j = 0; j < h1; j++) {
                    for (int i = 0; i < w1; i++) {
                        //Getting the RGB values of a pixel
                        int pixel1 = img1.getRGB(i, j);
                        Color color1 = new Color(pixel1, true);
                        int r1 = color1.getRed();
                        int g1 = color1.getGreen();
                        int b1 = color1.getBlue();
                        int pixel2 = img2.getRGB(i, j);
                        Color color2 = new Color(pixel2, true);
                        int r2 = color2.getRed();
                        int g2 = color2.getGreen();
                        int b2 = color2.getBlue();
                        //sum of differences of RGB values of the two images
                        long data = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
                        diff = diff + data;
                    }
                }
                double avg = diff / (w1 * h1 * 3);
                double percentage = (avg / 255) * 100;
                System.out.println("Difference: " + percentage);
                if(percentage<10){
                    return true;
                }
            } catch (Exception e) {

            }
        }
        return false;
    }
}