package com.stackroute.user;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.User;
import com.stackroute.repository.UserRepository;
import com.stackroute.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp(){
        user1 = new User("100", "test1","first", "last","email", "profilePic","password1");
        user2 = new User("101", "test2","mihir", "sharma","andro", "profilePic","password2");
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        user1 = null;
        user2 = null;
    }

    @Test
    public void saveUsersTest() throws UserAlreadyExistsException {
        when(userRepository.save(any())).thenReturn(user1);
        assertEquals(user1, userService.saveUser(user1));
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void getExpertsTest(){
        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);
        when(userRepository.findAll()).thenReturn(userList);
        assertEquals(2, userService.getUsers().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUsersByUsernameTest() throws UserNotFoundException {
        Optional<User> exp = Optional.of(user1);
        when(userRepository.findById(any())).thenReturn(exp);
        assertEquals(exp, userService.getUserByUsername("test1"));
        verify(userRepository, times(2)).findById(any());
    }

    @Test
    public void getUsersByIdTest() throws UserNotFoundException{
        when(userRepository.findUsertById(any())).thenReturn(user2);
        assertEquals(user2, userService.getUserById("101"));
        verify(userRepository, times(2)).findUsertById(any());
    }

    @Test
    public void updateUsersTest() throws UserNotFoundException{
        User exp = new User("100", "test1","first", "last","email", "profilePic","software engineer");
        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(exp);
        assertEquals(exp, userService.updateUser(exp));
        verify(userRepository, times(1)).save(any());
    }
}
