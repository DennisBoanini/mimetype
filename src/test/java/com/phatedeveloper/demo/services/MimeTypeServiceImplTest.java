package com.phatedeveloper.demo.services;

import com.phatedeveloper.demo.service.impl.MimeTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
class MimeTypeServiceImplTest {

	@InjectMocks
	private MimeTypeServiceImpl mimeTypeService;

	@Test
	void dummyTest() {
		assertThat(true, is(true));
	}
}
