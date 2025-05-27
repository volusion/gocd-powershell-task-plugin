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

import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;

/**
 * Class to represent the result of the PowerShell process execution
 */
public class Result {
    private boolean success;
    private String message;
    private Exception exception;

    /**
     * Construct a result without an exception
     * @param success Whether or not the PowerShell process completed successfully
     * @param message The message to communicate back as the result of the PowerShell execution
     */
    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Construct a result with an exception
     * @param success Whether or not the PowerShell process completed successfully
     * @param message The message to communicate back as the result of the PowerShell execution
     * @param exception The exception that the PowerShell execution encountered
     */
    public Result(boolean success, String message, Exception exception) {
        this(success, message);
        this.exception = exception;
    }

    /**
     * Convert this object to a Map (e.g. to send in a JSON response)
     * @return A Map representation of this Response
     */
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);
        result.put("exception", exception);
        return result;
    }

    /**
     * Get the response code for the result
     * @return The appropriate successful or unsuccessful response code
     */
    public int responseCode() {
        return success ? DefaultGoApiResponse.SUCCESS_RESPONSE_CODE : DefaultGoApiResponse.INTERNAL_ERROR;
    }
}
