package com.epam.dockercompscan;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test-application.properties")
@ContextConfiguration(classes = TestApplication.class)
public abstract class AbstractSpringTest {
}
