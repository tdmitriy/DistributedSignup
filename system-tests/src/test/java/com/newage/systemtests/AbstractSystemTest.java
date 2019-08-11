package com.newage.systemtests;

import com.newage.systemtests.repository.PlayerRepository;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSystemTest {

    @Autowired
    protected PlayerRepository playerRepository;

    @After
    public void afterTests() {
        playerRepository.deleteAll();
    }
}
