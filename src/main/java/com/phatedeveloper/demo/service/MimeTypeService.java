package com.phatedeveloper.demo.service;

import com.phatedeveloper.demo.dto.MimeTypeDTO;
import com.phatedeveloper.demo.dto.MimeTypeValidationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MimeTypeService {

	List<MimeTypeDTO> getAll();

	List<MimeTypeValidationDTO> validateFiles(List<MultipartFile> files) throws IOException;

	Page<MimeTypeValidationDTO> validateFolder(String pathToFolder, Pageable pageable) throws IOException;

}
