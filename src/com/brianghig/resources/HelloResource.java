package com.brianghig.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloResource extends AbstractResource {
	
	public HelloResource() {
		System.out.println("\n\nCREATED HELLO RESOURCE!!\n\n");
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody String test() {
		return "Works!!";
	}
	
	@RequestMapping(value="test", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public @ResponseBody Map<String, Object> getData(
			@RequestParam(value="start", required=false) Integer start,
			@RequestParam(value="limit", required=false) Integer limit) throws Exception {
		
		System.out.println("Made it into getData() !!!");
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("success", Boolean.TRUE);
		data.put("count", 75);
		
		String[] vals = new String[]{ "One", "Two", "Three" };
		
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		for( int i=0; i < vals.length ; i++ ) {
			Map<String, Object> newObj = new HashMap<String, Object>();
			newObj.put("value", vals[i]);
			values.add(newObj);
		}
		
		data.put("rows", values);
		
		if( start != null ) {
			data.put("start", start);
		}
		
		if( limit != null ) {
			data.put("limit", limit);
		}
		
		return data;
		
	}
	
}
