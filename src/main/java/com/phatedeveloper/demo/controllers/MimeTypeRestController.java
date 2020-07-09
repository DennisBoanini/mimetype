package com.phatedeveloper.demo.controllers;

import com.phatedeveloper.demo.MimeType;
import com.phatedeveloper.demo.service.MimeTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mime-types/v1")
public class MimeTypeRestController {

	private final MimeTypeService mimeTypeService;

	public MimeTypeRestController(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}

	@GetMapping
	public List<MimeType> getAll() {
		return this.mimeTypeService.getAll();
	}
}
