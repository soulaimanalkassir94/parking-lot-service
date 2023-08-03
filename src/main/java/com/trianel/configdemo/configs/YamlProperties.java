package com.trianel.configdemo.configs;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YamlProperties {
	private Map<String, Map<String, String>> parkinglots;
}
