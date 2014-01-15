/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.logstash.logback.encoder;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LogstashEncoderV1Test extends LogstashEncoderTestCommon {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private LogstashEncoderV1 encoder;
    private ByteArrayOutputStream outputStream;

    public LogstashEncoderV1Test() {
        super("message", "tags");
    }

    protected LogstashEncoderBase encoder() {
        return encoder;
    }

    @Override
    protected ByteArrayOutputStream outputStream() {
        return outputStream;
    }

    protected JsonNode field(JsonNode node, String name) {
        return node.get(name);
    }

    @Before
    public void before() throws Exception {
        outputStream = new ByteArrayOutputStream();
        encoder = new LogstashEncoderV1();
        encoder.init(outputStream);
    }
    

}
