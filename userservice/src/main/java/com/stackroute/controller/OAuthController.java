package com.stackroute.controller;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.google.GoogleService;
import com.stackroute.model.LoggedUser;
import com.stackroute.model.User;
import com.stackroute.service.UserService;
import com.stackroute.utility.CookieUtil;
import com.stackroute.utility.JwtUtil;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping({"api/v1/authorize"})
@RestController
public class OAuthController {

    @Value("${home_page_url}")
    private String homePageUrl;

    @Autowired
    private GoogleService googleService;
    @Autowired
    private UserService userService;

    @Value("${Domain}")
    private String domain;
    @Value("${redirect_url}")
    private String redirectUrl;

    @Value("${logout_redirect_url}")
    private String logOutRedirectUrl;

    private ResponseEntity responseEntity;

    public OAuthController() {
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpServletResponse res) throws UserAlreadyExistsException {

        try{
            if(user.getPassword().length()>0){
                User user1 = this.userService.saveUser(user);
                String jwtToken = JwtUtil.addUserToken(res, Optional.of(user1));
                CookieUtil.create(res, "JWT-TOKEN", jwtToken, false, -1, this.domain);
                responseEntity = new ResponseEntity(user1, HttpStatus.CREATED);
            }
            else{
                responseEntity = new ResponseEntity("Error in registering user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (UserAlreadyExistsException e){
            throw new UserAlreadyExistsException();
        }
        catch (Exception e2){
            responseEntity = new ResponseEntity("Error in registering user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    //change it to accept username and password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoggedUser loggedUser, HttpServletResponse res) throws UserNotFoundException {
        try{
            User user1 = this.userService.getUserByUsername(loggedUser.getUsername()).get();
            if(loggedUser.getPassword().equals(user1.getPassword())){
                String jwtToken = JwtUtil.addUserToken(res, Optional.of(user1));
                CookieUtil.create(res, "JWT-TOKEN", jwtToken, false, -1, this.domain);
                responseEntity = new ResponseEntity(user1, HttpStatus.ACCEPTED);
            }
            else{
                responseEntity = new ResponseEntity("Error in logging in user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }
        catch (Exception e2){
            responseEntity = new ResponseEntity("Error in logging user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping({"/googlelogin"})
    public RedirectView googleInnovatorLogin() {
        RedirectView redirectview = new RedirectView();
        String url = this.googleService.googlelogin(this.redirectUrl);
        redirectview.setUrl(url);
        return redirectview;
    }

    @GetMapping({"/logout"})
    public RedirectView googleLogOut(HttpServletResponse res){
        RedirectView redirectView = new RedirectView();
        CookieUtil.clearCookie(res, "JWT-TOKEN", this.domain);
        redirectView.setUrl(logOutRedirectUrl); //read from config server later
        return redirectView;
    }

    @GetMapping({"/complete"})
    public RedirectView googleInnovator(@RequestParam("code") String code, HttpServletResponse res) throws ParseException {
        String accessToken = this.googleService.getGoogleAccessToken(code, this.redirectUrl);
        System.out.println("accessToken: " + accessToken);
        User user = this.googleService.getGoogleUserProfile(accessToken);
        //save user in repo
        try {
            System.out.println("USER:: " + user.toString());
            this.userService.saveUser(user);
        } catch (Exception var8) {
            PrintStream var10000 = System.out;
            LocalDateTime var10001 = LocalDateTime.now();
            var10000.println("In google method " + var10001 + " " + var8.getMessage());
        }
        //get saved user from repo
        Optional<User> repoUser = null;
        try{
            repoUser = userService.getUserByUsername(user.getUsername());
        }
        catch (UserNotFoundException ex){
            ex.getMessage();
        }

        String jwtToken = JwtUtil.addUserToken(res, repoUser);
        CookieUtil.create(res, "JWT-TOKEN", jwtToken, false, -1, this.domain);
        RedirectView redirectview = new RedirectView();
        redirectview.setUrl(this.homePageUrl);
        return redirectview;
    }
}
