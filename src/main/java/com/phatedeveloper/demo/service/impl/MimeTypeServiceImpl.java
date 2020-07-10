package com.phatedeveloper.demo.service.impl;

import com.phatedeveloper.demo.models.MimeType;
import com.phatedeveloper.demo.models.MimeTypeValidation;
import com.phatedeveloper.demo.repositories.MimeTypeRepository;
import com.phatedeveloper.demo.service.MimeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class MimeTypeServiceImpl implements MimeTypeService {

	private MimeTypeRepository mimeTypeRepository;

	public MimeTypeServiceImpl(MimeTypeRepository mimeTypeRepository) {
		this.mimeTypeRepository = mimeTypeRepository;
	}

	@Override
	public List<MimeType> getAll() {
		Iterable<MimeType> mimeTypeIterable = this.mimeTypeRepository.findAll();

		return StreamSupport.stream(mimeTypeIterable.spliterator(), true).collect(Collectors.toList());
	}

	@Override
	public List<MimeTypeValidation> validateFiles(List<MultipartFile> files) {
		var validationResult = new ArrayList<MimeTypeValidation>();
		List<String> validTypes = StreamSupport.stream(this.mimeTypeRepository.findAll().spliterator(), true).map(MimeType::getType).collect(Collectors.toList());

		for (MultipartFile file : files) {
			var validation = new MimeTypeValidation();
			validation.setFilename(file.getOriginalFilename());
			validation.setValid(validTypes.contains(file.getContentType()));

			validationResult.add(validation);
		}

		return validationResult;
	}

}
