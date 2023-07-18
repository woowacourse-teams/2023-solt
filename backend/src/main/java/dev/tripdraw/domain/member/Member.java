package dev.tripdraw.domain.member;

import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nickname;

    @Builder
    public Member(String nickname) {
        this.nickname = nickname;
    }
}