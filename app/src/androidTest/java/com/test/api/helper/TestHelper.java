package com.test.api.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class TestHelper {
    public static ClassLoader classLoader;

    public static String getJsonFromFile(String jsonFileName) {
        InputStream resourceAsStream = classLoader.getResourceAsStream(jsonFileName);

        try {
            String json = convertStreamToString(resourceAsStream);
            resourceAsStream.close();
            return json;
        } catch (IOException e) {
            Timber.w(e);
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        is.close();
        return sb.toString();
    }

    public static ResponseBody createResponseBody(String contentJson) {
        ResponseBody responseBody = null;

        if (contentJson != null) {
            responseBody = ResponseBody.create(MediaType.parse("application/json"), contentJson);
        }
        return responseBody;
    }

}
