package com.stackroute.service;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.Interest;
import com.stackroute.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InterestServiceImpl implements InterestService{
    @Autowired
    private InterestRepository interestRepository;

    @Override
    public Interest saveInterest(Interest interest) throws UserAlreadyExistsException {
        if(this.interestRepository.findInteresttById(interest.getUserId())!=null){
            throw new UserAlreadyExistsException();
        }
        else{
            UUID uuid = UUID.randomUUID();
            interest.setInterestId(String.valueOf(uuid));
            return this.interestRepository.save(interest);
        }
    }

    @Override
    public Interest updateInterest(Interest interest) throws UserNotFoundException {
        if(this.interestRepository.findInteresttById(interest.getUserId())==null){
            throw new UserNotFoundException();
        }
        else{
            return this.interestRepository.save(interest);
        }
    }

    @Override
    public Interest getInterest(String userId) throws UserNotFoundException{
        if(this.interestRepository.findInteresttById(userId)==null){
            throw new UserNotFoundException();
        }
        else{
            return this.interestRepository.findInteresttById(userId);
        }
    }
}
