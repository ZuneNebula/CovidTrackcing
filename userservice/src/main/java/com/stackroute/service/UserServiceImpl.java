package com.stackroute.service;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.User;
import com.stackroute.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        if (this.userRepository.findById(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            UUID id = UUID.randomUUID();
            user.setUserId(String.valueOf(id));
            return (User)this.userRepository.save(user);
        }
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws UserNotFoundException {
        if(!this.userRepository.findById(username).isPresent()){
            throw new UserNotFoundException();
        }
        else{
            return userRepository.findById(username);
        }
    }

    @Override
    public User getUserById(String id) throws UserNotFoundException {
        if(this.userRepository.findUsertById(id)==null){
            throw new UserNotFoundException();
        }
        else{
            return userRepository.findUsertById(id);
        }
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        if(!this.userRepository.findById(user.getUsername()).isPresent()){
            throw new UserNotFoundException();
        }
        else{
            return userRepository.save(user);
        }
    }
}
