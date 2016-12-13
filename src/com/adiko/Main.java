package com.adiko;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Stage window;
    TextField tfOutput;

    String input;
    int md5index = 0;

    String password = "";

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        tfOutput = new TextField();
        Button btnGo = new Button("GO");
        btnGo.setOnAction(e -> processInput());

        layout.getChildren().addAll(btnGo, tfOutput);

        StackPane root = new StackPane();
        root.getChildren().add(layout);
        window.setScene(new Scene(root, 300, 300));
        window.show();
    }

    private void processInput() {
        parseInput();
        generatePassword();
        tfOutput.setText(password);
    }

    private void generatePassword() {
        while(password.length() < 8) {
            StringBuilder sb = new StringBuilder(input);
            sb.append(md5index);
            String md5string = md5(sb.toString());
           // System.out.println(md5index + ": " + md5string);
            processMd5ToPassword(md5string);
            md5index++;
        }
    }

    private void processMd5ToPassword(String md5string) {
        if(md5string.startsWith("00000")){
            StringBuilder sb = new StringBuilder(password);
            sb.append(md5string.charAt(5));
            password = sb.toString();
        }
    }

    private void parseInput() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")))) {
            line = br.readLine();
            while (line != null) {
                input = line;
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("FILE NOT FOUND: " + e.getMessage());
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("IOEXCEPTION: " + e.getMessage());
        }
    }

    public String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
