package com.example.doodhbhandar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {
    public static void sentIt(String name, String phone) {
        try {
            // Set the API endpoint and API key
            String apiEndpoint = "https://api.textlocal.in/send/?";
            String apiKey = "NmI3NzMxNDk2NjQ5Njk3NzQ1NTg3MTU5NTU1NTY0NTc=";

            // Set the message details
            String message = "Hello " + name + ", this is a test message.";
            String sender = "SENDERID";
            String numbers = phone; // Use the recipient's phone number passed in as the "phone" parameter

            // Encode the message and sender for the API request
            message = URLEncoder.encode(message, "UTF-8");
            sender = URLEncoder.encode(sender, "UTF-8");

            // Build the API request URL
            String requestUrl = apiEndpoint + "apiKey=" + apiKey + "&message=" + message + "&sender=" + sender + "&numbers=" + numbers;

            // Send the API request
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the API response
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

