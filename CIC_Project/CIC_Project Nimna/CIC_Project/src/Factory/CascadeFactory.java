package Factory;

import org.opencv.objdetect.CascadeClassifier;

public class CascadeFactory {
    private static CascadeFactory cascadeFactory;
    private CascadeClassifier classifier;
    private CascadeFactory(){
        classifier= new CascadeClassifier();
        classifier.load("D:\\CIC Agri Banana Management System\\CIC_Project\\CIC_Project Nimna\\CIC_Project\\src\\service\\lbpcascade_frontalface.xml");
    }

    public static CascadeFactory getInstance(){
        return cascadeFactory==null ? cascadeFactory=new CascadeFactory():cascadeFactory;
    }

    public CascadeClassifier getClassifier(){
        return classifier;
    }

}
