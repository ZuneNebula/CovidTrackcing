package com.stackroute.google;

import com.stackroute.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleServiceImpl implements GoogleService {
    @Value("${spring.social.google.app-id}")
    private String googleId;
    @Value("${spring.social.google.app-secret}")
    private String googleSecret;

    public GoogleServiceImpl() {
    }

    private GoogleConnectionFactory createGoogleConnection() {
        return new GoogleConnectionFactory(this.googleId, this.googleSecret);
    }

    @Override
    public String googlelogin(String redirectUrl) {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(redirectUrl);
        parameters.setScope("profile email");
        return this.createGoogleConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getGoogleAccessToken(String code, String redirectUrl) {
        return this.createGoogleConnection().getOAuthOperations().exchangeForAccess(code, redirectUrl, null).getAccessToken();
    }

    @Override
    public User getGoogleUserProfile(String accessToken) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String profileData = (String)restTemplate.getForObject("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken, String.class, new Object[0]);
        System.out.println("profileData" + profileData.getClass());
        System.out.println("profileData" + profileData);
        JSONParser parser = new JSONParser();
        JSONObject profileObj = (JSONObject)parser.parse(profileData);
        User user = new User();
        System.out.println("profileobj" + profileObj.getClass());
        System.out.println("name" + profileObj.get("name"));

        user.setUsername(profileObj.get("email").toString());
        String name = profileObj.get("name").toString();
        String[] fullName = name.split(" ", 2);

        user.setFirstName(fullName[0]);
        user.setLastName(fullName[1]);
        user.setEmail(profileObj.get("email").toString());
        user.setAvatarUrl(profileObj.get("picture").toString());
        return user;
    }
}
