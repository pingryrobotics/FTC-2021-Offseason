package Merlin;


//import com.robotics.Merlin.Merlin;

import android.graphics.Bitmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.translator.YoloV5Translator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;


public class MerlinPredict {


    private static final Logger logger = LoggerFactory.getLogger(MerlinPredict.class);

//    public static void main(String[] args) throws IOException, ModelException, TranslateException {
//        DetectedObjects detection = MerlinPredict.predict();
//        logger.info("{}", detection);
////        CameraTest.predict();
//    }

    public void getPrediction(Bitmap bitmap) throws IOException, ModelException, TranslateException {
//        Path imageFile = Paths.get("TeamCode/src/test/resources/robot2.jpg");
//        System.out.println(imageFile.toAbsolutePath());
//        Image img = ImageFactory.getInstance().fromFile(imageFile);

        Image img = ImageFactory.getInstance().fromImage(bitmap);

        predict(img);
    }

    private DetectedObjects predict(Image img) throws IOException, ModelException, TranslateException {


//        NDManager manager = NDManager.newBaseManager();
//        NDArray imgArr = img.toNDArray(manager);

        String backbone;
        if ("TensorFlow".equals(Engine.getInstance().getEngineName())) {
            backbone = "mobilenet_v2";
        } else {
            backbone = "resnet50";
        }

//        Criteria<Image, DetectedObjects> criteria =
//                Criteria.builder()
//                        .optApplication(Application.CV.OBJECT_DETECTION)
//                        .setTypes(Image.class, DetectedObjects.class)
//                        .optFilter("backbone", backbone)
//                        .optProgress(new ProgressBar())
//                        .build();
        System.out.println(Engine.getInstance().getEngineName());
        System.out.println(Engine.getAllEngines());
        System.out.println(Engine.getEngine("PyTorch"));

        Path modelFile = Paths.get("TeamCode/src/test/resources/yolov5s");

        Pipeline pipeline = new Pipeline();

        Translator<Image, DetectedObjects> translator = YoloV5Translator.builder()
                .optOutputType(YoloV5Translator.YoloOutputType.BOX)
                .optRescaleSize(416, 416)
//                .optThreshold(0.1f)
//                .setPipeline(pipeline)
                .build();

//        Translator<Image, DetectedObjects> translator =
//        Path modelDir = Paths.get(getFilesDir().toString(), "merlin_assets");
//        Log.d("via-d", modelDir.toString());
//        Model model = Model.newInstance("yolo", Engine.getEngine("PyTorch").getEngineName());
//        model.load(modelFile);

//        Path outputDir = Paths.get("build/output");
//        Path imagePath = outputDir.resolve("subimage.png");
//        img.getSubimage(0, 0, 2, 2).save(Files.newOutputStream(imagePath), "png");
//        saveBoundingBoxImage(img, detection);
//        return detection;

//        try (Predictor<Image, DetectedObjects> predictor = model.newPredictor(translator)) {
//                DetectedObjects detection = predictor.predict(img);
//                saveBoundingBoxImage(img, detection);
//                return detection;
//        }
//        Translator<Image, DetectedObjects> objTranslator = ObjectDetectionTranslator.builder();

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.OBJECT_DETECTION)
                        .setTypes(Image.class, DetectedObjects.class)
                        .optTranslator(translator)
                        .optModelPath(modelFile)
                        .optProgress(new ProgressBar())
                        .optEngine("PyTorch")
                        .optModelName("yolov5s.pt")
                        .build();

        try (ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(criteria)) {
            System.out.println(model);
            try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
                DetectedObjects detection = predictor.predict(img);

//                Path outputDir = Paths.get("TeamCode/build/output");
//                Path imagePath = outputDir.resolve("subimage.png");
//                img.getSubimage(0, 0, 2, 2).save(Files.newOutputStream(imagePath), "png");
                saveBoundingBoxImage(img, detection);
                logger.info("{}", detection);
                return detection;
            }
        }

//            try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
//                DetectedObjects detection = predictor.predict(img);
//                saveBoundingBoxImage(img, detection);
//                return detection;
//            }
//        }
    }

    private void saveBoundingBoxImage(Image img, DetectedObjects detection)
            throws IOException {
        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);

        // Make image copy with alpha channel because original image was jpg
        Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
        newImage.drawBoundingBoxes(detection);

        Path imagePath = outputDir.resolve("detected-dog_bike_car.png");
        // OpenJDK can't save jpg with alpha channel
        newImage.save(Files.newOutputStream(imagePath), "png");
        logger.info("Detected objects image has been saved in: {}", imagePath);
    }


}
