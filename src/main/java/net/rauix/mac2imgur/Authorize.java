package net.rauix.mac2imgur;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Authorize {

    private HttpResponse<JsonNode> response;

    public static void startBrowserForAuthorization() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("https://api.imgur.com/oauth2/authorize?client_id=" + Main.CLIENT_ID + "&response_type=pin&state=active"));
            }
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void makePinTokenRequest(String pin) throws UnirestException, IOException {
        response = Unirest.post("https://api.imgur.com/oauth2/token")
                .field("client_id", Main.CLIENT_ID)
                .field("client_secret", Main.CLIENT_SECRET)
                .field("grant_type", "pin")
                .field("pin", pin)
                .asJson();
    }

    public void saveRefreshToken() {
        String refreshToken = response.getBody().getObject().getString("refresh_token");
        Utils.getPrefs().put("REFRESH_TOKEN", refreshToken);

    }

    public void makeNewAccessTokenRequest() throws UnirestException, IOException {
        String refreshToken = Utils.getPrefs().get("REFRESH_TOKEN", "");

        response = Unirest.post("https://api.imgur.com/oauth2/token")
                .field("client_id", Main.CLIENT_ID)
                .field("client_secret", Main.CLIENT_SECRET)
                .field("grant_type", "refresh_token")
                .field("refresh_token", refreshToken)
                .asJson();

        if (wasSuccessful())
            Utils.setLastToken(response.getBody().getObject().getString("access_token"));

    }

    public String getAccessToken() {
        return response.getBody().getObject().getString("access_token");
    }

    public boolean wasSuccessful() {
        return response.getCode() == 200;
    }
}
