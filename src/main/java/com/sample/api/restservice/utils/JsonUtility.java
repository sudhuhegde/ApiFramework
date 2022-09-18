package com.sample.api.restservice.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sample.api.restservice.file.FileReader;
import groovy.json.StringEscapeUtils;


public  class JsonUtility {
	public static JsonNode getJsonNode(String json) throws  IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(json);
		return node;
	}
	public static String getStringFromPropertyFileObject(Object object)  {
		if(object==null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(object, JsonNode.class);

		return StringEscapeUtils.unescapeJava(getStringFromObject(node));
	}


	public static String readJsonAsString(String filePath) throws  IOException {
		ObjectMapper mapper = new ObjectMapper();
		InputStream io = FileReader.getInstance().readInputStreamFromClassPath(filePath);
		JsonNode node = mapper.readTree(io);
		return mapper.writeValueAsString(node);
	}

	public static JsonNode getJsonNodeFromFile(String filePath) throws  IOException {
		ObjectMapper mapper = new ObjectMapper();
		InputStream io = FileReader.getInstance().readInputStreamFromClassPath(filePath);
		JsonNode node = mapper.readTree(io);
		return node;
	}

	public static String getStringFromJson(JsonNode node) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {
			//TODO : log error
		}
		return jsonString;
	}

	public static JsonNode getJsonNodeFromObject(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(object, JsonNode.class);
		return node;
	}


	public static String getStringFromObject(Object object)  {
		if(object==null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(object, JsonNode.class);
		return getStringFromJson(node);
	}

	public static <T extends Object> Object getObjectFromJsonNode(JsonNode node, Class<?> classType) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(node, classType);

	}
	
	public static <T extends Object> Object getObjectFromString(String value, Class<?> classType) throws IllegalArgumentException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(getJsonNode(value), classType);

	}

	public static JsonNode getJsonNodeFromList(List<String> list) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node =null;
		if(list!=null && list.size()>0) {
			node= mapper.valueToTree(list);
		}
		return node;
	}

	public static JsonNode removeFields(JsonNode node,List<String> fields) {
		return ((ObjectNode)node).remove(fields);

	}
	public static JsonNode removeNode(JsonNode node,String attributeName,String attributeValue) {
		Iterator<JsonNode> nodes = node.elements();
		while(nodes.hasNext()) {
			JsonNode nextNode=nodes.next();
			if(nextNode.get(attributeName).asText().equals(attributeValue)){
				nodes.remove();
				break;
			}
		}
		return node;
	}
	public static JsonNode removeFieldsFromJsonNode(JsonNode node,String removeAttributeName,String matchAttributeName,String attributeValue) {
		Iterator<JsonNode> nodes = node.elements();
		while(nodes.hasNext()) {
			JsonNode nextNode=nodes.next();
			if(nextNode.get(matchAttributeName).asText().equals(attributeValue)){
				removeFields(nextNode, Collections.singletonList(removeAttributeName));
				break;
			}
		}
		return node;
	}
	public static JsonNode addKeyToJsonNode(JsonNode node,String matchAttributeName,String matchAttributeValue,String attributeName,String attributeValue) {
		Iterator<JsonNode> nodes = node.elements();
		while(nodes.hasNext()) {
			JsonNode nextNode=nodes.next();
			if(nextNode.get(matchAttributeName).asText().equals(matchAttributeValue)){
				((ObjectNode)nextNode).put(attributeName,attributeValue);
				break;
			}
		}
		return node;
	}

	public static JsonNode setNullValues(JsonNode node, List<String> fields) {
		JsonNode nullNode = null;
		fields.stream().forEach(field->((ObjectNode)node).set(field, nullNode));
		return node;
	}

	public static <T extends Object> Object getObjectFromFile(String filePath, Class<?> classType) throws  IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(getJsonNodeFromFile(filePath), classType);
	}


	public static JsonNode createJson(String filePath,JsonNode node) throws  IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(new File(filePath), node);
		return node;
	}

	public static JsonNode setEmptyString(JsonNode node, List<String> fields) {
		fields.stream().forEach(field->((ObjectNode)node).put(field, ""));
		return node;
	}

	public static JsonNode setEmptyArray(JsonNode node, List<String> fields) {
		ArrayNode arrayNode = new ObjectMapper().createArrayNode();
		fields.stream().forEach(field->((ObjectNode)node).set(field, arrayNode));
		return node;
	}

	public static JsonNode setEmptyObject(JsonNode node, List<String> fields) {
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		fields.stream().forEach(field->((ObjectNode)node).set(field, objectNode));
		return node;
	}
	public static JsonNode setObjectValue(JsonNode node, String attributeName,String attributeValue) {
		((ObjectNode)node).put(attributeName, attributeValue);
		return node;
	}

	public static JsonNode setNullString(JsonNode node, List<String> fields) {
		fields.stream().forEach(field->((ObjectNode)node).put(field, "null"));
		return node;
	}


}
