package com.epam.dockercompscan.owasp.analyzer;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.dependencycheck.analyzer.exception.AnalysisException;
import org.owasp.dependencycheck.dependency.Dependency;

public class NvidiaCudaAnalyzerTest {
    private final NvidiaCudaAnalyzer nvidiaCudaAnalyzer = new NvidiaCudaAnalyzer();

    @Test
    public void shouldAnalyzeNvidiaVersion() throws AnalysisException {
        final Dependency dependency = new Dependency();
        nvidiaCudaAnalyzer.analyzeDependency(dependency, null);

        Assert.assertEquals(NvidiaCudaAnalyzer.DEPENDENCY_ECOSYSTEM, dependency.getEcosystem());
        Assert.assertEquals(NvidiaCudaAnalyzer.DEPENDENCY_NAME, dependency.getName());
    }
}
