package com.newnomal.booth_reservation.domain.entity;


import com.newnomal.booth_reservation.domain.state.BoothState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "booth_table", indexes = @Index(name = "authorityIndex",columnList = "authorityId"))
public class Booth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authorityId;//매핑은 굳이 안해도 상관없음
    private Integer boothNumber;//기관에 속한 개인 부스의 번호
    private BoothState state;//1.부스 이용 가능 상태 2.부스 이용 불가능 상태 3.부스 삭제 상태

}
