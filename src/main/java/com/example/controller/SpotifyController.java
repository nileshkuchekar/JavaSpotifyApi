package com.example.controller;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;

/**
 * @author nileshkuchekar
 *
 */
@Path("/spotify/auth")
@Component
public class SpotifyController {


    public static final String clientId = "b60cdf9a234f44e48e4a16d5a9edd679";
    public static final String clientSecret = "8d4e9527405e414ebf60af6127feb39e";
    public static final String redirectURI = "http://localhost:8080/java-spotify-rest/spotify/auth/callback";

    @RequestMapping(value="/init")
    public ModelAndView initAuth() {

        final Api api = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectURI(redirectURI)
                .build();

        final List<String> scopes = Arrays.asList("user-read-private", "user-read-email", "playlist-read-private", "playlist-modify-private");

        // pass a user-identifiable string using state variable
        final String state = "someExpectedStateString";

        String authorizeURL = api.createAuthorizeURL(scopes, state);

        return new ModelAndView("redirect:" + authorizeURL);
    }

    @RequestMapping(value="/callback")
    public String myCallback(@RequestParam("code") String code, @RequestParam("state") String state) {

        final Api api = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectURI(redirectURI)
                .build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = api.authorizationCodeGrant(code).build().get();

            System.out.println("State: " + state);
            System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
            System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
            System.out.println("Luckily, I can refresh it using this refresh token! " + authorizationCodeCredentials.getRefreshToken());

            api.setAccessToken(authorizationCodeCredentials.getAccessToken());
            api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return "/spotify/initAuth";
    }
	
}
