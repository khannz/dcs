package com.epam.dockercompscan.owasp.analyzer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.owasp.dependencycheck.analyzer.exception.AnalysisException;
import org.owasp.dependencycheck.dependency.Dependency;

public class RPackageAnalyzerTest {

    private RPackageAnalyzer rPackageAnalyzer;

    @Before
    public void setUp() {
        rPackageAnalyzer = new RPackageAnalyzer();
    }

    @Test
    public void analyzeDependencyTestPositive() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/positive/DESCRIPTION").getPath());
        rPackageAnalyzer.analyzeDependency(dependency, null);

        Assert.assertEquals(RPackageAnalyzer.DEPENDENCY_ECOSYSTEM, dependency.getEcosystem());
        Assert.assertEquals("PositiveTest", dependency.getName());
        Assert.assertEquals("1.0", dependency.getVersion());
    }

    @Test
    public void analyzeDependencyTestNegative() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/negative/etc/DESCRIPTION").getPath());
        rPackageAnalyzer.analyzeDependency(dependency, null);

        Assert.assertNull(dependency.getEcosystem());
    }
}
