package com.test.cities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class FileDownloader {

    public String downloadFile(String link) {
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            return readInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readInputStream(InputStream inputStream) {
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}