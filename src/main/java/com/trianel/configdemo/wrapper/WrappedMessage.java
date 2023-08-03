package com.trianel.configdemo.wrapper;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor

@Document(collection = "messages")
public class WrappedMessage {
	@Id
	String id;
	String service;
	String queue;
	String exchange;
	String routingkey;
	String date;
	boolean reproduced;
	boolean inspected;
	String message;
}