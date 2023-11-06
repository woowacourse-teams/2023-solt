package dev.tripdraw.area.application;

import dev.tripdraw.area.domain.Area;
import dev.tripdraw.area.domain.AreaRepository;
import dev.tripdraw.area.dto.AreaResponse;
import dev.tripdraw.area.dto.FullAreaResponse;
import dev.tripdraw.area.dto.FullAreaResponses;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AreaService {

    private final AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public AreaResponse read(String sido, String sigungu) {
        if (sido.isBlank()) {
            return readAllSidos();
        }

        if (sigungu.isBlank()) {
            return readAllSigungusOf(sido);
        }

        return readAllUmdsOf(sido, sigungu);
    }

    private AreaResponse readAllSidos() {
        List<Area> areas = areaRepository.findAll();
        List<String> sidos = areas.stream()
                .map(Area::sido)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(sidos);
    }

    private AreaResponse readAllSigungusOf(String sido) {
        List<Area> areasOfSido = areaRepository.findBySido(sido);
        List<String> sigungus = areasOfSido.stream()
                .map(Area::sigungu)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(sigungus);
    }

    private AreaResponse readAllUmdsOf(String sido, String sigungu) {
        List<Area> areasOfSigunguAndSido = areaRepository.findBySidoAndSigungu(sido, sigungu);
        List<String> umds = areasOfSigunguAndSido.stream()
                .map(Area::umd)
                .distinct()
                .sorted()
                .toList();

        return AreaResponse.from(umds);
    }

    @CacheEvict(cacheNames = "areas", key = "'allAreas'")
    public void create(List<Area> areas) {
        long count = areaRepository.count();
        if (count != 0) {
            return;
        }

        areaRepository.deleteAllInBatch();
        areaRepository.saveAll(areas);
    }

    @Cacheable(cacheNames = "areas", key = "'allAreas'")
    @Transactional(readOnly = true)
    public FullAreaResponses readAll() {
        List<Area> areas = areaRepository.findAll();
        List<FullAreaResponse> areaResponses = areas.stream()
                .map(FullAreaResponse::from)
                .collect(Collectors.toList());

        return FullAreaResponses.from(areaResponses);
    }
}
