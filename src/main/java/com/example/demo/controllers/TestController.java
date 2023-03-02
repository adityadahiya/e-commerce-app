package com.example.demo.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/health")
public class TestController {

	@GetMapping
	public ResponseEntity<String> checkHealth() {
		return ResponseEntity.ok("Status: Working");
	}
	
}
