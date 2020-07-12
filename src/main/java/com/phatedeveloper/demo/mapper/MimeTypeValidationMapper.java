package com.phatedeveloper.demo.mapper;

import com.phatedeveloper.demo.dto.MimeTypeDTO;
import com.phatedeveloper.demo.dto.MimeTypeValidationDTO;
import com.phatedeveloper.demo.models.MimeType;
import com.phatedeveloper.demo.models.MimeTypeValidation;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface MimeTypeValidationMapper {

	MimeTypeValidationDTO toDTO(MimeTypeValidation mimeTypeValidation);

	List<MimeTypeValidationDTO> toMimeTypeValidationDTOs(List<MimeTypeValidation> mimeTypeValidations);

	List<MimeTypeDTO> toMimeTypeDTOs(List<MimeType> mimeTypes);

	default Page<MimeTypeValidationDTO> toPageDTOs(Page<MimeTypeValidation> mimeTypeValidations) {
		return mimeTypeValidations.map(this::toDTO);
	}
}
