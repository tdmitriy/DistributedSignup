package com.newage.systemtests;

import com.newage.systemtests.repository.PlayerRepository;
import org.awaitility.Duration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        MockitoTestExecutionListener.class})
public abstract class AbstractSystemTest {

    @Autowired
    protected PlayerRepository playerRepository;

    @After
    public void afterTests() {
        playerRepository.deleteAll();
    }

    public static final Duration POOL_FIVE_SECONDS = new Duration(5, SECONDS);
    public static final Duration WAIT_FIFTEEN_SECONDS = new Duration(15, SECONDS);

    protected void awaitUntil(Callable<Boolean> conditionEvaluator) {
        await().atMost(WAIT_FIFTEEN_SECONDS).pollInterval(POOL_FIVE_SECONDS).until(conditionEvaluator);
    }

    protected void awaitUntil(Callable<Boolean> conditionEvaluator, Duration duration) {
        await().atMost(duration).pollInterval(POOL_FIVE_SECONDS).until(conditionEvaluator);
    }

    protected void awaitUntil(Callable<Boolean> conditionEvaluator, Duration duration, Duration interval) {
        await().atMost(duration).pollInterval(interval).until(conditionEvaluator);
    }
}
