package dev.tripdraw.member.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.test.ServiceTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PostRepository postRepository;

    private Member member;
    private Trip trip;
    private Post post;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        trip = tripRepository.save(new Trip(TripName.from("통후추의 여행"), member));
        Point point = new Point(3.14, 5.25, LocalDateTime.now());
        trip.add(point);
        post = postRepository.save(new Post(
                "제목",
                point,
                "위치",
                "오늘은 날씨가 좋네요.",
                member,
                trip.id()
        ));
    }

    @Test
    void 사용자를_조회한다() {
        // given
        LoginUser loginUser = new LoginUser(member.id());

        // when
        MemberSearchResponse response = memberService.find(loginUser);

        // expect
        assertThat(response).usingRecursiveComparison().isEqualTo(
                new MemberSearchResponse(member.id(), "통후추")
        );
    }

    @Test
    void 사용자를_삭제한다() {
        // given
        LoginUser loginUser = new LoginUser(member.id());

        // when
        memberService.delete(loginUser);

        // then
        assertSoftly(softly -> {
            softly.assertThat(memberRepository.findById(member.id())).isEmpty();
            softly.assertThat(tripRepository.findById(trip.id())).isEmpty();
            softly.assertThat(postRepository.findById(post.id())).isEmpty();
        });
    }
}
