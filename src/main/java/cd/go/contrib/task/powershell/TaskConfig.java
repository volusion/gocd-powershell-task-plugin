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

/**
 * Class to hold the PowerShell Task Configuration data
 */
public class TaskConfig {
    private final String exe;
    private final String mode;
    private final String file;
    private final String command;
    private final boolean noProfile;
    private final boolean noLogo;
    private final String executionPolicy;

    /**
     * Construct a task configuration from a Map (e.g. derived from JSON request)
     * @param config The configuration map containing the task properties
     */
    public TaskConfig(Map config) {
        exe = getValue(config, TaskPlugin.POWERSHELL_EXE_PROPERTY);
        mode = getValue(config, TaskPlugin.MODE_PROPERTY);
        file = getValue(config, TaskPlugin.FILE_PROPERTY);
        command = getValue(config, TaskPlugin.COMMAND_PROPERTY);
        noProfile = getBooleanValue(config, TaskPlugin.NO_PROFILE_PROPERTY);
        noLogo = getBooleanValue(config, TaskPlugin.NO_LOGO_PROPERTY);
        executionPolicy = getValue(config, TaskPlugin.EXECUTION_POLICY_PROPERTY);
    }

    /**
     * Get a string value in the config
     * @param config
     * @param property
     * @return
     */
    private String getValue(Map config, String property) {
        return (String) ((Map) config.get(property)).get("value");
    }

    /**
     * Get a boolean value in the config
     * @param config
     * @param property
     * @return
     */
    private boolean getBooleanValue(Map config, String property) {
        Object value = ((Map) config.get(property)).get("value");

        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }

        return (boolean) value;
    }

    /**
     * Get the PowerShell Task `Version` to run
     * @return "PowerShell.exe" for Windows Poweror "pwsh" for PowerShell7+
     */
    public String getExe() {
        return exe;
    }

    /**
     * Get the PowerShell Task `Mode` configuration
     * @return "File" or "Command"
     */
    public String getMode() {
        return mode;
    }

    /**
     * Get the PowerShell Task `File` path
     * @return The path of the PowerShell script to run
     */
    public String getFile() {
        return file;
    }

    /**
     * Get the PowerShell Task `Command` to run
     * @return The PowerShell script to pass into PowerShell's standard input
     */
    public String getCommand() {
        return command;
    }

    /**
     * Get the PowerShell Task `NoProfile` configuration
     * @return Whether or not to pass `-NoProfile` to PowerShell
     */
    public boolean getNoProfile() {
        return noProfile;
    }

    /**
     * Get the PowerShell Task `NoLogo` configuration
     * @return Whether or not to pass `-NoLogo` to PowerShell
     */
    public boolean getNoLogo() {
        return noLogo;
    }

    /**
     * Get the PowerShell Task `ExecutionPolicy` configuration
     * @return The execution policy to set for the PowerShell process, e.g. "Default", "Bypass", "Unrestricted"
     */
    public String getExecutionPolicy() {
        return executionPolicy;
    }
}
