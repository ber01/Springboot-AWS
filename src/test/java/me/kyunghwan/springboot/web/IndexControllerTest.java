package me.kyunghwan.springboot.web;

import me.kyunghwan.springboot.domain.posts.Posts;
import me.kyunghwan.springboot.domain.posts.PostsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    PostsRepository postsRepository;

    @Test
    public void mainLoad() {
        // when
        String body = this.restTemplate.getForObject("/", String.class);

        // then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }

    @Test
    public void mainPosts() {
        // given
        IntStream.rangeClosed(0, 2).forEach(i -> {
            postsRepository.save(
                    Posts.builder()
                            .title("title" + i)
                            .content("content" + i)
                            .author("author" + i)
                            .build()
            );
        });

        // when
        String body = restTemplate.getForObject("/", String.class);

        // then
        assertThat(body).contains("title0");
        assertThat(body).contains("title1");
        assertThat(body).contains("title2");

        assertThat(body).contains("author0");
        assertThat(body).contains("author1");
        assertThat(body).contains("author2");
    }

}