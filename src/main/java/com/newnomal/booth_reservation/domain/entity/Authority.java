package com.newnomal.booth_reservation.domain.entity;


import com.newnomal.booth_reservation.domain.state.AuthorityState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "auhhority_table")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200, nullable = false)
    private String name;//기관 명
    @Column(length = 1000, nullable = false)
    private String description;//기관 설명
    @Column(length = 1000)
    private String image;//S3와 연동된 기관 대표 이미지 주소
    private Float latitude;//위도
    private Float longitude;//경도

    private Integer boothStartTimezone;//부스 시작 시간
    private Integer boothEndTimezone;//부스 종료 시간
    private Integer maxTimeZoneNumber;//하루 최대 이용 가능 TimeZone 수
    private AuthorityState status;//1.부스 이용 가능 상태 2.부스 이용 불가능 상태 3.기관 삭제 상태
    private String weekHolidays;//쉬는 요일 "월, 화, 수"식으로 정규화 없이 삽입


    @OneToMany(fetch = FetchType.LAZY)
    private List<Holiday> holidayList;


}
