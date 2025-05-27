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

import java.util.HashMap;

import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

/**
 * Class handler to get the configuration settings screen for a PowerShell Task in a pipeline
 */
public class GetViewRequest {
    /**
     * This function runs on the server, to get the configuration settings screen for a PowerShell Task in a pipeline
     * @return A GoPluginApiResponse containing the HTML template for the PowerShell task configuration
     */
    public GoPluginApiResponse execute() {
        int responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
        HashMap<String, String> view = new HashMap<>();
        view.put("displayValue", "PowerShell");
        try {
            view.put("template", Util.readResource("/task.template.html"));
        } catch (Exception e) {
            responseCode = DefaultGoApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            TaskPlugin.LOGGER.error(errorMessage, e);
        }
        TaskPlugin.LOGGER.info(TaskPlugin.GSON.toJson(view));
        return new DefaultGoPluginApiResponse(responseCode, TaskPlugin.GSON.toJson(view));
    }
}
