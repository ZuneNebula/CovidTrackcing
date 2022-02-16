package com.stackroute.controller;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.User;
import com.stackroute.service.UserService;
import com.stackroute.utility.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    CookieUtil cookieUtil;

    private ResponseEntity responseEntity;

    //for testing only
    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        List<User> all = this.userService.getUsers();
        this.responseEntity = new ResponseEntity(all, HttpStatus.OK);
        return this.responseEntity;
    }

    @GetMapping("/")
    public ResponseEntity getUser(@CookieValue("JWT-TOKEN") Cookie cookie) throws UserNotFoundException {
        try{
            User loggedUser = userService.getUserById(cookieUtil.getUserIdFromCookie(cookie));
            this.responseEntity = new ResponseEntity(loggedUser, HttpStatus.ACCEPTED);
        }
        catch (UserNotFoundException var1){
            throw new UserNotFoundException();
        }
        catch (Exception var2){
            this.responseEntity = new ResponseEntity("Error in fetching user details", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return this.responseEntity;
    }

    @PutMapping({"/"})
    public ResponseEntity<User> updateInnovator(@RequestBody User user) throws UserNotFoundException {
        try {
            User userData = userService.updateUser(user);
            this.responseEntity = new ResponseEntity(userData, HttpStatus.ACCEPTED);
        } catch (UserNotFoundException var1) {
            throw new UserNotFoundException();
        } catch (Exception var2) {
            this.responseEntity = new ResponseEntity("error while updating user data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return this.responseEntity;
    }


}
