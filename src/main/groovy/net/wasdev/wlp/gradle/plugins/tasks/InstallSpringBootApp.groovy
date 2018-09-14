/**
 * (C) Copyright IBM Corporation 2018.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wasdev.wlp.gradle.plugins.tasks

import net.wasdev.wlp.common.plugins.util.PluginExecutionException
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

import java.text.MessageFormat

class InstallSpringBootApp extends InstallAppsTask {

    String taskName = 'InstallSpringBootApp'

    InstallSpringBootApp () {
        configure({
            description "Install a spring boot application generated by the gradle project to a Liberty server's " +
                    "dropins or apps directory."
            logging.level = LogLevel.INFO
            group 'Liberty'
        })
    }

    String getBootArchiveOutputPath() {
        /*This is for springboot plugin >= 2.0.
        project.plugins.hasPlugin(org.springboot)
          TODO add logic for < 2.0.0  */
        return project.bootJar.archivePath.getAbsolutePath()
    }

    String getParentLibCachePath() {

    }

    String getTargetLibCachePath() {
        new File(getInstallDir(project), "usr/shared/resources/lib.index.cache").absolutePath
    }

    String getTargetThinAppPath() {
        new File (createApplicationFolder("dropins/spring").absolutePath, "${project.bootJar.archiveName}")
    }

    @TaskAction
    void install( ) {
        if (server.looseApplication) {
            logger.info(MessageFormat.format(("Loose application configuration is not supported for spring boot applications." +
                    " The project artifact will be installed as an archive file."),
                    ))
            server.looseApplication = false;
        }
        installSpringBootFeature()
        checkForCommandLineUtil()
        invokeThinOperation()
    }

    private checkForCommandLineUtil() {
        //if ()
    }

    private invokeThinOperation() {
        Map<String, String> params = buildLibertyMap(project);

        project.ant.taskdef(name: 'invokeUtil',
                classname: 'net.wasdev.wlp.ant.SpringBootUtilTask',
                classpath: project.buildscript.configurations.classpath.asPath)

        params.put('sourceAppPath', getBootArchiveOutputPath())
        params.put('targetLibCachePath', getTargetLibCachePath())
        params.put('targetThinAppPath', getTargetThinAppPath())
        project.ant.invokeUtil(params)
    }

    private installSpringBootFeature() {
        //TODO check if product version NOT OL. Only neeed to install feature on WLP
        Map<String, String> params = buildLibertyMap(project);

        project.ant.taskdef(name: 'installFeature',
                classname: 'net.wasdev.wlp.ant.InstallFeatureTask',
                classpath: project.buildscript.configurations.classpath.asPath)

        params.put('acceptLicense', server.features.acceptLicense)
        params.put('name', "springBoot-2.0")
        project.ant.installFeature(params)
    }

    protected void configureSpringBootApp(Project project) {
        if ((server.apps == null || server.apps.isEmpty()) && (server.dropins == null || server.dropins.isEmpty())) {
            if (project.plugins.hasPlugin('org.springframework.boot')) {
                server.apps = [project.war]
            } else {
                throw new PluginExecutionException("The org.springframework.boot must be configured to use the " + taskName +
                        " task.")
            }
        }
    }
}
