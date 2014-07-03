package net.rauix.mac2imgur;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.IOException;

public class AnonymousUpload implements ImgurUpload {

    private Image img;
    private HttpResponse<JsonNode> response;

    public AnonymousUpload(Image img) {
        this.img = img;
    }

    public void start() throws UnirestException, IOException {
        response = Unirest.post("https://api.imgur.com/3/upload")
                .header("Authorization", "Client-ID " + Main.CLIENT_ID)
                .field("image", img.getEncodedImage())
                .field("type", "base64")
                .field("title", img.getName())
                .field("description", "Uploaded by mac2imgur! (" + Main.SUPPORT_URL + ")")
                .asJson();
    }

    public boolean wasSuccessful() {
        return response.getCode() == 200;
    }

    public JSONObject getResponse() {
        return response.getBody().getObject();
    }

}