package com.phatedeveloper.demo.controllers;

import com.phatedeveloper.demo.dto.MimeTypeDTO;
import com.phatedeveloper.demo.dto.MimeTypeValidationDTO;
import com.phatedeveloper.demo.service.MimeTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/mime-types/v1")
public class MimeTypeRestController {

	private final MimeTypeService mimeTypeService;

	public MimeTypeRestController(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}

	@GetMapping
	public List<MimeTypeDTO> getAll() {
		return this.mimeTypeService.getAll();
	}

	@PostMapping("/validate-files")
	public List<MimeTypeValidationDTO> validateFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
		return this.mimeTypeService.validateFiles(files);
	}

	@GetMapping("/validate-folder")
	public Page<MimeTypeValidationDTO> validateFiles(@RequestParam("folderPath") String pathToFolder, Pageable pageable) throws IOException {
		return this.mimeTypeService.validateFolder(pathToFolder, pageable);
	}
}
