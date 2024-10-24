import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitQuiz {
    @Test
    public void junitTest() {
        String name1 = "홍길동";
        String name2 = "홍길동";
        String name3 = "홍길은";

//        Assertions.assertNotNull(name1);
//        Assertions.assertNotNull(name2);
//        Assertions.assertNotNull(name3);
//
//        Assertions.assertEquals(name2, name1);
//        Assertions.assertNotEquals(name2, name3);

        assertThat(name1).isNotNull();
        assertThat(name2).isNotNull();
        assertThat(name3).isNotNull();

        assertThat(name1).isEqualTo(name2);
        assertThat(name2).isNotEqualTo(name3);
    }
}
