/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Fernando
 */
public class Song {

    private boolean running;
    private Integer index;
    private ArrayList<String> list;
    private FileInputStream FIS;
    private BufferedInputStream BIS;
    private Player player;
    private long pauseLocation;
    private long songTotalLength;
    private String fileLocation;

    public void next() {
        
        if (!hasNext()) {
            return;
        }
        
        stop();
        ++index;
        play();
        
    }

    public void back() {
        
        if (!hasBack()) {
            return;
        }
        
        stop();
        --index;
        play();
        
    }

    public boolean hasNext() {
        return index < list.size() - 1;
    }

    public boolean hasBack() {
        return index > list.size() - 1;
    }

    public Integer getCurrent() {
        return index;
    }

    public boolean isRunning() {
        return running;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
        this.index = 0;
    }

    public void play() {

        try {
            
            if (list == null)
                return;

            String path = list.get(index);

            if (fileLocation != null) {

                resume();

            } else {

                FIS = new FileInputStream(path);
                BIS = new BufferedInputStream(FIS);

                player = new Player(BIS);
                songTotalLength = FIS.available();
                fileLocation = path + "";

                running = true;

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            player.play();
                        } catch (JavaLayerException ex) {
                            // ..
                        }
                    }
                }.start();

            }

        } catch (JavaLayerException | IOException | NullPointerException | IndexOutOfBoundsException e) {
            // ..
        }
    }

    public void stop() {
        if (player != null) {
            player.close();

            fileLocation = null;
            pauseLocation = 0;
            songTotalLength = 0;
            running = false;
        }
    }

    public void pause() {
        if (player != null) {
            try {
                pauseLocation = FIS.available();
                player.close();
                running = false;
            } catch (IOException ex) {
                // ..
            }
        }
    }

    public void resume() {

        try {

            FIS = new FileInputStream(fileLocation);
            BIS = new BufferedInputStream(FIS);

            player = new Player(BIS);
            FIS.skip(songTotalLength - pauseLocation);

            running = true;

        } catch (JavaLayerException | IOException e) {
            // ..
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException ex) {
                    // ..
                }
            }
        }.start();

    }

}
