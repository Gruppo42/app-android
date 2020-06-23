package com.gruppo42.app.utils;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

public class JSONResourceReader {

    public static String getJsonString(Resources resources, int id) {
        InputStream resourceReader = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e("JSONREADER", "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e("JSONREADER", "Unhandled exception while using JSONResourceReader", e);
            }
        }
        return writer.toString();
    }
}