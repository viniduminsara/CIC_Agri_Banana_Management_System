package service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FaceUnlockThread extends Service<Image> {

    private BufferedImage bimg;

    private final Webcam cam ;

    private final WebcamResolution resolution ;

    public FaceUnlockThread(Webcam cam, WebcamResolution resolution) {
        this.cam = cam ;
        this.resolution = resolution;
        cam.setCustomViewSizes(new Dimension[] {resolution.getSize()});
        cam.setViewSize(resolution.getSize());
    }

    public FaceUnlockThread(Webcam cam) {
        this(cam, WebcamResolution.QVGA);
    }
    @Override
    protected Task<Image> createTask() {
        return new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                try {
                    //cam.open();
                    System.out.println("Called");
                    int i = 0;
                    while (i<51) {
                        if(i==50){
                            updateMessage("Face Detection Compleated");
                            break;
                        }
                        bimg = cam.getImage();
                        BufferedImage img = bimg;
                        if(bimg!=null) {
                            FaceDetectionService faceDetectionService = new FaceDetectionService();
                            try {
                                BufferedImage[] f = faceDetectionService.getFaceDetection(bimg,i);
                                if(f==null){
                                    bimg=img;
                                }else {
                                    bimg=f[0];
                                    if(f[1]!=null) {
                                        updateProgress(2*i,100);
                                        i++;
                                    }
                                }
                                updateValue(SwingFXUtils.toFXImage(bimg, null));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //String s = QRDecoder.decodeQRCode(bimg);
                                /*if(s!=null){
                                    updateMessage(s);
                                }else{
                                    try {
                                        updateProgress(20,100);
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continue;
                                }*/
                        }

                        /*try {
                            //updateProgress(100,100);
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        /*if (cam.isImageNew()) {
                            bimg = cam.getImage();
                            if(bimg!=null) {
                                FaceDetectionService faceDetectionService = new FaceDetectionService();
                                try {
                                    BufferedImage b = bimg;
                                    BufferedImage[] f = faceDetectionService.getFaceDetection(bimg,i);
                                    if(f!=null){
                                        i++;
                                        bimg=f[0];
                                        updateProgress(10,100);

                                    }else {
                                        bimg=b;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //updateProgress(100,100);
                            //updateValue(SwingFXUtils.toFXImage(bimg, null));
                        }*/
                    }
                    System.out.println("Cancelled, closing cam");
                    cam.close();
                    System.out.println("Cam closed");
                    return null;
                } finally {
                    cam.close();
                }

            }

        };
    }
    public int getCamWidth() {
        return resolution.getSize().width ;
    }

    public int getCamHeight() {
        return resolution.getSize().height ;
    }
}
