package com.example.ncproject.Services.ServiceUtils;

import com.example.ncproject.DAO.Models.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class TokenVerifier {

    private static final String CLIENT_ID = "375617435757-oapk0ruihfafpn85e93jv96envuhm5uc.apps.googleusercontent.com";
    private static HttpTransport transport;
    private static JsonFactory jsonFactory;

    public static void Init(){
        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = GsonFactory.getDefaultInstance();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getNewUser(String idTokenString){
        try {
            GoogleIdToken idToken =tokenCheck(idTokenString);
            if(idToken!=null){
                Payload payload = idToken.getPayload();
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                return new User(userId,givenName,familyName,email);
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static GoogleIdToken tokenCheck(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        return idToken;
    }
}