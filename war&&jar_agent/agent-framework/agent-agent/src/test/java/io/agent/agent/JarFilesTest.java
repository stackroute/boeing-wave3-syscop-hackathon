// Copyright 2017 The agent Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.agent.agent;

import org.junit.jupiter.api.Test;

import static io.agent.agent.JarFiles.findAgentJarFromCmdline;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class JarFilesTest {

    @Test
    void testCmdlineParserWildfly() {
        // The command line arguments are taken from the Wildfly application server example.
        String[] cmdlineArgs = new String[]{
                "-D[Standalone]",
                "-Xbootclasspath/p:/tmp/wildfly-10.1.0.Final/modules/system/layers/base/org/jboss/logmanager/main/jboss-logmanager-2.0.4.Final.jar",
                "-Djboss.modules.system.pkgs=org.jboss.logmanager,io.agent.agent",
                "-Djava.util.logging.manager=org.jboss.logmanager.LogManager",
                "-javaagent:../agent/agent-dist/target/agent.jar=port=9300",
                "-Dorg.jboss.boot.log.file=/tmp/wildfly-10.1.0.Final/standalone/log/server.log",
                "-Dlogging.configuration=file:/tmp/wildfly-10.1.0.Final/standalone/configuration/logging.properties"
        };
        assertEquals("../agent/agent-dist/target/agent.jar", findAgentJarFromCmdline(asList(cmdlineArgs)).toString());
    }

    @Test
    void testCmdlineParserVersioned() {
        String[] cmdlineArgs = new String[] {
                "-javaagent:agent-1.0-SNAPSHOT.jar"
        };
        assertEquals("agent-1.0-SNAPSHOT.jar", findAgentJarFromCmdline(asList(cmdlineArgs)).toString());
    }

    @Test()
    void testCmdlineParserFailed() {
        String[] cmdlineArgs = new String[] {
                "-javaagent:/some/other/agent.jar",
                "-jar",
                "agent.jar"
        };
        Exception e = assertThrows(Exception.class, () -> findAgentJarFromCmdline(asList(cmdlineArgs)));
        // The exception should contain some message indicating agent.jar was not found.
        assertTrue(e.getMessage().contains("agent.jar"));
    }
}
