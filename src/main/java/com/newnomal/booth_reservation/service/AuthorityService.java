package com.newnomal.booth_reservation.service;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.domain.entity.Authority;
import com.newnomal.booth_reservation.domain.request.AuthorityRequest;
import com.newnomal.booth_reservation.domain.response.AuthorityResponse;
import com.newnomal.booth_reservation.domain.state.AuthorityState;
import com.newnomal.booth_reservation.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    //Authority생성
    public ResponseEntity<RestResult<Object>> createAuthority(AuthorityRequest authorityRequest) {
        Authority authority = new Authority();
        updateAuthorityFromRequest(authority, authorityRequest);
        authority.setStatus(AuthorityState.VALID);
        authorityRepository.save(authority);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new AuthorityResponse(authority)));
    }


    //Authority 정보 반환
    public ResponseEntity<RestResult<Object>> getAuthority(Long id) {
        Optional<Authority> authority = authorityRepository.findById(id);
        if (authority.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT",new RestResult<>("NOT_FOUND","존재하지 않는 기관입니다")));
        }
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new AuthorityResponse(authority.get())));
    }

    public ResponseEntity<RestResult<Object>> getAllStateValid() {
        List<Authority> authorityList = authorityRepository.getAllStateValid();
        List<AuthorityResponse> authorityResponseList = authorityList.stream().map(AuthorityResponse::new).toList();
        return ResponseEntity.ok(new RestResult<>("SUCCESS",authorityResponseList));
    }

    //Authority 업데이트
    public ResponseEntity<RestResult<Object>> updateAuthority(Long id, AuthorityRequest authorityRequest) {
        Optional<Authority> optionalAuthority = authorityRepository.findById(id);
        if (optionalAuthority.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT", new RestResult<>("NOT_FOUND", "존재하지 않는 기관입니다")));
        }
        Authority authority = optionalAuthority.get();
        updateAuthorityFromRequest(authority, authorityRequest);
        authorityRepository.save(authority);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new AuthorityResponse(authority)));
    }

    //Authority 상태 삭제로 변경
    public ResponseEntity<RestResult<Object>> deleteAuthority(Long id) {
        Optional<Authority> optionalAuthority = authorityRepository.findById(id);
        if (optionalAuthority.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT", new RestResult<>("NOT_FOUND", "존재하지 않는 기관입니다")));
        }
        Authority authority = optionalAuthority.get();
        authority.setStatus(AuthorityState.DELETED);
        authorityRepository.save(authority);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", "기관 상태 삭제 변경 완료"));
    }

    private void updateAuthorityFromRequest(Authority authority, AuthorityRequest request) {
        authority.setName(request.getName());
        authority.setDescription(request.getDescription());
        authority.setImage(request.getImage());
        authority.setLatitude(request.getLatitude());
        authority.setLongitude(request.getLongitude());
        authority.setBoothStartTimezone(request.getBoothStartTimezone());
        authority.setBoothEndTimezone(request.getBoothEndTimezone());
        authority.setMaxTimeZoneNumber(request.getMaxTimeZoneNumber());
        authority.setWeekHolidays(request.getWeekHolidays());
    }
}