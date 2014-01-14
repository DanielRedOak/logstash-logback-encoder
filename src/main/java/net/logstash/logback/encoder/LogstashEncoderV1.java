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

import static org.apache.commons.io.IOUtils.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Marker;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.encoder.EncoderBase;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LogstashEncoderV1 extends LogstashEncoderBase {
    
    @Override
    public void doEncode(ILoggingEvent event) throws IOException {
        
        ObjectNode eventNode = MAPPER.createObjectNode();
        eventNode.put("@timestamp", ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(event.getTimeStamp()));
        eventNode.put("message", event.getFormattedMessage());
        eventNode.put("tags", createTags(event));
        eventNode.put("thread_name", event.getThreadName());
        eventNode.put("logger_name", event.getLoggerName());
        eventNode.put("level", event.getLevel().toString());
        eventNode.put("level_value", event.getLevel().toInt());
        if (isIncludeCallerInfo()) {
            StackTraceElement callerData = extractCallerData(event);
            eventNode.put("caller_class_name", callerData.getClassName());
            eventNode.put("caller_method_name", callerData.getMethodName());
            eventNode.put("caller_file_name", callerData.getFileName());
            eventNode.put("caller_line_number", callerData.getLineNumber());
        }
        
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            eventNode.put("stack_trace", ThrowableProxyUtil.asString(throwableProxy));
        }
        write(MAPPER.writeValueAsBytes(eventNode), outputStream);
        write(CoreConstants.LINE_SEPARATOR, outputStream);
        
        if (isImmediateFlush()) {
            outputStream.flush();
        }
        
    }

}
