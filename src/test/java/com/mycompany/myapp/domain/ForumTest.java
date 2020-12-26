package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class ForumTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forum.class);
        Forum forum1 = new Forum();
        forum1.setId(1L);
        Forum forum2 = new Forum();
        forum2.setId(forum1.getId());
        assertThat(forum1).isEqualTo(forum2);
        forum2.setId(2L);
        assertThat(forum1).isNotEqualTo(forum2);
        forum1.setId(null);
        assertThat(forum1).isNotEqualTo(forum2);
    }
}
