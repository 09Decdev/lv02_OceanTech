package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Category;
import com.globits.da.dto.CategoryDto;
import com.globits.da.dto.search.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoryService extends GenericService<Category, UUID> {
    public Page<CategoryDto> getPage(int pageSize, int pageIndex);

    public CategoryDto saveOrUpdate(UUID id, CategoryDto dto);

    public Boolean deleteKho(UUID id);

    public CategoryDto getCertificate(UUID id);

    Page<CategoryDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);

    public List<CategoryDto> getAllCategory();

    public Boolean deleteCheckById(UUID id);
}
