package service;

import Factory.CascadeFactory;
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

public class FaceDetectionService {
    public CascadeClassifier classifier;

    public static BufferedImage mat2Img(Mat in) throws IOException {
        Imgcodecs.imwrite("D:\\Open CV\\temp\\temp.jpg",in);
        //File file = new File("C:\\Users\\user\\Desktop\\untitled\\untitled\\src\\faces\\1111.jpg");
        return ImageIO.read(new File("D:\\Open CV\\temp\\temp.jpg"));
    }

    public static Mat img2Mat(BufferedImage in) throws IOException {

        File file = new File("D:\\Open CV\\faces1111.jpg");
        ImageIO.write(in,"jpg",file);
        return Imgcodecs.imread("D:\\Open CV\\faces1111.jpg");


    }




    public BufferedImage[] getFaceDetection(BufferedImage image,int count) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //int type = CvType.CV_16UC1;
        BufferedImage[] ar = new BufferedImage[2];
        //String file = "E:/OpenCV/chap23/5555.JPG";
        //Mat src =

        //Highgui.imwrite("Lena_copy.png", newMat);

        // Reading the Image from the file and storing it in to a Matrix object
        //String file = "E:/OpenCV/chap23/5555.JPG";
        Mat src = img2Mat(image);
        Imgcodecs.imwrite("D:/Open CV/tempfacet" + ".jpg", src);
        // Instantiating the CascadeClassifier
       // String xmlFile = "lbpcascade_frontalface.xml";

        CascadeClassifier classifier = CascadeFactory.getInstance().getClassifier();
        //System.out.println(load);

        // Detecting the face in the snap
        MatOfRect faceDetections = new MatOfRect();

        classifier.detectMultiScale(src, faceDetections);
        //System.out.println(String.format("Detected %s faces",
                ///faceDetections.toArray().length));

        Rect[] rects = faceDetections.toArray();
        //System.out.print(rects.length);

        // Drawing boxes
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(
                    src,                                               // where to draw the box
                    new Point(rect.x, rect.y),                            // bottom left
                    new Point(rect.x + rect.width, rect.y + rect.height), // top right
                    new Scalar(0, 0, 255),
                    3                                                     // RGB colour
            );

           // System.out.print("Rectangle Added");
        }
        MatOfByte m = new MatOfByte();
        Imgcodecs.imencode(".jpeg", src, m);
        byte[] bytes1 = m.toArray();
        BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes1));
        ar[0]=read;
        int i = 0;
        //for (Rect rect : faceDetections.toArray()) {
        Rect[] r = faceDetections.toArray();
        if(r.length==0){
            return null;
        }
        Rect rect = r[0];
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0)); // frame is Mat
            Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
            Mat image_roi = new Mat(src, rectCrop);
            //String f = "E:/New folder/face2.jpg";
            //Mat s = Imgcodecs.imread(f);
            //MatOfByte mob = new MatOfByte();
            //boolean imencode = Imgcodecs.imencode(".jpg", s, mob);
            //byte[] bytes = mob.toArray();
            /*BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes));
            MatOfByte m = new MatOfByte();
            Imgcodecs.imencode(".jpg", image_roi, m);/*
            return;*/
           //byte[] bytes1 = m.toArray();
            //BufferedImage read1 = ImageIO.read(new ByteArrayInputStream(bytes1));

            //System.out.println(bytes.length +" : "+bytes1.length);
            //boolean compare = compare(read, read1);
            //System.out.println(compare);
            Imgcodecs.imwrite("D:\\Open CV\\temp\\facet" + count + ".jpg", image_roi);
            BufferedImage bufferedImage = mat2Img(image_roi);
            ar[1]=bufferedImage;
            //i++;
        //System.out.println("Image Processed");
        return rects.length==0 ? null:ar;
    }


    public boolean compare(BufferedImage img1, BufferedImage img2) throws IOException {
        //img1 = new BufferedImage(236, 236, img1.getType());
        //img2 = new BufferedImage(236, 236, img2.getType());
        for (int k = 0; k < 50; k++) {
            img2 = ImageIO.read(new File("E:\\New folder\\New folder\\facet" + k + ".jpg"));
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
                    if (percentage < 10) {
                        return true;
                    }
                } catch (Exception e) {

                }
            }
            return false;
        }
        return false;
    }
}
