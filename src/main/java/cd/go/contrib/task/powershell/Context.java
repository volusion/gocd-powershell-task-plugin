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
 * Runtime context for the Process
 */
public class Context {
    private final Map<String, String> environmentVariables;
    private final String workingDir;

    /**
     * Construct a context from a Map (e.g. derived from JSON request)
     * @param context The map containing environment variables and working directory
     */
    @SuppressWarnings("unchecked")
    public Context(Map<String, Object> context) {
        environmentVariables = (Map<String, String>) context.get("environmentVariables");
        workingDir = (String) context.get("workingDirectory");
    }

    /**
     * Gets the environment variables for the context
     * @return Environment variables
     */
    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    /**
     * Get a single environment variable for the context
     * @param key The environment variable name
     * @return The environment variable value
     */
    public String getEnvironmentVariable(String key) {
        return environmentVariables.get(key);
    }

    /**
     * Gets the working directory for the context
     * @return The working directory path
     */
    public String getWorkingDir() {
        return workingDir;
    }
}
