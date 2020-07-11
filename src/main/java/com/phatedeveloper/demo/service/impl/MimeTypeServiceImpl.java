package com.phatedeveloper.demo.service.impl;

import com.phatedeveloper.demo.models.MimeType;
import com.phatedeveloper.demo.models.MimeTypeValidation;
import com.phatedeveloper.demo.repositories.MimeTypeRepository;
import com.phatedeveloper.demo.service.MimeTypeService;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
		List<String> validTypes = this.mimeTypeRepository.findAll().parallelStream().map(MimeType::getType).collect(Collectors.toList());

		for (MultipartFile file : files) {
			var validation = new MimeTypeValidation();
			validation.setFilename(file.getOriginalFilename());
			validation.setValid(validTypes.contains(file.getContentType()));

			validationResult.add(validation);
		}

		return validationResult;
	}

	@Override
	public ArrayList<MimeTypeValidation> validateFolder(String pathToFolder) throws IOException {
		if (Files.notExists(Path.of(pathToFolder))) {
			throw new IllegalArgumentException("The folder at path does not exist (" + pathToFolder + ")");
		}

		List<String> validTypes = this.mimeTypeRepository.findAll().parallelStream().map(MimeType::getType).collect(Collectors.toList());
		var validationResult = new ArrayList<MimeTypeValidation>();

		try (Stream<Path> walk = Files.walk(Paths.get(pathToFolder))) {
			walk.filter(Files::isRegularFile).forEach(x -> {
				Tika tika = new Tika();
				try {
					String fileMimeType = tika.detect(x);
					var validation = new MimeTypeValidation();
					validation.setFilename(x.getFileName().toString());
					validation.setValid(validTypes.contains(fileMimeType));
					validationResult.add(validation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		return validationResult;
	}

}
