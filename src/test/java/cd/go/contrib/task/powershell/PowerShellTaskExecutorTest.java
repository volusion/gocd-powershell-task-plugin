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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

public class PowerShellTaskExecutorTest {
    @Test
    void canExecuteCommand() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String pwshCommand = TestUtil.readResource("/fixtures/requests/pwsh-command.json");
        request.setRequestBody(pwshCommand);

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().size(), equalTo(1));
        assertThat(mockConsoleLogger.getPrintLines().get(0), equalTo("Launching command: [pwsh, -NonInteractive, -Command, -]"));
        assertThat(mockConsoleLogger.getStdOut(), containsString("Hello World"));
    }

    @Test
    @EnabledOnOs(WINDOWS)
    void canExecuteCommandUsingWindowsPowerShell() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String powershellCommand = TestUtil.readResource("/fixtures/requests/powershell-command.json");
        request.setRequestBody(powershellCommand);

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().size(), equalTo(1));
        assertThat(mockConsoleLogger.getPrintLines().get(0), equalTo("Launching command: [PowerShell.exe, -NonInteractive, -Command, -]"));
        assertThat(mockConsoleLogger.getStdOut(), containsString("Hello World"));
    }

    @Test
    void canExecuteFile() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String pwshCommand = TestUtil.readResource("/fixtures/requests/pwsh-file.json");
        request.setRequestBody(pwshCommand);

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().size(), equalTo(1));
        assertThat(mockConsoleLogger.getPrintLines().get(0), equalTo("Launching command: [pwsh, -NonInteractive, -File, ./src/test/resources/fixtures/scripts/hello-world.ps1]"));
        assertThat(mockConsoleLogger.getStdOut(), containsString("Hello, World!"));
    }

    @Test
    @EnabledOnOs(WINDOWS)
    void canExecuteFileUsingWindowsPowerShell() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String powershellCommand = TestUtil.readResource("/fixtures/requests/powershell-file.json");
        request.setRequestBody(powershellCommand);

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().size(), equalTo(1));
        assertThat(mockConsoleLogger.getPrintLines().get(0), equalTo("Launching command: [PowerShell.exe, -NonInteractive, -File, ./src/test/resources/fixtures/scripts/hello-world.ps1]"));
        assertThat(mockConsoleLogger.getStdOut(), containsString("Hello, World!"));
    }

    @Test
    void canExecuteWithNoProfile() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String pwshCommand = TestUtil.readResource("/fixtures/requests/pwsh-command.json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(pwshCommand, JsonObject.class);

        JsonObject configObject = jsonObject.getAsJsonObject("config");
        JsonObject noProfileObject = configObject.getAsJsonObject("NoProfile");
        noProfileObject.addProperty("value", true);
        request.setRequestBody(gson.toJson(jsonObject));

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().get(0), containsString("-NoProfile"));
    }

    @Test
    void canExecuteWithNoLogo() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String pwshCommand = TestUtil.readResource("/fixtures/requests/pwsh-command.json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(pwshCommand, JsonObject.class);

        JsonObject configObject = jsonObject.getAsJsonObject("config");
        JsonObject noLogoObject = configObject.getAsJsonObject("NoLogo");
        noLogoObject.addProperty("value", true);
        request.setRequestBody(gson.toJson(jsonObject));

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().get(0), containsString("-NoLogo"));
    }

    @Test
    void canExecuteWithExecutionPolicy() {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        String pwshCommand = TestUtil.readResource("/fixtures/requests/pwsh-command.json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(pwshCommand, JsonObject.class);

        JsonObject configObject = jsonObject.getAsJsonObject("config");
        JsonObject executionPolicyObject = configObject.getAsJsonObject("ExecutionPolicy");
        executionPolicyObject.addProperty("value", "Bypass");
        request.setRequestBody(gson.toJson(jsonObject));

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");
        MockConsoleLogger mockConsoleLogger = new MockConsoleLogger(context);

        Result result = executor.execute(new TaskConfig(config), new Context(context), mockConsoleLogger);

        assertThat(result.responseCode(), equalTo(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));
        assertThat(mockConsoleLogger.getPrintLines().get(0), containsString("-ExecutionPolicy, Bypass"));
    }
}
