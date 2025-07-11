package com.epam.dockercompscan.owasp;

import com.epam.dockercompscan.AbstractSpringTest;
import com.epam.dockercompscan.owasp.analyzer.RPackageAnalyzer;
import com.epam.dockercompscan.scan.domain.Dependency;
import org.junit.Assert;
import org.junit.Test;
import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class DependencyCheckServiceTest extends AbstractSpringTest {

    private ClassLoader classLoader = DependencyCheckServiceTest.class.getClassLoader();

    @Autowired
    private DependencyCheckService dependencyCheckService;

    @Test
    public void dependencyCheckServiceTest() throws URISyntaxException, ExceptionCollection {
        List<Dependency> dependencies =
                dependencyCheckService.runScan(new File(classLoader.getResource("owasp/analyzer").toURI()));

        Assert.assertEquals(1, dependencies.size());
        Assert.assertEquals(RPackageAnalyzer.DEPENDENCY_ECOSYSTEM, dependencies.get(0).getEcosystem());
        Assert.assertEquals("PositiveTest", dependencies.get(0).getName());
    }
}
