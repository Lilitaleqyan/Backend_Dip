package org.example.backend_dip.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String sendMessage(String message) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        String body = "{\n" +
                      "  \"contents\": [\n" +
                      "    {\n" +
                      "      \"role\": \"user\",\n" +
                      "      \"parts\": [\n" +
                      "        {\"text\": \"" + message + "\"}\n" +
                      "      ]\n" +
                      "    }\n" +
                      "  ]\n" +
                      "}";

        RequestBody body1 = RequestBody.create(mediaType, body);
        Request request = new Request.Builder().
                url(apiUrl + apiKey)
                .post(body1)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {

            assert response.body() != null;
            String json = response.body().string();

            System.out.println(json);
            JsonNode node = objectMapper.readTree(json);

            return node
                    .path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText("empty response ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
