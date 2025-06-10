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

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

/**
 * The main plugin class that implements the GoPlugin interface.
 * It handles requests from the GoCD server and provides responses.
 */
@Extension
public class TaskPlugin implements GoPlugin {

    /**
     * Default PowerShell executable to select in a PowerShell task configuration
     */
    public static final String POWERSHELL_EXE_DEFAULT = "PowerShell.exe";

    /**
     * Property name for the PowerShell `Version` task configuration.
     */
    public static final String POWERSHELL_EXE_PROPERTY = "PowerShellVersion";

    /**
     * Property name for the PowerShell `Mode` task configuration.
     */
    public static final String MODE_PROPERTY = "Mode";

    /**
     * Property name for the PowerShell `File` task configuration.
     */
    public static final String FILE_PROPERTY = "File";

    /**
     * Property name for the PowerShell `File` task configuration.
     */
    public static final String FILE_FROM_ENV_PROPERTY = "FileFromEnv";

    /**
     * Property name for the PowerShell `Command` task configuration.
     */
    public static final String COMMAND_PROPERTY = "Command";

    /**
     * Property name for the PowerShell `NoProfile` task configuration.
     */
    public static final String NO_PROFILE_PROPERTY = "NoProfile";

    /**
     * Property name for the PowerShell `NoLogo` task configuration.
     */
    public static final String NO_LOGO_PROPERTY = "NoLogo";

    /**
     * Property name for the PowerShell `ExecutionPolicy` task configuration.
     */
    public static final String EXECUTION_POLICY_PROPERTY = "ExecutionPolicy";

    /**
     * Gson instance for serializing and deserializing JSON.
     */
    public static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * Logger instance for logging messages from the plugin.
     */
    public static Logger LOGGER = Logger.getLoggerFor(TaskPlugin.class);

    /**
     * Initializes the plugin with the GoApplicationAccessor.
     * @param goApplicationAccessor The GoApplicationAccessor instance to interact with the GoCD server.
     */
    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
    }

    /**
     * Handles incoming requests from the GoCD server.
     * @param request The request from the GoCD server.
     * @return A response to the request, which could be a configuration, view, validation, execution result, or an icon.
     */
    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        if ("configuration".equals(request.requestName())) {
            return new GetConfigRequest().execute();
        } else if ("view".equals(request.requestName())) {
            return new GetViewRequest().execute();
        } else if ("validate".equals(request.requestName())) {
            return new ValidateRequest().execute(request);
        } else if ("execute".equals(request.requestName())) {
            return new ExecuteRequest().execute(request);
        } else if ("go.cd.task.get-icon".equals(request.requestName())) {
            return new GetPluginSettingsIconExecutor().execute();
        }
        throw new UnhandledRequestTypeException(request.requestName());
    }

    /**
     * Returns the identifier for the plugin.
     * @return The type of plugin and its GoAPI version number.
     */
    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("task", Arrays.asList("1.0"));
    }
}
