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

import java.util.Base64;
import java.util.HashMap;

import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

/**
 * Class handler to get the PowerShell task plugin icon.
 * It appears that this is not presently supported for task pugins...
 */
public class GetPluginSettingsIconExecutor {
    /**
     * This function runs on the server, to get the icon for the PowerShell task plugin
     * @return A GoPluginApiResponse containing the icon for the PowerShell task plugin
     */
    public GoPluginApiResponse execute() {
        int responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
        HashMap<String, String> view = new HashMap<>();
        view.put("content_type", "image/svg+xml");
        try {
            view.put("data", Base64.getEncoder().encodeToString(Util.readResourceBytes("/plugin-icon.svg")));
        } catch (Exception e) {
            responseCode = DefaultGoApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            TaskPlugin.LOGGER.error(errorMessage, e);
        }
        return new DefaultGoPluginApiResponse(responseCode, TaskPlugin.GSON.toJson(view));
    }
}
