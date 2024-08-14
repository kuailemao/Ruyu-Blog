package xyz.kuailemao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BlogBackendApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void contextLoads() throws Exception {
//        String encode = passwordEncoder.encode("123456");
//        System.out.println(encode);
    }

}
