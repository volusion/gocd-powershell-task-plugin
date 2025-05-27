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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

/**
 * Utility class for helpful functions
 */
public class Util {
    /**
     * Read a resource file and return a String of its contents
     * @param resourceFile the resource file to read
     * @return the contents of the resource file
     */
    public static String readResource(String resourceFile) {
        try (InputStream inputStream = Util.class.getResourceAsStream(resourceFile)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourceFile);
            }
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not find resource " + resourceFile, e);
        }
    }

    /**
     * Read a resource file and return a byte array of its contents (e.g. for binary files)
     * @param resourceFile the resource file to read
     * @return the contents of the resource file
     */
    public static byte[] readResourceBytes(String resourceFile) {
        try (InputStream inputStream = Util.class.getResourceAsStream(resourceFile)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourceFile);
            }
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not find resource " + resourceFile, e);
        }
    }
}
