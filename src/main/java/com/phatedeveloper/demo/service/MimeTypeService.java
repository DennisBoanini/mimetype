package com.phatedeveloper.demo.service;

import com.phatedeveloper.demo.models.MimeType;
import com.phatedeveloper.demo.models.MimeTypeValidation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface MimeTypeService {

	List<MimeType> getAll();

	List<MimeTypeValidation> validateFiles(List<MultipartFile> files);

	ArrayList<MimeTypeValidation> validateFolder(String pathToFolder) throws IOException;

}
