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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Main PowerShell Task Executor
 */
public class PowerShellTaskExecutor {

    /**
     * Execution handler
     * @param taskConfig The configuration for the PowerShell task, including the executable path, mode, file, command, and execution policy
     * @param taskContext The runtime context for the task, including environment variables and working directory
     * @param console The console logger to log output and errors during the execution of the PowerShell task
     * @return A Result object indicating the success or failure of the PowerShell execution, along with any relevant messages
     */
    public Result execute(TaskConfig taskConfig, Context taskContext, JobConsoleLogger console) {
        try {
            return runCommand(taskContext, taskConfig, console);
        } catch (Exception e) {
            String errorMessage = "PowerShell execution failed: " + e.getMessage();
            console.printLine(errorMessage);
            return new Result(false, errorMessage);
        }
    }

    /**
     * Run the PowerShell process
     * @param taskContext The runtime context for the task, including environment variables and working directory
     * @param taskConfig The configuration for the PowerShell task, including the executable path, mode, file, command, and execution policy
     * @param console The console logger to log output and errors during the execution of the PowerShell task
     * @return A Result object indicating the success or failure of the PowerShell execution, along with any relevant messages
     * @throws IOException
     * @throws InterruptedException
     */
    private Result runCommand(Context taskContext, TaskConfig taskConfig, JobConsoleLogger console) throws IOException, InterruptedException {
        ProcessBuilder powershell = createPowerShellProcessWithOptions(taskContext, taskConfig);
        console.printLine("Launching command: " + powershell.command());
        powershell.environment().putAll(taskContext.getEnvironmentVariables());
        console.printEnvironment(powershell.environment());

        Process powershellProcess = powershell.start();

        if (taskConfig.getMode().equals("Command")) {
            OutputStream outputStream = powershellProcess.getOutputStream();
            String commandScript = taskConfig.getCommand();
            // Write the commandScript string directly to the process's standard input
            outputStream.write(commandScript.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        console.readErrorOf(powershellProcess.getErrorStream());
        console.readOutputOf(powershellProcess.getInputStream());

        int exitCode = powershellProcess.waitFor();
        powershellProcess.destroy();

        if (exitCode != 0) {
            return new Result(false, "PowerShell execution failed. Please check the output.");
        }

        return new Result(true, "PowerShell execution complete.");
    }

    /**
     * Helper function to create the PowerShell process
     * @param taskConfig The configuration for the PowerShell task, including the executable path, mode, file, command, and execution policy
     * @param taskContext The runtime context for the task, including environment variables and working directory
     * @return A ProcessBuilder configured with the PowerShell command and options
     */
    ProcessBuilder createPowerShellProcessWithOptions(Context taskContext, TaskConfig taskConfig) {
        List<String> command = new ArrayList<String>();
        command.add(taskConfig.getExe());

        command.add("-NonInteractive");

        if (taskConfig.getNoProfile()) {
            command.add("-NoProfile");
        }

        if (taskConfig.getNoLogo()) {
            command.add("-NoLogo");
        }

        if (!taskConfig.getExecutionPolicy().equals("Default")) {
            command.add("-ExecutionPolicy");
            command.add(taskConfig.getExecutionPolicy());
        }

        if (taskConfig.getMode().equals("File")) {
            command.add("-File");
            command.add(taskConfig.getFile());
        } else if (taskConfig.getMode().equals("Command")) {
            command.add("-Command");
            command.add("-");
        }

        return new ProcessBuilder(command);
    }
}
