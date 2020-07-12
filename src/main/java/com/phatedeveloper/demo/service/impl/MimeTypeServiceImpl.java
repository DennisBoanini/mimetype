package com.phatedeveloper.demo.service.impl;

import com.phatedeveloper.demo.dto.MimeTypeDTO;
import com.phatedeveloper.demo.dto.MimeTypeValidationDTO;
import com.phatedeveloper.demo.mapper.MimeTypeValidationMapper;
import com.phatedeveloper.demo.models.MimeType;
import com.phatedeveloper.demo.models.MimeTypeValidation;
import com.phatedeveloper.demo.repositories.MimeTypeRepository;
import com.phatedeveloper.demo.service.MimeTypeService;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class MimeTypeServiceImpl implements MimeTypeService {

	private final MimeTypeRepository mimeTypeRepository;
	private final MimeTypeValidationMapper mimeTypeValidationMapper;

	public MimeTypeServiceImpl(MimeTypeRepository mimeTypeRepository, MimeTypeValidationMapper mimeTypeValidationMapper) {
		this.mimeTypeRepository = mimeTypeRepository;
		this.mimeTypeValidationMapper = mimeTypeValidationMapper;
	}

	@Override
	public List<MimeTypeDTO> getAll() {
		Iterable<MimeType> mimeTypeIterable = this.mimeTypeRepository.findAll();

		List<MimeType> mimeTypes = StreamSupport.stream(mimeTypeIterable.spliterator(), true).collect(Collectors.toList());

		return this.mimeTypeValidationMapper.toMimeTypeDTOs(mimeTypes);
	}

	@Override
	public List<MimeTypeValidationDTO> validateFiles(List<MultipartFile> files) throws IOException {
		var validationResult = new ArrayList<MimeTypeValidation>();
		List<String> validTypes = this.mimeTypeRepository.findAll().parallelStream().map(MimeType::getType).collect(Collectors.toList());

		for (MultipartFile file : files) {
			if (Objects.requireNonNull(file.getOriginalFilename()).endsWith("p7m")) {
				validationResult.add(this.validateP7M(file.getOriginalFilename(), validTypes));
			} else {
				var validation = new MimeTypeValidation();
				validation.setFilename(file.getOriginalFilename());
				validation.setValidated(validTypes.contains(file.getContentType()));

				validationResult.add(validation);
			}
		}

		return this.mimeTypeValidationMapper.toMimeTypeValidationDTOs(validationResult);
	}

	@Override
	public Page<MimeTypeValidationDTO> validateFolder(String pathToFolder, Pageable pageable) throws IOException {
		if (Files.notExists(Path.of(pathToFolder))) {
			throw new IllegalArgumentException("The folder at path does not exist (" + pathToFolder + ")");
		}

		List<String> validTypes = this.mimeTypeRepository.findAll().parallelStream().map(MimeType::getType).collect(Collectors.toList());
		var validationResult = new ArrayList<MimeTypeValidation>();
		int start;
		int end;
		int totalPages;

		try (Stream<Path> walk = Files.walk(Paths.get(pathToFolder))) {
			Predicate<Path> isNotHiddenFile = path -> { try { return !Files.isHidden(path); } catch (IOException e) { e.printStackTrace(); } return false; };
			Predicate<Path> isNotDirectory = path -> !Files.isDirectory(path);
			List<Path> pathList = walk.filter(isNotHiddenFile).filter(isNotDirectory).filter(Files::isRegularFile).collect(Collectors.toList());

			totalPages = pathList.size();

			start = (int) pageable.getOffset();
			end = Math.min(start + pageable.getPageSize(), totalPages);

			if (end >= start) {
				pathList.subList(start, end).forEach(file -> {
					Tika tika = new Tika();
					try {
						if (Objects.requireNonNull(file.getFileName().toString()).endsWith("p7m")) {
							validationResult.add(this.validateP7M(file.getFileName().toString(), validTypes));
						} else {
							String fileMimeType = tika.detect(file);
							var validation = new MimeTypeValidation();
							validation.setFilename(file.getFileName().toString());
							validation.setValidated(validTypes.contains(fileMimeType));
							validationResult.add(validation);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}

		Page<MimeTypeValidation> mimeTypeValidationPage = new PageImpl<>(validationResult, pageable, totalPages);

		return this.mimeTypeValidationMapper.toPageDTOs(mimeTypeValidationPage);
	}

	private MimeTypeValidation validateP7M(String filename, List<String> validTypes) throws IOException {
		Path path = Path.of(Objects.requireNonNull(filename).replace(".p7m", ""));
		String contentType = Files.probeContentType(path);

		var validation = new MimeTypeValidation();
		validation.setFilename(filename);
		validation.setValidated(validTypes.contains(contentType));

		return validation;
	}


}
