package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository.save(Q1);
        userRepository.save(JAVAJIGI);
    }

    @DisplayName("deleted가 false이고 해당 question Id를 가진 answer를 찾는다.")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer answer = new Answer(JAVAJIGI, Q1, "내용입니다.");
        answerRepository.save(answer);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(Q1.getId());

        assertAll(
                () -> assertThat(answers).hasSize(1),
                () -> assertThat(answers).containsExactly(answer)
        );
    }

    @DisplayName("deleted가 false이고 해당 answer Id를 가진 answer를 찾는다.")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = new Answer(JAVAJIGI, Q1, "내용입니다.");
        answerRepository.save(answer);
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        assertThat(answer).isSameAs(findAnswer);
    }

    @DisplayName("deleted가 false이고 해당 answer Id를 가진 answer가 없으면 empty()가 반환된다.")
    @Test
    void findByIdAndDeletedFalseWhenFailure() {
        Long noAnswerId = -1L;

        assertThat(answerRepository.findByIdAndDeletedFalse(noAnswerId)).isEmpty();
    }
}
