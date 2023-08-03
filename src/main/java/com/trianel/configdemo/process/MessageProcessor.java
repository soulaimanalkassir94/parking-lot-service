package com.trianel.configdemo.process;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.json.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trianel.configdemo.configs.YamlProperties;
import com.trianel.configdemo.repository.ParkinglotRepository;
import com.trianel.configdemo.wrapper.WrappedMessage;

import io.swagger.v3.core.util.Json;

@Controller
public class MessageProcessor {
	
	private final MongoTemplate mongoTemplate;
	
	private final RabbitTemplate rabbitTemplate;
	
	@Autowired
	private final ParkinglotRepository repository;

    @Autowired
	private YamlProperties yamlProperties;
	
	public MessageProcessor(MongoTemplate mongoTemplate, ParkinglotRepository repository, RabbitTemplate rabbitTemplate) {
		this.mongoTemplate = mongoTemplate;
		this.repository = repository;
		this.rabbitTemplate = rabbitTemplate;
	}
    
    private List<String> listServiceNames(){
    	List<String> services = new ArrayList<String>();
    	
		Map<String, Map<String, String>> map = yamlProperties.getTrianelparkinglots();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
    		services.add(entry.getValue().get("servicename"));
		}
		return services;
    }
    
    private List<String> listParkinglots(){
    	List<String> parkinglots = new ArrayList<String>();
    	
		Map<String, Map<String, String>> map = yamlProperties.getTrianelparkinglots();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
			parkinglots.add(entry.getValue().get("parkinglot"));
		}
		return parkinglots;
    }
	
    @RequestMapping(value = "/filterMessages", method = RequestMethod.GET) 
	public String processMessage(Model model) {
		model.addAttribute("servicename", listServiceNames());
		model.addAttribute("parkinglot", listParkinglots());
		return "filterMessages";
	}
	
    @RequestMapping(value = "/inspectMessages", method = RequestMethod.POST) 
	public ModelAndView processMessagePost(HttpServletRequest request, HttpServletResponse response) throws ParseException, JsonProcessingException {
    	boolean reproduced = request.getParameter("reproduced")!=null?true:false;
    	boolean inspected = request.getParameter("inspected")!=null?true:false;
    	String queue = request.getParameter("parkinglot");
    	String service = request.getParameter("servicename");
    	String routingkey = request.getParameter("routingkey");
    	String before = request.getParameter("before");
    	String after = request.getParameter("after");
    	
    	List<WrappedMessage> filteredMessages = mongoTemplate.findAll(WrappedMessage.class);
    	
    	filteredMessages = filterMessagesByService(filteredMessages, service);
    	filteredMessages = filterMessagesByQueue(filteredMessages, queue);
    	filteredMessages = filterMessagesByReproduced(filteredMessages, reproduced);
    	filteredMessages = filterMessagesByInspected(filteredMessages, inspected);
    	filteredMessages = filterMessagesByBefore(filteredMessages, before);
    	filteredMessages = filterMessagesByAfter(filteredMessages, after);
    	
    	ModelAndView model = new ModelAndView("inspectMessages");
    	
    	model.addObject("filteredMessages", filteredMessages);
		return model;
	}
    
    @RequestMapping(value = "/reproduceMessages", method = RequestMethod.POST)
    public ModelAndView inspectMessage(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView model = new ModelAndView("reproduceMessages");
    	Query query = new Query();
		query.addCriteria(Criteria
				.where("_id").is(id));
		WrappedMessage message = mongoTemplate.findOne(query, WrappedMessage.class);
		Update update = new Update().set("inspected", true);
		mongoTemplate.updateFirst(query, update, WrappedMessage.class);
		model.addObject("message", message);
    	return model;
    }
    
    @RequestMapping(value = "/success", method = RequestMethod.POST)
    public ModelAndView reproduceMessage(@RequestParam("id") String id, @RequestParam("hidden-message") String message, HttpServletRequest request, HttpServletResponse response){
    	ModelAndView model = new ModelAndView("success");
    	Query query = new Query();
		query.addCriteria(Criteria
				.where("_id").is(id));
		Update update = new Update().set("message", message).set("reproduced", true);
		mongoTemplate.updateFirst(query, update, WrappedMessage.class);
		WrappedMessage filteredMessage = mongoTemplate.findOne(query, WrappedMessage.class);
		String exchange = filteredMessage.getExchange();
		String routingkey = filteredMessage.getRoutingkey();
		rabbitTemplate.convertAndSend(exchange, routingkey, message);
		return model;
    }
    
    public List<WrappedMessage> filterMessagesByService(List<WrappedMessage> messages, String service){
    	List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
    	if(service.equals("all-services")) {
    		result_messages = messages;
    	}
    	else {
    		for(WrappedMessage message : messages) {
    			if(message.getService().equals(service)) {
    				result_messages.add(message);
    			}
    		}
    	}
    	return result_messages;
    }
    
    public List<WrappedMessage> filterMessagesByQueue(List<WrappedMessage> messages, String queue){
    	List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
    	if(queue.equals("all-parking-lots")) {
    		result_messages = messages;
    	}
    	else {
    		for(WrappedMessage message : messages) {
    			if(message.getQueue().equals(queue)) {
    				result_messages.add(message);
    			}
    		}
    	}
    	return result_messages;
    }
    
    public List<WrappedMessage> filterMessagesByReproduced(List<WrappedMessage> messages, boolean reproduced){
    	List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
    		for(WrappedMessage message : messages) {
    			if(message.isReproduced() == reproduced) {
    				result_messages.add(message);
    			}
    		}
    	return result_messages;
    }
    
    public List<WrappedMessage> filterMessagesByInspected(List<WrappedMessage> messages, boolean inspected){
    	List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
    		for(WrappedMessage message : messages) {
    			if(message.isInspected() == inspected) {
    				result_messages.add(message);
    			}
    		}
    	return result_messages;
    }
    
    public List<WrappedMessage> filterMessagesByBefore(List<WrappedMessage> messages, String before) throws ParseException{
    	List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	
    	if(before.equals("")) {
    		result_messages = messages;
    	}
    	else {
    		Date before_date = format.parse(before);
    		for(WrappedMessage message : messages) {
    			Date message_date = format.parse(message.getDate());
    			if(before_date.compareTo(message_date) > 0) {
    				result_messages.add(message);
    			}
    		}
    	}
    	return result_messages;
    }
    
    public List<WrappedMessage> filterMessagesByAfter(List<WrappedMessage> messages, String after) throws ParseException{
    	List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	if(after.equals("")) {
    		result_messages = messages;
    	}
    	else {
    		Date after_date = format.parse(after);
    		for(WrappedMessage message : messages) {
    			Date message_date = format.parse(message.getDate());
    			if(after_date.compareTo(message_date) < 0) {
    				result_messages.add(message);
    			}
    		}
    	}
    	return result_messages;
    }
    
    public List<WrappedMessage> filterMessagesBetweenTwoDates(boolean inspected, boolean reproduced, String service, String queue, String before_date, String after_date) throws ParseException{
    	Query query = new Query();
		query.addCriteria(Criteria
				.where("service").is(service)
				.and("queue").is(queue)
				.and("inspected").is(inspected)
				.and("reproduced").is(reproduced)
				);
		List<WrappedMessage> messages = mongoTemplate.find(query, WrappedMessage.class);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date before = format.parse(before_date);
		Date after = format.parse(after_date);
		List<WrappedMessage> result_messages = new ArrayList<WrappedMessage>();
		for(WrappedMessage message : messages) {
			Date message_date = format.parse(message.getDate());
			if (after.compareTo(message_date) < 0 && before.compareTo(message_date) > 0) {
			    result_messages.add(message);
			}
		}
		return result_messages;
    }
	public void reproduceMessageToOriginalExchange(WrappedMessage wrappedMessage){
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.convertAndSend(wrappedMessage.getExchange(), "", wrappedMessage.getMessage());
	}
	
	@GetMapping("/message/findByDate/between")
	public List<WrappedMessage> findMessageBetweenTwoDates(@RequestParam String from, @RequestParam String to) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date from_date = format.parse(from); 
		Date to_date = format.parse(to); 
		List<WrappedMessage> result = new ArrayList<WrappedMessage>();
		List<WrappedMessage> all_messages = repository.findAll();
		
		for(WrappedMessage message : all_messages) {
			Date messageDate = format.parse(message.getDate());
			if (from_date.compareTo(messageDate) < 0 && to_date.compareTo(messageDate) > 0) {
			    result.add(message);
			}
		}
		return result;
	}
	
	@GetMapping("/message/findByDate/before")
	public List<WrappedMessage> findMessageBeforeGivenDate(@RequestParam String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date given_date = format.parse(date); 
		List<WrappedMessage> result = new ArrayList<WrappedMessage>();
		List<WrappedMessage> all_messages = repository.findAll();
		
		for(WrappedMessage message : all_messages) {
			Date messageDate = format.parse(message.getDate());
			if (given_date.compareTo(messageDate) > 0) {
			    result.add(message);
			}
		}
		return result;
	}
	
	@GetMapping("/message/findByDate/after")
	public List<WrappedMessage> findMessageAfterGivenDate(@RequestParam String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date given_date = format.parse(date); 
		List<WrappedMessage> result = new ArrayList<WrappedMessage>();
		List<WrappedMessage> all_messages = repository.findAll();
		
		for(WrappedMessage message : all_messages) {
			Date messageDate = format.parse(message.getDate());
			if (given_date.compareTo(messageDate) < 0) {
			    result.add(message);
			}
		}
		return result;
	}
	
	@GetMapping("/message/findByDate/at")
	public List<WrappedMessage> findMessageAtGivenDate(@RequestParam String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date given_date = format.parse(date); 
		List<WrappedMessage> result = new ArrayList<WrappedMessage>();
		List<WrappedMessage> all_messages = repository.findAll();
		
		for(WrappedMessage message : all_messages) {
			Date messageDate = format.parse(message.getDate());
			if (given_date.compareTo(messageDate) == 0) {
			    result.add(message);
			}
		}
		return result;
	}
	
	@GetMapping("/message/findByService/{service}")
	public List<WrappedMessage> findMessageByService(@PathVariable String service) {
		Query query = new Query();
		query.addCriteria(Criteria.where("service").is(service));
		List<WrappedMessage> messages = mongoTemplate.find(query, WrappedMessage.class);
		return messages;
	}
	
	@GetMapping("/message/findByQueue/{queue}")
	public List<WrappedMessage> findMessageByQueue(@PathVariable String queue) {
		Query query = new Query();
		query.addCriteria(Criteria.where("queue").is(queue));
		List<WrappedMessage> messages = mongoTemplate.find(query, WrappedMessage.class);
		return messages;
	}
	
	@GetMapping("/message/findByReproduced/{reproduced}")
	public List<WrappedMessage> findMessageByReproduced(@PathVariable boolean reproduced) {
		Query query = new Query();
		query.addCriteria(Criteria.where("reproduced").is(reproduced));
		List<WrappedMessage> messages = mongoTemplate.find(query, WrappedMessage.class);
		return messages;
	}
	
	@GetMapping("/message/findByInspected/{inspected}")
	public List<WrappedMessage> findMessageByInspected(@PathVariable boolean inspected) {
		Query query = new Query();
		query.addCriteria(Criteria.where("inspected").is(inspected));
		List<WrappedMessage> messages = mongoTemplate.find(query, WrappedMessage.class);
		return messages;
	}
}
