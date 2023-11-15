package controller.face;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXProgressBar;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import org.opencv.core.Mat;
import service.*;

public class UnlockingFormController {
    public BorderPane pane;
    public Label lblName;
    public Rectangle rectangle;
    public JFXProgressBar prograss;
    Mat matrix = null;
    public ImageView imgView;
    public Label lblId;
    private FaceUnlockThread service;
    private QrPerformance ob;
    public void initialize(){
        //rectangle.setVisible(false);
        prograss.setVisible(false);
        //imgView.setVisible(false);
        Webcam cam =Webcam.getWebcams().get(0);
        //cam.open();
        service = new FaceUnlockThread(cam);

        WebCamView2 view = new WebCamView2(service);

        pane.getChildren().add(view.getView());
        cam.open();
        //setOnCloseRequest(new )

        prograss.progressProperty().bind(service.progressProperty());

        service.messageProperty().addListener((a,old,c)->{
            if(c!=null){
//                new Alert(Alert.AlertType.INFORMATION,c).show();
                ob.qrIdRequestAction("C-001");
            }
        });
    }

    public void btnStartOnAction(ActionEvent actionEvent) {
        service.reset();
        service.start();
        prograss.setVisible(true);

    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        service.cancel();
        prograss.setVisible(false);
    }

    public void setController(QrPerformance ob){
        this.ob = ob;
    }
}
