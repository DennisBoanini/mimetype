package com.phatedeveloper.demo.service.impl;

import com.phatedeveloper.demo.MimeType;
import com.phatedeveloper.demo.repositories.MimeTypeRepository;
import com.phatedeveloper.demo.service.MimeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
