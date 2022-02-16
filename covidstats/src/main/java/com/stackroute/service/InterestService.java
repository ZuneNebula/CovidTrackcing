package com.stackroute.service;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.Interest;

public interface InterestService {
    Interest saveInterest(Interest interest) throws UserAlreadyExistsException;
    Interest updateInterest(Interest interest) throws UserNotFoundException;
    Interest getInterest(String userId) throws UserNotFoundException;
}
