package org.example.backend_dip.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.backend_dip.entity.books.BookCopy;
import org.example.backend_dip.entity.books.BookDtoForChat;
import org.example.backend_dip.repo.BookDtoForChatRepo;
import org.example.backend_dip.repo.BookRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatService {
    private final BookDtoForChatRepo bookDtoForChatRepo;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Միացման սպասում
            .writeTimeout(60, TimeUnit.SECONDS)   // Տվյալների ուղարկման սպասում
            .readTimeout(60, TimeUnit.SECONDS)    // Պատասխանի ստացման սպասում (կարևորը սա է)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatService(BookDtoForChatRepo bookDtoForChatRepo) {
        this.bookDtoForChatRepo = bookDtoForChatRepo;
    }


    public String sendMessage(String message) {
        List<BookDtoForChat> books = bookDtoForChatRepo.findAll();
        StringBuilder booksListText = new StringBuilder();
        for (BookDtoForChat b : books) {
            booksListText.append(String.format("- %s, հեղինակ: %s (ID: %d)\n",
                    b.getTitle(), b.getAuthor(), b.getId(), b.getCount()));
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String systemPrompt = "Դու ԳրքաՊտույտ գրադարանի օգնականն ես: " +
                "Քո իրավասության մեջ է խոսել ՄԻԱՅՆ գրքերի, գրականության, հեղինակների և գրադարանի մասին: " +
                "Եթե քեզ տան այլ հարցեր (օրինակ՝ խոհարարության, սպորտի, քաղաքականության մասին), " +
                "քաղաքավարի պատասխանիր, որ դու կարող ես խոսել միայն գրքերի հետ կապված թեմաներից: " +
                "Պատասխանիր միայն տրամադրված ցուցակի հիման վրա։ " + booksListText +
                " Եթե գիրքը չկա ցուցակում, ասա, որ այն առկա չէ " +
                "Օգտատիրոջ հարցն է. ";

        String safeMessage = message.replace("\"", "\\\"");

        String fullPrompt = systemPrompt + safeMessage;
        String body = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"parts\": [\n" +
                "        {\"text\": \"" + fullPrompt + "\"}\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        RequestBody body1 = RequestBody.create(mediaType, body);
        Request request = new Request.Builder()
                .url(apiUrl + apiKey)
                .post(body1)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() == null) {
                return "Error: Response body is null";
            }

            String json = response.body().string();
            System.out.println("API Response: " + json);

            JsonNode node = objectMapper.readTree(json);

            if (node.has("error")) {
                return "API Error: " + node.path("error").path("message").asText();
            }


            JsonNode candidates = node.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode parts = firstCandidate.path("content").path("parts");

                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText("Empty response text");
                }
            }

            return "Error: Unexpected JSON structure from API";

        } catch (IOException e) {
            return "Network error: " + e.getMessage();
        }
    }
}
