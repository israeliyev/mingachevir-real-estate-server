package io.mingachevir.mingachevirrealestateserver.service.impl;

import io.mingachevir.mingachevirrealestateserver.model.dto.*;
import io.mingachevir.mingachevirrealestateserver.model.entity.*;
import io.mingachevir.mingachevirrealestateserver.model.request.CreateSectionRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.InputParameterValueRequest;
import io.mingachevir.mingachevirrealestateserver.repository.*;
import io.mingachevir.mingachevirrealestateserver.service.HouseService;
import io.mingachevir.mingachevirrealestateserver.service.FileService;
import io.mingachevir.mingachevirrealestateserver.util.CustomServiceException;
import io.mingachevir.mingachevirrealestateserver.util.FileFilterEnum;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import io.mingachevir.mingachevirrealestateserver.util.OrderEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HouseServiceImpl implements HouseService {

    private final HouseJpaRepository houseJpaRepository;
    private final HouseRequestJpaRepository houseRequestJpaRepository;
    private final HouseImageJpaRepository houseImageJpaRepository;
    private final SelectiveParameterValueJpaRepository selectiveParameterValueJpaRepository;
    private final ParameterJpaRepository parameterJpaRepository;
    private final SubCategoryJpaRepository subCategoryJpaRepository;
    private final MainCategoryJpaRespository mainCategoryJpaRespository;
    private final InputParameterJpaRepository inputParameterJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SectionJpaRepository sectionJpaRepository;
    private final BrokerRepository brokerRepository;
    private final FileService fileService;

    @Override
    public GenericResponse<List<SectionDto>> getHousesbySectionPage(String page) {
        try {
            List<SectionDto> sectionDtoList = sectionJpaRepository.findAllByEnabledIsTrueAndIsDeletedIsFalseAndPage(page)
                    .stream()
                    .map(SectionDto::new)
                    .collect(Collectors.toList());
            return GenericResponse.ok(sectionDtoList);
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching houses by section page", e.getMessage());
        }
    }

    @Override
    public GenericResponse<HouseDto> getHouseDetails(Long houseId) {
        try {
            House house = houseJpaRepository.findByIdAndEnabledIsTrueAndIsDeletedIsFalse(houseId);
            return GenericResponse.ok(new HouseDto(house));
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching house details", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> saveBrokerInformation(BrokerDto brokerRequest) {
        try {
            Broker broker = this.brokerRepository.findFirstByEnabledIsTrue().orElse(new Broker());
            broker.setEmail(brokerRequest.getEmail());
            broker.setLocation(brokerRequest.getLocation());
            broker.setPhone(brokerRequest.getPhone());
            broker.setFacebook(brokerRequest.getFacebook());
            broker.setInstagram(brokerRequest.getInstagram());
            broker.setTiktok(brokerRequest.getTiktok());
            broker.setEnabled(true);
            broker.setIsDeleted(false);
            broker.setCreateDate(new Date());
            broker.setUpdateDate(new Date());
            this.brokerRepository.save(broker);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error saving broker information", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> createHouseRequest(HouseRequestDto createHouseRequest) {
        try {
            HouseRequest houseRequest = mapToEntityHouseRequest(createHouseRequest);
            houseRequest.setCreateDate(new Date());
            houseRequest.setUpdateDate(new Date());
            houseRequest.setEnabled(true);
            houseRequest.setIsDeleted(false);
            HouseRequest savedHouse = houseRequestJpaRepository.save(houseRequest);
            return GenericResponse.ok(new HouseRequestDto(savedHouse));
        } catch (Exception e) {
            throw new CustomServiceException("Error creating house request", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> makePassiveFile(Long fileId) {
        try {
            HouseImage houseImage = new HouseImage();
            houseImage.setId(fileId);
            houseImage.setEnabled(false);
            houseImageJpaRepository.save(houseImage);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error making file passive", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> deleteFile(Long fileId) {
        try {
            houseImageJpaRepository.deleteById(fileId);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error deleting file", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<SectionDto> createSection(CreateSectionRequest createSectionRequest) {
        try {
            LocalDate localDate = LocalDate.now();
            Date nowDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Section section = new Section();
            section.setName(createSectionRequest.getSectionName());
            section.setEnabled(true);
            section.setIsDeleted(false);
            section.setCreateDate(nowDate);
            section.setUpdateDate(nowDate);
            section.setPage(createSectionRequest.getPage());

            Set<House> houseList = new HashSet<>();
            createSectionRequest.getHouseIds().forEach(houseId -> {
                House house = new House();
                house.setId(houseId);
                houseList.add(house);
            });
            section.setHouses(houseList);
            sectionJpaRepository.save(section);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error creating section", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<SectionDto> updateSection(CreateSectionRequest createSectionRequest) {
        try {
            Section section = sectionJpaRepository.findById(createSectionRequest.getSectionId()).orElse(null);
            if (Objects.isNull(section)) return GenericResponse.resourceNotFound();
            section.setName(createSectionRequest.getSectionName());
            section.getHouses().clear();
            section.setEnabled(true);
            section.setIsDeleted(false);
            section.setUpdateDate(new Date());
            section.setCreateDate(new Date());
            Set<House> houseList = new HashSet<>();
            createSectionRequest.getHouseIds().forEach(houseId -> {
                House house = houseJpaRepository.findById(houseId).orElse(null);
                if (Objects.nonNull(house))
                    houseList.add(house);
            });
            section.setHouses(houseList);
            return GenericResponse.ok(new SectionDto(sectionJpaRepository.save(section)));
        } catch (Exception e) {
            throw new CustomServiceException("Error updating section", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> deleteSection(Long sectionId) {
        try {
            Section section = sectionJpaRepository.findById(sectionId).orElse(null);
            if (Objects.isNull(section)) {
                return GenericResponse.resourceNotFound();
            }
            section.getHouses().clear();
            sectionJpaRepository.delete(section);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error deleting section", e.getMessage());
        }
    }

    public GenericResponse<List<HouseCardDto>> getHousesByFilter(GetFilterHousesRequest request) {
        try {
            GenericResponse<List<HouseCardDto>> houseCardDtos = searchHouses(request);
            if (houseCardDtos.getData().isEmpty()) {
                return GenericResponse.resourceNotFound();
            }
            return houseCardDtos;
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching houses by filter", e.getMessage());
        }
    }

    public GenericResponse<List<HouseCardDto>> searchHouses(GetFilterHousesRequest request) {
        try {
            int pageNumber = request.getPageNumber() != null ? request.getPageNumber() : 1;
            int pageSize = 20;
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, getSort(request.getSort()));

            Page<House> housesPage = houseJpaRepository.findAll((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("enabled"), true));
                predicates.add(cb.equal(root.get("isDeleted"), false));

                Join<House, SelectiveParameterValue> selectiveParamsJoin = root.join("selectiveParameterValues", JoinType.LEFT);
                Join<House, InputParameterValue> inputParamsJoin = root.join("inputParameterValues", JoinType.LEFT);
                Join<InputParameterValue, Parameters> paramJoin = inputParamsJoin.join("parameter", JoinType.LEFT);

                if (request.getSearchKey() != null && !request.getSearchKey().trim().isEmpty()) {
                    String searchKeyLower = "%" + request.getSearchKey().toLowerCase() + "%";
                    List<Predicate> searchPredicates = new ArrayList<>();
                    searchPredicates.add(cb.like(cb.lower(root.get("name")), searchKeyLower));
                    searchPredicates.add(cb.like(cb.lower(root.get("description")), searchKeyLower));

                    Join<House, MainCategory> mainCategoryJoin = root.join("mainCategory", JoinType.LEFT);
                    Join<House, SubCategory> subCategoryJoin = root.join("subCategory", JoinType.LEFT);
                    searchPredicates.add(cb.like(cb.lower(mainCategoryJoin.get("name")), searchKeyLower));
                    searchPredicates.add(cb.like(cb.lower(subCategoryJoin.get("name")), searchKeyLower));
                    searchPredicates.add(cb.like(cb.lower(selectiveParamsJoin.get("name")), searchKeyLower));
                    searchPredicates.add(cb.like(cb.lower(paramJoin.get("name")), searchKeyLower));

                    predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
                }

                if (request.getMainCategoryId() != null) {
                    predicates.add(cb.equal(root.get("mainCategory").get("id"), request.getMainCategoryId()));
                }

                if (request.getSubCategoryId() != null) {
                    predicates.add(cb.equal(root.get("subCategory").get("id"), request.getSubCategoryId()));
                }

                if (request.getSelectiveParameterIds() != null && !request.getSelectiveParameterIds().isEmpty()) {
                    predicates.add(selectiveParamsJoin.get("id").in(request.getSelectiveParameterIds()));
                }

                if (request.getInputParametersRanges() != null && !request.getInputParametersRanges().isEmpty()) {
                    List<Predicate> rangePredicates = new ArrayList<>();
                    for (InputParameterValueRequest range : request.getInputParametersRanges()) {
                        Long parameterId = range.getParameterId();
                        Integer minValue = range.getMin();
                        Integer maxValue = range.getMax();

                        if (parameterId == null || minValue == null || maxValue == null) {
                            continue;
                        }
                        if (minValue > maxValue) {
                            return cb.disjunction();
                        }

                        Predicate parameterIdPredicate = cb.equal(paramJoin.get("id"), parameterId);
                        Predicate valueRangePredicate = cb.between(inputParamsJoin.get("value"), minValue, maxValue);
                        rangePredicates.add(cb.and(parameterIdPredicate, valueRangePredicate));
                    }
                    if (!rangePredicates.isEmpty()) {
                        predicates.add(cb.or(rangePredicates.toArray(new Predicate[0])));
                    }
                }

                query.distinct(true);
                return cb.and(predicates.toArray(new Predicate[0]));
            }, pageable);

            List<HouseCardDto> houseCardDtos = housesPage.getContent().stream()
                    .map(HouseCardDto::new)
                    .collect(Collectors.toList());

            return GenericResponse.ok(pageNumber, pageSize, housesPage.getTotalElements(), houseCardDtos);
        } catch (Exception e) {
            throw new CustomServiceException("Error searching houses", e.getMessage());
        }
    }

    private Sort getSort(OrderEnum sortType) {
        if (sortType == null) {
            return Sort.by(Sort.Direction.DESC, "createDate");
        }
        return switch (sortType) {
            case CHEAP -> Sort.by(Sort.Direction.ASC, "price");
            case EXPENSIVE -> Sort.by(Sort.Direction.DESC, "price");
            case OLD -> Sort.by(Sort.Direction.ASC, "createDate");
            default -> Sort.by(Sort.Direction.DESC, "createDate");
        };
    }

    @Override
    public GenericResponse<List<HouseRequestDto>> getAllHouseRequests() {
        try {
            return GenericResponse.ok(houseRequestJpaRepository.findAllByEnabledIsTrueAndIsDeletedIsFalse()
                    .stream()
                    .map(HouseRequestDto::new)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching all house requests", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> deleteHouseRequest(Long houseRequestId, Boolean forsave) {
        try {
            HouseRequest houseRequest = this.houseRequestJpaRepository.findById(houseRequestId).orElse(null);
            if (Objects.isNull(houseRequest)) {
                return GenericResponse.resourceNotFound();
            }
            if (!forsave) {
                houseRequest.getHouseImages().forEach(image -> {
                    try {
                        this.fileService.deleteFile(image.getPath());
                    } catch (IOException e) {
                        throw new CustomServiceException("Error deleting image during house request deletion", e.getMessage());
                    }
                });
            }
            houseRequest.getHouseImages().clear();
            inputParameterJpaRepository.deleteByHouseRequest(houseRequest);
            houseRequest.getSelectiveParameterValues().clear();
            houseRequestJpaRepository.delete(houseRequest);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error deleting house request", e.getMessage());
        }
    }

    @Override
    public GenericResponse<List<HouseImageDto>> getAllActiveFiles(String filter) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<HouseImage> cq = cb.createQuery(HouseImage.class);
            Root<HouseImage> root = cq.from(HouseImage.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enabled"), true));
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (FileFilterEnum.SMALL.toString().equals(filter)) {
                cq.orderBy(cb.asc(root.get("size")));
            }
            if (FileFilterEnum.BIG.toString().equals(filter)) {
                cq.orderBy(cb.desc(root.get("size")));
            }
            if (FileFilterEnum.OLD.toString().equals(filter)) {
                cq.orderBy(cb.asc(root.get("createDate")));
            }
            if (FileFilterEnum.LAST.toString().equals(filter)) {
                cq.orderBy(cb.desc(root.get("createDate")));
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);
            return GenericResponse.ok(entityManager.createQuery(cq).getResultList().stream()
                    .map(HouseImageDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching active files", e.getMessage());
        }
    }

    @Override
    public GenericResponse<List<HouseImageDto>> getAllPassiveFiles(String filter) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<HouseImage> cq = cb.createQuery(HouseImage.class);
            Root<HouseImage> root = cq.from(HouseImage.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enabled"), true));
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (FileFilterEnum.SMALL.toString().equals(filter)) {
                cq.orderBy(cb.asc(root.get("size")));
            }
            if (FileFilterEnum.BIG.toString().equals(filter)) {
                cq.orderBy(cb.desc(root.get("size")));
            }
            if (FileFilterEnum.OLD.toString().equals(filter)) {
                cq.orderBy(cb.asc(root.get("createDate")));
            }
            if (FileFilterEnum.LAST.toString().equals(filter)) {
                cq.orderBy(cb.desc(root.get("createDate")));
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);
            return GenericResponse.ok(entityManager.createQuery(cq).getResultList().stream()
                    .map(HouseImageDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching passive files", e.getMessage());
        }
    }

    @Override
    public GenericResponse<BrokerDto> getBrokerInformation() {
        try {
            return GenericResponse.ok(new BrokerDto(this.brokerRepository.findFirstByEnabledIsTrue().orElse(new Broker())));
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching broker information", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<HouseDto> createHouse(HouseDto houseDto) {
        try {
            House house = mapToEntity(houseDto);
            house.setCreateDate(new Date());
            house.setUpdateDate(new Date());
            house.setEnabled(true);
            house.setIsDeleted(false);

            House savedHouse = houseJpaRepository.save(house);
            return GenericResponse.ok(new HouseDto(savedHouse));
        } catch (Exception e) {
            throw new CustomServiceException("Error creating house", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<HouseDto> updateHouse(HouseDto houseDto) {
        try {
            if (houseDto.getId() == null) {
                throw new CustomServiceException("House ID is required for update", null);
            }

            Optional<House> existingHouseOpt = houseJpaRepository.findById(houseDto.getId());
            if (!existingHouseOpt.isPresent()) {
                return GenericResponse.resourceNotFound();
            }

            House existingHouse = existingHouseOpt.get();
            updateEntityFromDto(existingHouse, houseDto);
            existingHouse.setUpdateDate(new Date());

            House updatedHouse = houseJpaRepository.save(existingHouse);
            return GenericResponse.ok(new HouseDto(updatedHouse));
        } catch (Exception e) {
            throw new CustomServiceException("Error updating house", e.getMessage());
        }
    }

    @Override
    public GenericResponse<HouseDto> getHouseById(Long id) {
        try {
            Optional<House> houseOpt = houseJpaRepository.findById(id);
            if (!houseOpt.isPresent()) {
                return GenericResponse.resourceNotFound();
            }
            return GenericResponse.ok(new HouseDto(houseOpt.get()));
        } catch (Exception e) {
            throw new CustomServiceException("Error fetching house by ID", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<?> deleteHouse(Long houseId) {
        try {
            House house = this.houseJpaRepository.findById(houseId).orElse(null);
            if (Objects.isNull(house)) return GenericResponse.resourceNotFound();

            if (!house.getHouseImages().isEmpty()) {
                house.getHouseImages().forEach(image -> {
                    try {
                        this.fileService.deleteFile(image.getPath());
                    } catch (IOException e) {
                        throw new CustomServiceException("Error deleting image during house deletion", e.getMessage());
                    }
                });
                house.getHouseImages().clear(); // This ensures no stale references remain
            }
            this.inputParameterJpaRepository.deleteByHouse(house);
            house.getSelectiveParameterValues().clear();
            house.getSections().clear();

            this.houseJpaRepository.delete(house);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Error deleting house", e.getMessage());
        }
    }

    private House mapToEntity(HouseDto dto) {
        try {
            House house = new House();
            house.setName(dto.getName());
            house.setPrice(dto.getPrice());
            house.setPriceType(dto.getPriceType());
            house.setLocation(dto.getLocation());
            house.setDescription(dto.getDescription());
            house.setCoverImage(dto.getCoverImage());
            house.setHouseVideo(dto.getHouseVideo());

            if (dto.getMainCategory() != null) {
                MainCategory mainCategory = mainCategoryJpaRespository.findById(dto.getMainCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid main category ID"));
                house.setMainCategory(mainCategory);
            }
            if (dto.getSubCategory() != null) {
                SubCategory subCategory = subCategoryJpaRepository.findById(dto.getSubCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid subcategory ID"));
                house.setSubCategory(subCategory);
            }

            if (dto.getHouseImages() != null) {
                house.setHouseImages(dto.getHouseImages().stream().map(imageDto -> {
                    HouseImage image = new HouseImage();
                    image.setPath(imageDto.getPath());
                    image.setFileName(imageDto.getFileName());
                    image.setHouse(house);
                    image.setEnabled(true);
                    image.setIsDeleted(false);
                    image.setCreateDate(new Date());
                    image.setUpdateDate(new Date());
                    return image;
                }).collect(Collectors.toList()));
            }

            if (dto.getSelectiveParameters() != null) {
                house.setSelectiveParameterValues(dto.getSelectiveParameters().stream()
                        .map(spDto -> selectiveParameterValueJpaRepository.findById(spDto.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid selective parameter ID")))
                        .collect(Collectors.toSet()));
            }

            if (dto.getInputParameters() != null) {
                house.setInputParameterValues(dto.getInputParameters().stream().map(ipDto -> {
                    InputParameterValue ip = new InputParameterValue();
                    ip.setValue(ipDto.getValue());
                    ip.setEnabled(true);
                    ip.setIsDeleted(false);
                    ip.setCreateDate(new Date());
                    ip.setUpdateDate(new Date());
                    Parameters param = parameterJpaRepository.findById(ipDto.getParameter().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid parameter ID"));
                    ip.setParameter(param);
                    ip.setHouse(house);
                    return ip;
                }).collect(Collectors.toList()));
            }

            return house;
        } catch (Exception e) {
            throw new CustomServiceException("Error mapping house DTO to entity", e.getMessage());
        }
    }

    private void updateEntityFromDto(House house, HouseDto dto) {
        try {
            house.setName(dto.getName());
            house.setPrice(dto.getPrice());
            house.setPriceType(dto.getPriceType());
            house.setLocation(dto.getLocation());
            house.setDescription(dto.getDescription());
            house.setCoverImage(dto.getCoverImage());
            house.setHouseVideo(dto.getHouseVideo());

            if (dto.getMainCategory() != null) {
                MainCategory mainCategory = mainCategoryJpaRespository.findById(dto.getMainCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid main category ID"));
                house.setMainCategory(mainCategory);
            }
            if (dto.getSubCategory() != null) {
                SubCategory subCategory = subCategoryJpaRepository.findById(dto.getSubCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid subcategory ID"));
                house.setSubCategory(subCategory);
            }

            if (dto.getHouseImages() != null) {
                houseImageJpaRepository.deleteByHouse(house);
                house.getHouseImages().clear();
                dto.getHouseImages().forEach(imageDto -> {
                    HouseImage image = new HouseImage();
                    image.setPath(imageDto.getPath());
                    image.setFileName(imageDto.getFileName());
                    image.setHouse(house);
                    image.setEnabled(true);
                    image.setIsDeleted(false);
                    image.setCreateDate(new Date());
                    image.setUpdateDate(new Date());
                    house.getHouseImages().add(image);
                });
            }

            if (dto.getSelectiveParameters() != null) {
                house.getSelectiveParameterValues().clear();
                dto.getSelectiveParameters().forEach(spDto -> {
                    SelectiveParameterValue sp = selectiveParameterValueJpaRepository.findById(spDto.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid selective parameter ID"));
                    house.getSelectiveParameterValues().add(sp);
                });
            }

            if (dto.getInputParameters() != null) {
                this.inputParameterJpaRepository.deleteByHouse(house);
                dto.getInputParameters().forEach(ipDto -> {
                    InputParameterValue ip = new InputParameterValue();
                    ip.setValue(ipDto.getValue());
                    Parameters param = parameterJpaRepository.findById(ipDto.getParameter().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid parameter ID"));
                    ip.setParameter(param);
                    ip.setHouse(house);
                    ip.setEnabled(true);
                    ip.setIsDeleted(false);
                    ip.setCreateDate(new Date());
                    ip.setUpdateDate(new Date());
                    if (Objects.nonNull(ipDto.getValue()) && ipDto.getValue() > 0) {
                        house.getInputParameterValues().add(ip);
                    }
                });
            }
        } catch (Exception e) {
            throw new CustomServiceException("Error updating house entity from DTO", e.getMessage());
        }
    }

    private HouseRequest mapToEntityHouseRequest(HouseRequestDto dto) {
        try {
            HouseRequest houseRequest = new HouseRequest();
            houseRequest.setRequester(dto.getRequester());
            houseRequest.setPrice(dto.getPrice());
            houseRequest.setPriceType(dto.getPriceType());
            houseRequest.setLocation(dto.getLocation());
            houseRequest.setNumber(dto.getNumber());
            houseRequest.setCoverImage(dto.getCoverImage());

            if (dto.getMainCategory() != null) {
                MainCategory mainCategory = mainCategoryJpaRespository.findById(dto.getMainCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid main category ID"));
                houseRequest.setMainCategory(mainCategory);
            }
            if (dto.getSubCategory() != null && Objects.nonNull(dto.getSubCategory().getId()) && dto.getSubCategory().getId() != 0) {
                SubCategory subCategory = subCategoryJpaRepository.findById(dto.getSubCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid subcategory ID"));
                houseRequest.setSubCategory(subCategory);
            }

            if (dto.getHouseImages() != null) {
                houseRequest.setHouseImages(dto.getHouseImages().stream().map(imageDto -> {
                    HouseImage image = new HouseImage();
                    image.setPath(imageDto.getPath());
                    image.setFileName(imageDto.getFileName());
                    image.setHouseRequest(houseRequest);
                    image.setEnabled(true);
                    image.setIsDeleted(false);
                    image.setCreateDate(new Date());
                    image.setUpdateDate(new Date());
                    return image;
                }).collect(Collectors.toList()));
            }

            if (dto.getSelectiveParameters() != null) {
                houseRequest.setSelectiveParameterValues(dto.getSelectiveParameters().stream()
                        .map(spDto -> selectiveParameterValueJpaRepository.findById(spDto.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid selective parameter ID")))
                        .collect(Collectors.toSet()));
            }

            if (dto.getInputParameters() != null) {
                houseRequest.setInputParameterValues(dto.getInputParameters().stream().map(ipDto -> {
                    InputParameterValue ip = new InputParameterValue();
                    ip.setValue(ipDto.getValue());
                    ip.setEnabled(true);
                    ip.setIsDeleted(false);
                    ip.setCreateDate(new Date());
                    ip.setUpdateDate(new Date());
                    Parameters param = parameterJpaRepository.findById(ipDto.getParameter().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid parameter ID"));
                    ip.setParameter(param);
                    ip.setHouseRequest(houseRequest);
                    return ip;
                }).collect(Collectors.toList()));
            }

            return houseRequest;
        } catch (Exception e) {
            throw new CustomServiceException("Error mapping house request DTO to entity", e.getMessage());
        }
    }
}
