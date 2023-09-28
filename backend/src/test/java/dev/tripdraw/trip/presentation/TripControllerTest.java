package dev.tripdraw.trip.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.TestFixture.서울_2022_1_2_일;
import static dev.tripdraw.test.fixture.TestFixture.서울_2023_1_1_일;
import static dev.tripdraw.test.fixture.TestFixture.제주_2023_1_1_일;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static dev.tripdraw.test.step.PostStep.createPostAtCurrentPoint;
import static dev.tripdraw.test.step.TripStep.createTripAndGetResponse;
import static dev.tripdraw.trip.domain.TripStatus.FINISHED;
import static dev.tripdraw.trip.presentation.TripControllerTest.PostRequestFixture.위치정보_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchResponse;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripControllerTest extends ControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private String huchuToken;
    private Trip huchuTrip;
    private String reoToken;
    private Trip reoTrip;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Member huchu = memberRepository.save(새로운_사용자("통후추"));
        Member reo = memberRepository.save(새로운_사용자("리오"));
        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
        reoToken = jwtTokenProvider.generateAccessToken(reo.id().toString());
        huchuTrip = tripRepository.save(새로운_여행(huchu));
        reoTrip = tripRepository.save(새로운_여행(reo));
    }

    @Test
    void 여행을_생성한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().post("/trips")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

        // then
        TripCreateResponse tripCreateResponse = response.as(TripCreateResponse.class);
        assertThat(tripCreateResponse.tripId()).isNotNull();
    }

    @Test
    void 여행에_위치_정보를_추가한다() {
        // given
        PointCreateRequest request = 위치정보_생성_요청(huchuTrip.id());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

        // then
        PointCreateResponse pointCreateResponse = response.as(PointCreateResponse.class);
        assertThat(pointCreateResponse.pointId()).isNotNull();
    }

    @Test
    void 위치_정보_추가_시_인증에_실패하면_예외를_발생시킨다() {
        // given
        PointCreateRequest request = 위치정보_생성_요청(huchuTrip.id());

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(reoToken)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 여행을_ID로_조회한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/{tripId}", huchuTrip.id())
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        // then
        TripResponse tripResponse = response.as(TripResponse.class);
        assertThat(tripResponse).isEqualTo(TripResponse.from(huchuTrip));
    }

    @Test
    void 특정_위치정보를_삭제한다() {
        // given
        Point point = pointRepository.save(새로운_위치정보(huchuTrip));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .param("tripId", huchuTrip.id())
                .when().delete("/points/{pointId}", point.id())
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void 특정_위치정보_삭제시_인증에_실패하면_예외를_발생시킨다() {
        // given
        Point point = pointRepository.save(새로운_위치정보(huchuTrip));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(reoToken)
                .param("tripId", huchuTrip.id())
                .when().delete("/points/{pointId}", point.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 특정_회원의_전체_여행을_조회한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/me")
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        // then
        TripsSearchResponseOfMember tripsSearchResponseOfMember = response.as(TripsSearchResponseOfMember.class);
        assertThat(tripsSearchResponseOfMember)
                .usingRecursiveComparison()
                .isEqualTo(TripsSearchResponseOfMember.from(List.of(huchuTrip)));
    }

    @Test
    void 여행의_이름과_상태를_수정한다() {
        // given
        TripUpdateRequest tripUpdateRequest = new TripUpdateRequest("제주도 여행", FINISHED);

        // when
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(tripUpdateRequest)
                .when().patch("/trips/{tripId}", huchuTrip.id())
                .then().log().all()
                .statusCode(NO_CONTENT.value());

        // then
        Trip trip = tripRepository.getById(huchuTrip.id());
        assertSoftly(softly -> {
            softly.assertThat(trip.nameValue()).isEqualTo("제주도 여행");
            softly.assertThat(trip.status()).isEqualTo(FINISHED);
        });
    }

    @Test
    void 위치_정보를_조회한다() {
        // given
        Point point = pointRepository.save(새로운_위치정보(huchuTrip));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .param("tripId", huchuTrip.id())
                .when().get("/points/{pointId}", point.id())
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        // then
        PointResponse pointResponse = response.as(PointResponse.class);
        assertThat(pointResponse).usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(PointResponse.from(point));
    }

    @Test
    void 여행을_삭제한다() {
        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/trips/{tripId}", huchuTrip.id())
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void 여행을_삭제할_때_인증에_실패하면_예외가_발생한다() {
        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(reoToken)
                .when().delete("/trips/{tripId}", huchuTrip.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 감상이_있는_모든_여행을_조건으로_조회할_수_있다() {
        // given
        Long 후추_서울_2022_1_2_일 = createTripAndGetResponse(huchuToken).tripId();
        createPostAtCurrentPoint(서울_2022_1_2_일(후추_서울_2022_1_2_일), huchuToken);

        Long 리오_제주_2023_1_1_일 = createTripAndGetResponse(reoToken).tripId();
        createPostAtCurrentPoint(제주_2023_1_1_일(리오_제주_2023_1_1_일), reoToken);

        Long 리오_서울_2023_1_1_일 = createTripAndGetResponse(reoToken).tripId();
        createPostAtCurrentPoint(서울_2023_1_1_일(리오_서울_2023_1_1_일), reoToken);

        Map<String, Object> params = Map.of(
                "years", Set.of(2023, 2021),
                "daysOfWeek", Set.of(1),
                "address", "seoul",
                "limit", 10
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .params(params)
                .when().get("/trips")
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        // then
        TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);
        assertThat(tripsSearchResponse.trips())
                .extracting(TripSearchResponse::tripId)
                .containsExactly(리오_서울_2023_1_1_일);
    }

    static class PostRequestFixture {
        public static PointCreateRequest 위치정보_생성_요청(Long tripId) {
            return new PointCreateRequest(tripId, 1.1, 2.2, LocalDateTime.now());
        }
    }
}
