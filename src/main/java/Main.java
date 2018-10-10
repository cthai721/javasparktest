import com.google.gson.Gson;

import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    // byte to hex converter
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {

        get("/hello", (req, res) -> {
            String hello = "world";
            return new Gson().toJson(hello);
        });


        // Test: curl -d "{\"text\":\"Hello World\"}" -H "Content-Type: application/json" -X POST localhost:4567/hash
        post("/hash", (req, res) -> {

            // Get the request
            RequestModel reqBody = new Gson().fromJson(req.body(), RequestModel.class);

            // SHA-256 hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashByte = digest.digest(reqBody.text.getBytes(UTF_8));

            // Form the response
            ResponseModel resModel = new ResponseModel();
            resModel.hash = bytesToHex(hashByte);

            return new Gson().toJson(resModel);
        });
    }
}