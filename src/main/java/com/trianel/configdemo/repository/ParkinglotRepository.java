package com.trianel.configdemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trianel.configdemo.wrapper.WrappedMessage;

public interface ParkinglotRepository extends MongoRepository<WrappedMessage, String>{

}
