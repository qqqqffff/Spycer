import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.SessionHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalField;
import java.util.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class UserTest {
    @Test
    public void testLogin() throws IOException {
        SessionHandler.attemptLogin("1apollo.rowe@gmail.com", null);

    }

    @Test
    public void testCreateUser() throws IOException {
        User user = SessionHandler.createUser("2apollo.rowe@gmail.com", "apollo", "123");

        Gson gson = new Gson();

//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("userid", user.userId);
//        parameters.put("mfa_email", String.valueOf(user.mfaEmail));
//        parameters.put("mfa_app", String.valueOf(user.mfaApp));
//        parameters.put("verified", String.valueOf(user.verified));
//        parameters.put("display_name", user.displayName);
//        parameters.put("created_date", user.createdDate.toString());
//        parameters.put("hash_pw", user.hashPW);
//        parameters.put("hash_salt", user.hashSalt);
//        parameters.put("email_address", user.emailAddress);

//        Timestamp timestamp = new Timestamp(System.currentTimeMillis() );
//
//        String tzOffset = String.valueOf(ZoneId.systemDefault().getRules().getOffset(timestamp.toInstant()));
//        Timestamp adjustedTimestamp = new Timestamp(System.currentTimeMillis()).;
//        String formattedString = adjustedTimestamp.toInstant().toString();
//        System.out.println(ZoneId.systemDefault().getRules().getOffset(timestamp.toInstant()).getTotalSeconds());
//        System.out.println(formattedString.substring(0,formattedString.length()-1)+"000"+tzOffset);

        String urlString = "http://localhost:8080/users";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(gson.toJson(user, User.class));
        out.flush();
        out.close();

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        System.out.println(status + "," + content);

        System.out.println(gson.toJson(user, User.class));

    }
}
