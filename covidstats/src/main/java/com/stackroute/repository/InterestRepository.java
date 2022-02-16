package com.stackroute.repository;

import com.stackroute.model.Interest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface InterestRepository extends MongoRepository<Interest, String> {

    @Query(value = "{userId : ?0}")
    Interest findInteresttById(String userId);
}
