package com.neo.parkguidance.main.api.dataloader;

import com.neo.parkguidance.framework.impl.dataloader.DataloaderTool;

public class Dataloader {

    public static void main(String[] args) {
        DataloaderTool dataloaderTool = new DataloaderTool("local-postgres","DataloaderConfiguration.json");
        dataloaderTool.start();
    }
}
