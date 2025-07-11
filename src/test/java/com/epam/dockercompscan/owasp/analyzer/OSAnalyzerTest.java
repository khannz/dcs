package com.epam.dockercompscan.owasp.analyzer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.owasp.dependencycheck.analyzer.exception.AnalysisException;
import org.owasp.dependencycheck.dependency.Dependency;

public class OSAnalyzerTest {

    private OSVersionAnalyzer osVersionAnalyzer;

    @Before
    public void setUp() {
        osVersionAnalyzer = new OSVersionAnalyzer();
    }

    @Test
    public void analyzeDependencyTestPositiveOsRelease() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/positive/os/os-case1/etc/os-release").getPath());
        osVersionAnalyzer.analyzeDependency(dependency, null);

        Assert.assertEquals(OSVersionAnalyzer.DEPENDENCY_ECOSYSTEM, dependency.getEcosystem());
        Assert.assertEquals("fedora", dependency.getName());
        Assert.assertEquals("31", dependency.getVersion());
    }

    @Test
    public void analyzeDependencyTestPositiveOsRelease2() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/positive/os/os-case2/etc/os-release").getPath());
        osVersionAnalyzer.analyzeDependency(dependency, null);

        Assert.assertEquals(OSVersionAnalyzer.DEPENDENCY_ECOSYSTEM, dependency.getEcosystem());
        Assert.assertEquals("ubuntu", dependency.getName());
        Assert.assertEquals("18.04", dependency.getVersion());
    }

    @Test
    public void analyzeDependencyTestPositiveCentosRelease() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/positive/os/etc/centos-release").getPath());
        osVersionAnalyzer.analyzeDependency(dependency, null);

        Assert.assertEquals(OSVersionAnalyzer.DEPENDENCY_ECOSYSTEM, dependency.getEcosystem());
        Assert.assertEquals("centos", dependency.getName());
        Assert.assertEquals("6.10", dependency.getVersion());
    }

    @Test
    public void analyzeDependencyTestNegativeOsRelease() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/negative/etc/os-release").getPath());
        osVersionAnalyzer.analyzeDependency(dependency, null);

        Assert.assertNull(dependency.getEcosystem());
    }

    @Test
    public void analyzeDependencyTestNegativeCentosRelease() throws AnalysisException {
        Dependency dependency = new Dependency();
        dependency.setActualFilePath(this.getClass().getClassLoader().getResource(
                "owasp/analyzer/negative/etc/centos-release").getPath());
        osVersionAnalyzer.analyzeDependency(dependency, null);

        Assert.assertNull(dependency.getEcosystem());
    }

}
