package com.goodee.corpdesk.position.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.corpdesk.position.dto.PositionDTO;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionService {

    
    private final PositionRepository positionRepository;
    
    
    
    
    
    
    public void deleteOneAndReparent(Integer positionId) {
        Position target = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("직위를 찾을 수 없습니다. id=" + positionId));

        Integer parentId = target.getParentPositionId(); // null이면 최상위

        // 1) 삭제 대상의 직계 자식 수 검사 (부모당 자식 1명 정책)
        int childCount = positionRepository.countByParentPositionIdAndUseYnTrue(positionId);
        if (childCount > 1) {
            throw new IllegalStateException("해당 직위의 하위 직위가 2개 이상이라 삭제할 수 없습니다. 먼저 하위 직위를 정리하세요.");
        }

        // 2) 부모가 이미 '삭제 대상 이외의' 다른 자식을 갖고 있는지 검사
        if (parentId != null) {
            int siblingsAtParent =
                    positionRepository.countActiveChildrenExcluding(parentId, java.util.List.of(positionId));
            if (siblingsAtParent > 0 && childCount > 0) {
                // 부모에 이미 자식이 있는 상태에서 또 다른 자식을 올리려 함 → 제약 위반
                throw new IllegalStateException("상위 직위에 이미 다른 자식 직위가 있어 재연결할 수 없습니다.");
            }
        }

        // 3) 먼저 삭제 대상 소프트 삭제 (부모의 자식 슬롯 확보)
        positionRepository.softDelete(positionId);

     // 자식이 1명이면 재연결
        if (childCount == 1) {
            // 루트로 승격되는 경우(삭제 대상의 parent==null) → 루트 단일성 체크
            if (parentId == null &&
                positionRepository.existsByParentPositionIdIsNullAndUseYnTrue()) {
                // 방금 삭제한 루트가 use_yn=false가 되었으므로, 이 체크는 "다른 활성 루트가 남아있냐"를 본다.
                throw new IllegalStateException("이미 다른 최상위 직위가 존재하여 루트 승격이 불가합니다.");
            }
            positionRepository.reparentChildren(positionId, parentId); // parentId==null이면 루트 승격
        }
    }
    
    
    public void deleteAllAndReparent(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;

        // 삭제 대상의 parent 맵 조회
        Map<Integer, Integer> parentMap = positionRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Position::getPositionId, Position::getParentPositionId));

        Set<Integer> toDelete = new HashSet<>(ids);

        for (Integer id : ids) {
            // 1) 새 parent 찾기: 삭제 집합에 포함되지 않은 가장 가까운 조상
        	Integer newParent = parentMap.get(id);
        	while (newParent != null && toDelete.contains(newParent)) {
        	    // parentMap에 없으면 DB에서 parent를 읽어옴
        	    Integer next = parentMap.containsKey(newParent)
        	            ? parentMap.get(newParent)
        	            : positionRepository.findById(newParent)
        	                  .map(Position::getParentPositionId)
        	                  .orElse(null);
        	    newParent = next;
        	}
            
            //  2) 새 부모의 남는 활성 자식 검사 (삭제 대상은 제외)
            int remaining = positionRepository.countActiveChildrenExcluding(newParent, ids);
            if (remaining > 0) {
                throw new IllegalStateException("상위 직위에 이미 자식 직위가 있어 재연결할 수 없습니다. id=" + newParent);
            }
            
            // 2) 자식들 재연결
            positionRepository.reparentChildren(id, newParent);
        }

        // 3) 마지막에 한 번만 벌크 소프트 삭제
            positionRepository.softDeleteIn(ids);
        
    }
    
    

    public Integer getPositionIdByName(String positionName) {
        return positionRepository.findByPositionName(positionName)
                .map(Position::getPositionId)
                .orElse(null);
    }
    
 // 직위 목록 + 사용 사원 수 + 검색
    public List<PositionDTO> getAllWithEmployeeCount(String keyword) {
        // 1) 전체 목록 or 검색 결과 로딩 (기존 그대로)
        final List<PositionDTO> all = (keyword == null || keyword.trim().isEmpty())
                ? positionRepository.findAllWithEmployeeCount()
                : positionRepository.findAllWithEmployeeCountByKeyword(keyword.trim());

        // 검색 모드면 기존 정렬 유지(평면 목록)
        if (keyword != null && !keyword.trim().isEmpty()) {
            return all;
        }

        // 2) 루트 찾기 (parentPositionId == null) — 없으면 그대로 반환
        final PositionDTO root = all.stream()
                .filter(dto -> dto.getParentPositionId() == null)
                .findFirst()
                .orElse(null);
        if (root == null) return all;

        // 3) parentId -> child DTO 매핑 (체인 구조라 0 또는 1개)
        Map<Integer, PositionDTO> childByParent = new HashMap<>();
        for (PositionDTO dto : all) {
            Integer parentId = dto.getParentPositionId();
            if (parentId != null) childByParent.put(parentId, dto);
        }

        // 4) 루트부터 아래로 순회하며 순서 재구성
        List<PositionDTO> ordered = new ArrayList<>(all.size());
        PositionDTO cur = root;
        // 안전장치: 순환 방지 (이상 데이터 대비)
        int guard = 0, limit = all.size() + 2;

        while (cur != null && guard++ < limit) {
            ordered.add(cur);
            cur = childByParent.get(cur.getPositionId()); // 체인 구조상 최대 1명
        }

        // 5) 혹시 고립 노드가 있으면(데이터 꼬임 대비) 뒤에 덧붙이기
        if (ordered.size() != all.size()) {
            // 아직 미포함된 것만 추가
            var remaining = all.stream()
                    .filter(dto -> ordered.stream().noneMatch(o -> o.getPositionId().equals(dto.getPositionId())))
                    .collect(Collectors.toList());
            ordered.addAll(remaining);
        }

        return ordered;
    }

    //  직위 생성: save 사용
    public void create(String positionName, Integer parentPositionId) {
        String trimmed = (positionName == null) ? null : positionName.strip();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("직위명은 필수입니다.");
        }
        if (trimmed.length() > 50) {
            throw new IllegalArgumentException("직위명은 50자 이내여야 합니다.");
        }
        if (positionRepository.existsByPositionNameAndUseYnTrue(trimmed)) {
            throw new IllegalArgumentException("이미 존재하는 직위명입니다: " + trimmed);
        }

        // ✅ 루트 생성 요청이면: 새 루트 만들고 기존 루트(있다면) 강등
        if (parentPositionId == null) {
            createNewRootAndDemoteOld(trimmed);
            return;
        }

        // 상위 직위 존재/활성 확인
        if (positionRepository.findByPositionIdAndUseYnTrue(parentPositionId).isEmpty()) {
            throw new IllegalArgumentException("상위 직위가 존재하지 않거나 비활성 상태입니다: " + parentPositionId);
        }

        // 상위에 이미 자식이 있으면 '끼워넣기'
        if (positionRepository.existsByParentPositionIdAndUseYnTrue(parentPositionId)) {
            insertUnderParent(parentPositionId, trimmed);
            return;
        }

        // 평범한 생성
        Position p = new Position();
        p.setPositionName(trimmed);
        p.setParentPositionId(parentPositionId);
        p.setUseYn(true);
        positionRepository.save(p);
    }




    
    // ✅ 일괄 삭제: JPA 기본 제공 메서드 사용
    public void deleteAll(List<Integer> ids) {
        List<Position> positions = positionRepository.findAllById(ids);
        for (Position p : positions) {
            p.setUseYn(false);
        }
        positionRepository.saveAll(positions);
    }
    
    public List<Position> getAllActive() {
        return positionRepository.findByUseYnTrueOrderByPositionIdAsc();
    }
    
    public void changeParent(Integer positionId, Integer newParentId) {
        Position p = positionRepository.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("직위 없음: " + positionId));

        // (선택) 비활성 노드 변경 금지 정책
        // if (!Boolean.TRUE.equals(p.getUseYn())) {
        //     throw new IllegalStateException("비활성 직위는 상위 변경이 불가합니다.");
        // }

        Integer currentParentId = p.getParentPositionId();

        // 0) 무의미한 변경은 패스
        if ((currentParentId == null && newParentId == null) ||
            (currentParentId != null && currentParentId.equals(newParentId))) {
            return; // no-op
        }

        // 1) 자기 참조 금지
        if (newParentId != null && positionId.equals(newParentId)) {
            throw new IllegalArgumentException("자기 자신을 상위로 지정할 수 없습니다.");
        }

        // 2) 루트 단일성: p가 현재 루트가 아닐 때만 단일성 검사
        if (newParentId == null && currentParentId != null &&
            positionRepository.existsByParentPositionIdIsNullAndUseYnTrue()) {
            throw new IllegalStateException("최상위 직위는 하나만 가능합니다.");
        }

        // 3) 새 부모 존재/활성 검증
        if (newParentId != null &&
            positionRepository.findByPositionIdAndUseYnTrue(newParentId).isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 상위 직위입니다: " + newParentId);
        }

        // 4) 부모당 자식 1개 정책: 새 부모에 이미 다른 자식이 있으면 금지
        if (newParentId != null &&
            positionRepository.existsByParentPositionIdAndUseYnTrue(newParentId)) {
            throw new IllegalStateException("해당 상위 직위에는 이미 자식 직위가 존재합니다.");
        }

        // 5) 사이클 방지: newParentId 사슬 상에 positionId가 있으면 금지
        Integer cursor = newParentId;
        while (cursor != null) {
            if (cursor.equals(positionId)) {
                throw new IllegalStateException("상위 변경 시 계층 순환이 발생합니다.");
            }
            cursor = positionRepository.findById(cursor)
                .map(Position::getParentPositionId)
                .orElse(null);
        }

        // 6) 변경 적용
        p.setParentPositionId(newParentId);
    }

    @PersistenceContext
    private EntityManager em; // flush용

    /**
     * 체인 구조에서 parent 밑에 새 직위를 끼워넣는다.
     * - 부모 parent 아래 기존 자식(있다면) 잠시 NULL로 떼어냄
     * - 새 직위를 parent의 자식으로 저장
     * - 기존 자식을 새 직위의 자식으로 재부모지정
     * @return 새 직위 ID
     */
    public Integer insertUnderParent(Integer parentId, String newName) {
        if (parentId == null) {
            throw new IllegalArgumentException("루트 바로 아래 끼워넣기는 금지(정책)입니다.");
        }

        positionRepository.findByPositionIdAndUseYnTrue(parentId)
            .orElseThrow(() -> new IllegalArgumentException("상위 직위가 존재하지 않거나 비활성입니다: " + parentId));

        // 1) 기존 단일 자식 찾아두기
        var existingChildOpt = positionRepository.findByParentPositionIdAndUseYnTrue(parentId);

        // 2) 유니크 충돌 방지: 기존 자식이 있으면 먼저 parent=null로 떼어냄
        existingChildOpt.ifPresent(child -> {
            positionRepository.setParent(child.getPositionId(), null);
        });
        em.flush();

        // 3) 새 직위 생성 (부모 = parentId)
        Position p = new Position();
        p.setPositionName(newName.strip());
        p.setParentPositionId(parentId);
        p.setUseYn(true);
        positionRepository.save(p);
        em.flush();
        Integer newId = p.getPositionId();

        // 4) 기존 자식이 있었다면 새 직위 밑으로 연결
        existingChildOpt.ifPresent(child -> {
            positionRepository.setParent(child.getPositionId(), newId);
        });

        return newId;
    }

    public Integer createNewRootAndDemoteOld(String newName) {
        String name = (newName == null) ? null : newName.strip();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("직위명은 필수입니다.");
        }

        // 현재 활성 루트 조회 (※ Repository에 아래 메서드 필요: findByParentPositionIdIsNullAndUseYnTrue)
        List<Position> roots = positionRepository.findByParentPositionIdIsNullAndUseYnTrue();
        if (roots.size() > 1) {
            throw new IllegalStateException("활성 최상위 직위가 2개 이상입니다. 데이터 정합성을 먼저 맞춰주세요.");
        }
        Position oldRoot = roots.isEmpty() ? null : roots.get(0);

        // 새 루트 생성
        Position newRoot = new Position();
        newRoot.setPositionName(name);
        newRoot.setParentPositionId(null);
        newRoot.setUseYn(true);
        positionRepository.save(newRoot);
        em.flush(); // ID 확보

        Integer newRootId = newRoot.getPositionId();

        // 기존 루트가 있으면 새 루트의 자식으로 강등
        if (oldRoot != null) {
            // 부모당 자식 1개 정책에 위배 없음(새 루트는 방금 생성되어 자식 없음)
            positionRepository.setParent(oldRoot.getPositionId(), newRootId);
        }

        return newRootId;
    }
    
}
