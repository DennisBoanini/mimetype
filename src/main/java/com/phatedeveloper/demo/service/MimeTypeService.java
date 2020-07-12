package com.phatedeveloper.demo.service;

import com.phatedeveloper.demo.models.MimeType;
import com.phatedeveloper.demo.models.MimeTypeValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MimeTypeService {

	List<MimeType> getAll();

	List<MimeTypeValidation> validateFiles(List<MultipartFile> files) throws IOException;

	Page<MimeTypeValidation> validateFolder(String pathToFolder, Pageable pageable) throws IOException;

}
