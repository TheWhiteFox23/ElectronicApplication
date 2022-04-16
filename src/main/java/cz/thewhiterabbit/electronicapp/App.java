package cz.thewhiterabbit.electronicapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class App extends Application {
    public static ResourceBundle localization  = ResourceBundle.getBundle("strings", new Locale("en", "US" ));
    public static Properties config = new Properties();
    @Override
    public void start(Stage stage) throws IOException {
        //load properties
        try (InputStream input = new FileInputStream("config.properties")) {
            config.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if(config.getProperty("locale").equals("cs_CZ")) {
            localization  = ResourceBundle.getBundle("strings", new Locale("cs", "CZ" ));
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Window.fxml"));
        loader.setResources(localization);
        Scene scene = new Scene(loader.load(), 1200, 800);
        stage.setTitle("Design Circuit");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/logo.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}