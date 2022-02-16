package com.stackroute.repository;

import com.stackroute.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User,String> {

    @Query(value = "{userId : ?0}")
    User findUsertById(String userId);
}
