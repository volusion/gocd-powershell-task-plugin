/*
 * Copyright 2025 Volusion, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.contrib.task.powershell;

import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class MockConsoleLogger extends JobConsoleLogger {
    protected Map context;
    protected ArrayList<String> printLines;
    protected String stdOut;
    protected String stdErr;

    protected MockConsoleLogger() {
        super();
    }

    protected MockConsoleLogger(Map context) {
        super();
        this.context = context;
        this.printLines = new ArrayList<>();
    }

    public static MockConsoleLogger getConsoleLogger() {
        return new MockConsoleLogger();
    }

    @Override
    public void printLine(String line) {
        printLines.add(line);
        System.out.println(line);
    }

    @Override
    public void readOutputOf(InputStream in) {
        try {
            this.stdOut = IOUtils.toString(in, StandardCharsets.UTF_8);
            in.transferTo(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readErrorOf(InputStream in) {
        try {
            this.stdErr = IOUtils.toString(in, StandardCharsets.UTF_8);
            in.transferTo(System.err);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printEnvironment(Map<String, String> environment) {
//        context.console().printEnvironment(environment, context.environment().secureEnvSpecifier());
    }

    public ArrayList<String> getPrintLines() {
        return printLines;
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }
}
