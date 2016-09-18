package com.mycompany.controllers;

import com.mongodb.MongoClient;
import com.mycompany.models.Player;
import com.mycompany.player.Song;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class PlayerController implements Initializable {
    
    final Morphia morphia = new Morphia();
    private Datastore datastore;
    private Song song = new Song();

    @FXML
    private ImageView imgPlay;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnBack;

    @FXML
    private ListView display;

    @FXML
    private void play(ActionEvent event) {
        
        if (song.getList().isEmpty())
            return;

        if (song.isRunning() == true) {

            song.pause();

            File file = new File("src/main/resources/images/play.png");
            imgPlay.setImage(new Image(file.toURI().toString()));

        } else {

            File file = new File("src/main/resources/images/pause.png");
            imgPlay.setImage(new Image(file.toURI().toString()));

            song.play();
        }
    }

    @FXML
    private void stop(ActionEvent event) {
        
        song.stop();

        File file = new File("src/main/resources/images/play.png");
        imgPlay.setImage(new Image(file.toURI().toString()));
    }

    @FXML
    private void next(ActionEvent event) {

        song.next();

    }

    @FXML
    private void back(ActionEvent event) {

        song.back();

    }

    @FXML
    private void importFile(ActionEvent event) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3", "mp3");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(null);
        File[] files = fileChooser.getSelectedFiles();

        ArrayList list = new ArrayList<>();

        for (File file : files) {
            list.add(file.getPath());
            Player p = new Player(file.getPath());
            datastore.save(p);
        }

        displayItens(list);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // diz ao morphia onde encontrar suas clasess
        morphia.mapPackage("com.mycompany.models");
        // cria um Datastore que conecta com a porta padrão no localhost
        datastore = morphia.createDatastore(new MongoClient(), "player");
        datastore.ensureIndexes();

        List<Player> list = datastore.createQuery(Player.class).asList();
        ArrayList paths = new ArrayList<>();

        for (Player row : list) {
            paths.add(row.getPath());
        }
        
        displayItens(paths);

    }
    
    private void displayItens(ArrayList list) {
        song.setList(list);
        ObservableList<String> data = FXCollections.observableArrayList(list);
        display.setItems(data);
    }

}
