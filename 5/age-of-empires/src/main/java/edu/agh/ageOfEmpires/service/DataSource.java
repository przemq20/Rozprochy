package edu.agh.ageOfEmpires.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataSource {
    static Logger logger = LoggerFactory.getLogger(DataSource.class);
    public static String URL = "https://age-of-empires-2-api.herokuapp.com/api/v1";
    public static Charset coding = StandardCharsets.UTF_8;

    public static String requestData(final String urlTarget) throws Exception {
        HttpURLConnection httpConnection = connect(urlTarget);
        if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpConnection.getInputStream(), coding)
                );
                StringBuilder stringBuilder = new StringBuilder();
                String inputLine;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(inputLine).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                httpConnection.disconnect();
            }
        } else {
            logger.error("Status " + httpConnection.getResponseCode());
            throw new Exception("Status " + httpConnection.getResponseCode());
        }
    }

    public static HttpURLConnection connect(final String urlTarget) throws IOException {
        HttpURLConnection http = (HttpURLConnection) new URL(urlTarget).openConnection();
        http.setRequestMethod("GET");
        http.connect();

        return http;
    }
}
