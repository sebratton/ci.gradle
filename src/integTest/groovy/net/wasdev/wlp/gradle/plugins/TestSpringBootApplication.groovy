package net.wasdev.wlp.gradle.plugins

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

public class TestSpringBootApplication extends AbstractIntegrationTest{
    static File resourceDir = new File("build/resources/integrationTest/sample.springboot")
    static File buildDir = new File(integTestDir, "/test-spring-boot-application")
    static String buildFilename = "springboot_archive.gradle"

    @BeforeClass
    public static void setup() {
        createDir(buildDir)
        createTestProject(buildDir, resourceDir, buildFilename)
    }

    @AfterClass
    public static void tearDown() throws Exception {
        runTasks(buildDir, 'libertyStop')
    }

    @Test
    public void testAppStarts() {
        try {
            runTasks(buildDir, 'installApps')
        } catch (Exception e) {
            throw new AssertionError ("Fail on task installApps. " + e)
        }
    }
}
