package com.trianel.configdemo.wrapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.trianel.configdemo.configs.YamlProperties;
import com.trianel.configdemo.repository.ParkinglotRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
public class MessageWrapper {
    @Autowired
	private ParkinglotRepository repository;
    @Autowired
	private YamlProperties yamlProperties;
	
	@RabbitListener(queues = "#{queues}")
	public void onMessage(Message message) throws StreamReadException, DatabindException, IOException, ParseException{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		
		
		var queue = message.getMessageProperties().getConsumerQueue();
		var exchange = message.getMessageProperties().getReceivedExchange(); 
		var routingkey = message.getMessageProperties().getReceivedRoutingKey();
		var service = getServiceName(queue);
		
		
		String messageAsString = new String(message.getBody());
		
		WrappedMessage wrappedMessage = new WrappedMessage().builder()
				.service(service)
				.queue(queue)
				.exchange(exchange)
				.routingkey(routingkey)
				.date(date)
				.reproduced(false)
				.inspected(false)
				.message(messageAsString).build();
		repository.save(wrappedMessage);
		System.out.println("Message consumed!");
	}
    
	private String getServiceName(String queue) {
		String res = "";
		Map<String, Map<String, String>> map = yamlProperties.getTrianelparkinglots();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
    		if(entry.getValue().get("parkinglot").equals(queue)) {
    			res = entry.getValue().get("servicename");
    			break;
    		}
    		else {
    			res = "Unknown";
    		}
		}
		return res;
	}
}
