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
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

/**
 * Class handler for validating the PowerShell task configuration
 */
public class ValidateRequest {
    /**
     * This function receives a PowerShell task configuration and validates it
     * @param request The API request that contains the configuration to validate
     * @return The validation result, including any errors found in the configuration
     */
    public GoPluginApiResponse execute(GoPluginApiRequest request) {
        Map configMap = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        HashMap errorMap = new HashMap<String, String>();

        if (!configMap.containsKey(TaskPlugin.POWERSHELL_EXE_PROPERTY) || ((Map) configMap.get(TaskPlugin.POWERSHELL_EXE_PROPERTY)).get("value") == null || ((String) ((Map) configMap.get(TaskPlugin.POWERSHELL_EXE_PROPERTY)).get("value")).trim().isEmpty()) {
            errorMap.put(TaskPlugin.POWERSHELL_EXE_PROPERTY, "PowerShell Version cannot be empty");
        }

        if (!configMap.containsKey(TaskPlugin.MODE_PROPERTY) || ((Map) configMap.get(TaskPlugin.MODE_PROPERTY)).get("value") == null || ((String) ((Map) configMap.get(TaskPlugin.MODE_PROPERTY)).get("value")).trim().isEmpty()) {
            errorMap.put(TaskPlugin.MODE_PROPERTY, "Mode cannot be empty");
        }

        HashMap<String, Object> validationResult = new HashMap<>();
        validationResult.put("errors", errorMap);
        return new DefaultGoPluginApiResponse(DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE, TaskPlugin.GSON.toJson(validationResult));
    }
}
