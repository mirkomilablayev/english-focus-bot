package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.configuration.SSLCertificateConfig;
import com.ielts.englishfocusbot.dto.TranslatorResponseDTO;
import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@AllArgsConstructor
public class TranslatorService {

    private final SSLCertificateConfig sslCertificateConfig;
    private static final String GOOGLE_TTS_URL = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=en&q=";

    public TranslatorResponseDTO translate(String from, String to, String word) {
        try {
            String url = "https://translate.google.com/translate_a/single";
            String requestData = String.format("client=at&sl=%s&tl=%s&dt=t&q=%s",
                    from, to, URLEncoder.encode(word, String.valueOf(StandardCharsets.UTF_8)));


            Connection.Response response = Jsoup.connect(url)
                    .sslSocketFactory(sslCertificateConfig.sslSocketFactory())
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .header("Accept", "*/*")
                    .requestBody(requestData)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();


            String jsonResponse = response.body();
            return new TranslatorResponseDTO(jsonResponse.split("\"")[1], from, to);
        } catch (Exception e) {
            return new TranslatorResponseDTO("", from, to);
        }
    }


    private File sendTextToSpeech(String text) {
        try {
            sslCertificateConfig.sslSocketFactory();
            String encodedText = URLEncoder.encode(text, "UTF-8");
            URL url = new URL(GOOGLE_TTS_URL + encodedText);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");


            InputStream inputStream = conn.getInputStream();

            // Save audio file locally
            File file = new File("output.mp3");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();

            return file;
        } catch (Exception e) {
            return null;
        }
    }

}
