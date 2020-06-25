package com.figure.anothertest.utils;

import android.os.Environment;

import java.io.File;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

    // storage location for camera files
    private static final String CAMERA_DIR = "/xaidworkz/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
