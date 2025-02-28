package codesquad.kr.gyeonggidoidle.issuetracker.domain.label.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import codesquad.kr.gyeonggidoidle.issuetracker.annotation.RepositoryTest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.Label;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.repository.VO.LabelDetailsVO;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.repository.VO.LabelVO;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RepositoryTest
public class LabelRepositoryTest {

    private LabelRepository repository;

    @Autowired
    public LabelRepositoryTest(NamedParameterJdbcTemplate template) {
        this.repository = new LabelRepository(template);
    }

    @DisplayName("이슈 아이디로 해당 이슈의 모든 label 정보를 불러온다.")
    @Test
    void findAllByIssueIdTest() {
        List<LabelVO> actual = repository.findAllByIssueId(3L);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getName()).isEqualTo("라벨 1");
        assertThat(actual.get(0).getBackgroundColor()).isEqualTo("#F08080");
        assertThat(actual.get(1).getTextColor()).isEqualTo("#000000");
    }

    @DisplayName("모든 라벨을 찾아 이름 순으로 반환한다.")
    @Test
    void testFindAll() {

        List<LabelDetailsVO> actual = repository.findAll();
        assertThat(actual.size()).isEqualTo(4);
        assertThat(actual.get(0).getName()).isEqualTo("라벨 0");
        assertThat(actual.get(1).getName()).isEqualTo("라벨 1");
        assertThat(actual.get(2).getName()).isEqualTo("라벨 2");
        assertThat(actual.get(3).getName()).isEqualTo("라벨 3");

    }

    @DisplayName("Label을 받아서 db에 저장하고 성공하면 true를 반환한다.")
    @Test
    void save() {
        // given
        Label label = Label.builder()
                .name("label")
                .description("test")
                .backgroundColor("##")
                .textColor("#")
                .build();
        // when
        boolean actual = repository.save(label);
        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("라벨id를 입력받아 라벨을 반환한다.")
    @Test
    void findById() {
        // when
        LabelDetailsVO actual = repository.findById(1L);

        // then
        assertSoftly(assertions -> {
            assertions.assertThat(actual.getId()).isEqualTo(1L);
            assertions.assertThat(actual.getName()).isEqualTo("라벨 1");
            assertions.assertThat(actual.getDescription()).isEqualTo(null);
            assertions.assertThat(actual.getBackgroundColor()).isEqualTo("#F08080");
        });
    }

    @DisplayName("라벨 내용을 수정하고 성공하면 true를 반환한다.")
    @Test
    void update() {
        // given
        Label label = Label.builder()
                .id(1L)
                .name("update title")
                .description("tmp")
                .backgroundColor("##")
                .textColor("##")
                .build();
        // when
        boolean actual = repository.update(label);
        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("라벨 아이디를 받아 is_deleted를 true로 바꾸고 성공하면 true를 반환한다")
    @Test
    void delete() {
        // when
        boolean actual = repository.delete(2L);
        // then
        assertThat(actual).isTrue();
    }
}
