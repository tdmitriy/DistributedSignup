package com.newage.persistenceservice;

import com.newage.persistenceservice.config.PostgresTestConfig;
import com.newage.persistenceservice.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {PostgresTestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        MockitoTestExecutionListener.class})

public abstract class AbstractApplicationTest {

    @Autowired
    protected PlayerRepository playerRepository;

    @After
    public void cleanupDB() {
        playerRepository.deleteAll();
    }

}
