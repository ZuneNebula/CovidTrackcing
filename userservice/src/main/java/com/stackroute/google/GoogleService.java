package com.stackroute.google;

import com.stackroute.model.User;
import org.json.simple.parser.ParseException;

public interface GoogleService {
    String googlelogin(String redirectUrl);

    String getGoogleAccessToken(String code, String redirectUrl);

    User getGoogleUserProfile(String accessToken) throws ParseException;
}

