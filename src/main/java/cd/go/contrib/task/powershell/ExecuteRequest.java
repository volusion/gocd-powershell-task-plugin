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

import java.util.Map;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;

/**
 * Class handler for GoPluginApiRequest
 */
public class ExecuteRequest {
    /**
     * This function receives the GoPluginApiRequest from the server, then invokes the PowerShellTaskExecutor
     * @param request The API request from the GoCD Server that contains the configuration and context for the PowerShell execution
     * @return The result of the PowerShell execution, including the response code and any output from the PowerShell script
     */
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        PowerShellTaskExecutor executor = new PowerShellTaskExecutor();
        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");

        Result result = executor.execute(new TaskConfig(config), new Context(context), JobConsoleLogger.getConsoleLogger());
        return new DefaultGoPluginApiResponse(result.responseCode(), TaskPlugin.GSON.toJson(result.toMap()));
    }
}
