package io.mingachevir.mingachevirrealestateserver.service.impl;

import io.mingachevir.mingachevirrealestateserver.model.dto.MainCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.ParameterDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.SubCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.ActiveCategoriesDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.CategoryUpdateRequestDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.HouseDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.MainCategoryDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.ParametersDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.SelectiveParameterValueDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.SubCategoryDTO;
import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import io.mingachevir.mingachevirrealestateserver.model.request.CategoryParameterRequest;
import io.mingachevir.mingachevirrealestateserver.repository.HouseJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.InputParameterJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.MainCategoryJpaRespository;
import io.mingachevir.mingachevirrealestateserver.repository.ParameterJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.SelectiveParameterValueJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.SubCategoryJpaRepository;
import io.mingachevir.mingachevirrealestateserver.service.CategoryService;
import io.mingachevir.mingachevirrealestateserver.util.CustomServiceException;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final MainCategoryJpaRespository mainCategoryJpaRepository;
    private final ParameterJpaRepository parameterJpaRepository;
    private final SelectiveParameterValueJpaRepository selectiveParameterValueJpaRepository;
    private final SubCategoryJpaRepository subCategoryJpaRepository;
    private final InputParameterJpaRepository inputParameterJpaRepository;
    private final HouseJpaRepository houseJpaRepository;

    @Override
    public GenericResponse<List<MainCategoryDto>> getMainCategoriesAndSubCategories() {
        try {
            List<MainCategoryDto> mainCategoryList = new ArrayList<>();
            mainCategoryJpaRepository.findAllEnabledMainCategoriesWithEnabledSubCategories()
                    .forEach(mainCategory -> mainCategoryList.add(new MainCategoryDto(mainCategory)));
            return GenericResponse.ok(mainCategoryList);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while fetching main categories", e.getMessage());
        }
    }

    @Override
    public GenericResponse<List<ParameterDto>> getParametersAndValuesForFooter() {
        try {
            List<ParameterDto> parameterDtoList = new ArrayList<>();
            Pageable pageable = PageRequest.of(0, 4);
            parameterJpaRepository.findAllByEnabledIsTrueAndIsDeletedFalseOrSubCategoryIdIsNull(pageable)
                    .forEach(parameter -> parameterDtoList.add(new ParameterDto(parameter)));
            return GenericResponse.ok(parameterDtoList);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while fetching parameters for footer", e.getMessage());
        }
    }

    @Override
    public GenericResponse<List<ParameterDto>> getParametersBySubCategoryId(Long subCategoryId) {
        try {
            List<ParameterDto> parameterDtoList = new ArrayList<>();
            parameterJpaRepository.findAllByEnabledIsTrueAndIsDeletedFalseAndSubCategoryIdIsNullOrSubCategoryId(subCategoryId)
                    .forEach(parameter -> parameterDtoList.add(new ParameterDto(parameter)));
            return GenericResponse.ok(parameterDtoList);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while fetching parameters by subcategory", e.getMessage());
        }
    }

    @Override
    public GenericResponse<ActiveCategoriesDTO> getActiveCategories() {
        try {
            ActiveCategoriesDTO dto = new ActiveCategoriesDTO(
                    mainCategoryJpaRepository.findByEnabledTrueAndIsDeletedFalse().stream()
                            .sorted(Comparator.comparing(MainCategory::getId)).map(mc -> {
                                MainCategoryDTO d = new MainCategoryDTO();
                                d.setId(mc.getId());
                                d.setName(mc.getName());
                                d.setCode(mc.getCode());
                                return d;
                            }).collect(Collectors.toList()),
                    subCategoryJpaRepository.findByEnabledTrueAndIsDeletedFalse().stream().map(sc -> {
                                SubCategoryDTO d = new SubCategoryDTO();
                                d.setId(sc.getId());
                                d.setName(sc.getName());
                                d.setCode(sc.getCode());
                                d.setMainCategoryId(sc.getMainCategory() != null ? sc.getMainCategory().getId() : null);
                                return d;
                            }).sorted(Comparator.comparing(SubCategoryDTO::getMainCategoryId, Comparator.nullsLast(Comparator.naturalOrder())))
                            .collect(Collectors.toList()),
                    parameterJpaRepository.findByEnabledTrueAndIsDeletedFalse().stream().map(p -> {
                                ParametersDTO d = new ParametersDTO();
                                d.setId(p.getId());
                                d.setName(p.getName());
                                d.setCode(p.getCode());
                                d.setType(p.getType());
                                d.setSubCategoryId(p.getSubCategory() != null ? p.getSubCategory().getId() : null);
                                return d;
                            }).sorted(Comparator.comparing(ParametersDTO::getSubCategoryId, Comparator.nullsLast(Comparator.naturalOrder())))
                            .collect(Collectors.toList()),
                    selectiveParameterValueJpaRepository.findByEnabledTrueAndIsDeletedFalse().stream()
                            .filter(spv -> spv.getParameter() != null)
                            .map(spv -> {
                                SelectiveParameterValueDTO d = new SelectiveParameterValueDTO();
                                d.setId(spv.getId());
                                d.setName(spv.getName());
                                d.setCode(spv.getCode());
                                d.setParameterId(spv.getParameter().getId());
                                return d;
                            }).sorted(Comparator.comparing(SelectiveParameterValueDTO::getParameterId, Comparator.nullsLast(Comparator.naturalOrder())))
                            .collect(Collectors.toList())
            );
            return GenericResponse.ok(dto);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while fetching active categories", e.getMessage());
        }
    }

    @Override
    public GenericResponse<Long> checkCategoryParametersHouses(List<Long> mainCategoryIds,
                                                               List<Long> subCategoryIds,
                                                               List<Long> parameterIds,
                                                               List<Long> selectiveParameterIds) {
        try {
            Long count = houseJpaRepository.countDistinctHousesByCategoriesAndParameters(
                    mainCategoryIds, subCategoryIds, parameterIds, selectiveParameterIds);
            return GenericResponse.ok(count);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while checking category parameters houses", e.toString());
        }
    }

    @Transactional
    @Override
    public GenericResponse<String> deleteOrUpdateCategoryAndParameters(CategoryUpdateRequestDTO request) {
        try {
            // Save Main Categories and map tempId to real id
            Map<Long, Long> mainCategoryTempIdToId = new HashMap<>();
            List<MainCategory> savedMainCategories;
            if (request.getMainCategoriesToUpdate() != null) {
                savedMainCategories = mainCategoryJpaRepository.saveAll(
                        request.getMainCategoriesToUpdate().stream()
                                .map(dto -> {
                                    MainCategory entity = dto.getId() != null
                                            ? mainCategoryJpaRepository.findById(dto.getId()).orElse(new MainCategory())
                                            : new MainCategory();
                                    entity.setName(dto.getName());
                                    entity.setCode(dto.getCode());
                                    entity.setEnabled(true);
                                    entity.setIsDeleted(false);
                                    return entity;
                                })
                                .collect(Collectors.toList())
                );
                for (int i = 0; i < request.getMainCategoriesToUpdate().size(); i++) {
                    MainCategoryDTO dto = request.getMainCategoriesToUpdate().get(i);
                    if (dto.getTempId() != null) {
                        mainCategoryTempIdToId.put(dto.getTempId(), savedMainCategories.get(i).getId());
                    }
                }
            } else {
                savedMainCategories = new ArrayList<>();
            }

            // Save Sub Categories and bind to Main Categories using tempId
            Map<Long, Long> subCategoryTempIdToId = new HashMap<>();
            List<SubCategory> savedSubCategories;
            if (request.getSubCategoriesToUpdate() != null) {
                savedSubCategories = subCategoryJpaRepository.saveAll(
                        request.getSubCategoriesToUpdate().stream()
                                .map(dto -> {
                                    SubCategory entity = dto.getId() != null
                                            ? subCategoryJpaRepository.findById(dto.getId()).orElse(new SubCategory())
                                            : new SubCategory();
                                    entity.setName(dto.getName());
                                    entity.setCode(dto.getCode());
                                    entity.setEnabled(true);
                                    entity.setIsDeleted(false);
                                    entity.setCreateDate(entity.getId() == null ? new Date() : entity.getCreateDate());
                                    entity.setUpdateDate(new Date());
                                    if (dto.getMainCategoryId() != null) {
                                        Long realMainCategoryId = mainCategoryTempIdToId.get(dto.getMainCategoryId());
                                        if (realMainCategoryId != null) {
                                            MainCategory mc = savedMainCategories.stream()
                                                    .filter(m -> m.getId().equals(realMainCategoryId))
                                                    .findFirst()
                                                    .orElseThrow(() -> new IllegalArgumentException("Saved MainCategory not found: " + realMainCategoryId));
                                            entity.setMainCategory(mc);
                                        } else {
                                            MainCategory mc = mainCategoryJpaRepository.findById(dto.getMainCategoryId())
                                                    .orElseThrow(() -> new IllegalArgumentException("MainCategory not found: " + dto.getMainCategoryId()));
                                            entity.setMainCategory(mc);
                                        }
                                    } else {
                                        entity.setMainCategory(null);
                                    }
                                    return entity;
                                })
                                .collect(Collectors.toList())
                );
                for (int i = 0; i < request.getSubCategoriesToUpdate().size(); i++) {
                    SubCategoryDTO dto = request.getSubCategoriesToUpdate().get(i);
                    if (dto.getTempId() != null) {
                        subCategoryTempIdToId.put(dto.getTempId(), savedSubCategories.get(i).getId());
                    }
                }
            } else {
                savedSubCategories = new ArrayList<>();
            }

            // Save Parameters and bind to Sub Categories using tempId
            Map<Long, Long> parameterTempIdToId = new HashMap<>();
            List<Parameters> savedParameters;
            if (request.getParametersToUpdate() != null) {
                savedParameters = parameterJpaRepository.saveAll(
                        request.getParametersToUpdate().stream()
                                .map(dto -> {
                                    Parameters entity = dto.getId() != null
                                            ? parameterJpaRepository.findById(dto.getId()).orElse(new Parameters())
                                            : new Parameters();
                                    entity.setName(dto.getName());
                                    entity.setCode(dto.getCode());
                                    entity.setType(dto.getType());
                                    entity.setEnabled(true);
                                    entity.setIsDeleted(false);
                                    if (dto.getSubCategoryId() != null) {
                                        Long realSubCategoryId = subCategoryTempIdToId.get(dto.getSubCategoryId());
                                        if (realSubCategoryId != null) {
                                            SubCategory sc = savedSubCategories.stream()
                                                    .filter(s -> s.getId().equals(realSubCategoryId))
                                                    .findFirst()
                                                    .orElseThrow(() -> new IllegalArgumentException("Saved SubCategory not found: " + realSubCategoryId));
                                            entity.setSubCategory(sc);
                                        } else {
                                            SubCategory sc = subCategoryJpaRepository.findById(dto.getSubCategoryId())
                                                    .orElseThrow(() -> new IllegalArgumentException("SubCategory not found: " + dto.getSubCategoryId()));
                                            entity.setSubCategory(sc);
                                        }
                                    } else {
                                        entity.setSubCategory(null);
                                    }
                                    return entity;
                                })
                                .collect(Collectors.toList())
                );
                for (int i = 0; i < request.getParametersToUpdate().size(); i++) {
                    ParametersDTO dto = request.getParametersToUpdate().get(i);
                    if (dto.getTempId() != null) {
                        parameterTempIdToId.put(dto.getTempId(), savedParameters.get(i).getId());
                    }
                }
            } else {
                savedParameters = new ArrayList<>();
            }

            // Save Selective Parameters and bind to Parameters using tempId
            if (request.getSelectiveParametersToUpdate() != null) {
                selectiveParameterValueJpaRepository.saveAll(
                        request.getSelectiveParametersToUpdate().stream()
                                .map(dto -> {
                                    SelectiveParameterValue entity = dto.getId() != null
                                            ? selectiveParameterValueJpaRepository.findById(dto.getId()).orElse(new SelectiveParameterValue())
                                            : new SelectiveParameterValue();
                                    entity.setName(dto.getName());
                                    entity.setCode(dto.getCode());
                                    entity.setEnabled(true);
                                    entity.setIsDeleted(false);
                                    if (dto.getParameterId() != null) {
                                        Long realParameterId = parameterTempIdToId.get(dto.getParameterId());
                                        if (realParameterId != null) {
                                            Parameters param = savedParameters.stream()
                                                    .filter(p -> p.getId().equals(realParameterId))
                                                    .findFirst()
                                                    .orElseThrow(() -> new IllegalArgumentException("Saved Parameter not found: " + realParameterId));
                                            entity.setParameter(param);
                                        } else {
                                            Parameters param = parameterJpaRepository.findById(dto.getParameterId())
                                                    .orElseThrow(() -> new IllegalArgumentException("Parameter not found: " + dto.getParameterId()));
                                            entity.setParameter(param);
                                        }
                                    } else {
                                        entity.setParameter(null);
                                    }
                                    return entity;
                                })
                                .collect(Collectors.toList())
                );
            }

            // Handle deletions
            if (request.getMainCategoryIdsToDelete() != null) {
                mainCategoryJpaRepository.updateStatusByIds(request.getMainCategoryIdsToDelete());
            }
            if (request.getSubCategoryIdsToDelete() != null) {
                subCategoryJpaRepository.updateStatusByIds(request.getSubCategoryIdsToDelete());
            }
            if (request.getParameterIdsToDelete() != null) {
                parameterJpaRepository.updateStatusByIds(request.getParameterIdsToDelete());
            }
            if (request.getSelectiveParameterIdsToDelete() != null) {
                selectiveParameterValueJpaRepository.updateStatusByIds(request.getSelectiveParameterIdsToDelete());
            }

            return GenericResponse.ok("Changes applied successfully");
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while updating categories and parameters", e.getMessage());
        }
    }

    @Override
    public GenericResponse<List<HouseDTO>> getDerelictHouses() {
        try {
            List<House> houses = houseJpaRepository.findDerelictHouses();
            List<HouseDTO> dtos = houses.stream().map(h -> {
                HouseDTO dto = new HouseDTO();
                dto.setId(h.getId());
                dto.setName(h.getName());
                dto.setLocation(h.getLocation());
                return dto;
            }).collect(Collectors.toList());
            return GenericResponse.ok(dtos);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while fetching derelict houses", e.getMessage());
        }
    }

    @Override
    public GenericResponse<List<SubCategoryDto>> getSubCategoriesWithoutMainCategory() {
        try {
            List<SubCategoryDto> subCategoryDtos = subCategoryJpaRepository
                    .findAllByMainCategoryIsNullAndEnabledTrueAndIsDeletedFalse()
                    .stream()
                    .map(SubCategoryDto::new)
                    .toList();

            return GenericResponse.ok(subCategoryDtos);
        } catch (Exception e) {
            throw new CustomServiceException("Error occurred while fetching subcategories without main category", e.getMessage());
        }
    }
}
