package com.trianel.configdemo.configs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Bean config.
 * Here we're creating beans of general usage
 */
@Configuration
public class BeanConfig {
	
	@Autowired
	private YamlProperties yamlProperties;
	
	@Bean(name = "queues")
	public String[] getQueues() {
    	Map<String, Map<String, String>> map = yamlProperties.getTrianelparkinglots();
    	List<String> queues = new ArrayList<String>();
    	int i = map.size();
    	for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
    		queues.add(entry.getValue().get("parkinglot"));
    	}
    	String[] queues_array = new String[i];
    	return queues.toArray(queues_array);
	}
}
