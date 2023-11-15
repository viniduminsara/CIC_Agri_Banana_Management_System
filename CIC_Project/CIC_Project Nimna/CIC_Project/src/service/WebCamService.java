package service;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;


//Credit
//https://gist.github.com/james-d/f826c9f38d53628114124a56fb7c4557#file-webcamservice-java
public class WebCamService extends Service<Image> {
    private BufferedImage bimg;

    private final Webcam cam ;

    private final WebcamResolution resolution ;

    public WebCamService(Webcam cam, WebcamResolution resolution) {
        this.cam = cam ;
        this.resolution = resolution;
        cam.setCustomViewSizes(new Dimension[] {resolution.getSize()});
        cam.setViewSize(resolution.getSize());
    }

    public WebCamService(Webcam cam) {
        this(cam, WebcamResolution.QVGA);
    }

    @Override
    public Task<Image> createTask() {

        return new Task<Image>() {
            @Override
            protected Image call() throws Exception {



                //t1.start();
                try {
                    cam.open();
                    int i = 0;
                    while (!isCancelled()) {
                        if(i==50){
                            System.out.println("Waiting");
                            wait();
                        }

                        if (cam.isImageNew()) {
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
                            updateValue(SwingFXUtils.toFXImage(bimg, null));
                        }
                    }
                    System.out.println("Cancelled, closing cam");
                    cam.close();
                    System.out.println("Cam closed");
                    return getValue();
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
