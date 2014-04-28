/*
 * Copyright 2014 the original author or authors.
 *
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
package hello.appmaster.support.jackson;

import java.io.IOException;

import org.apache.hadoop.yarn.api.records.Container;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

/**
 * Jackson {@link JsonSerializer} handling custom serialization for {@link Container}.
 *
 * @author Janne Valkealahti
 *
 */
public final class ContainerSerializer extends StdScalarSerializer<Container> {

	/**
	 * Instantiates a new container serializer.
	 */
	public ContainerSerializer() {
		super(Container.class);
	}

	@Override
	public void serialize(Container value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeObjectField("id", value.getId());
		jgen.writeStringField("node", value.getNodeId().getHost());
		jgen.writeEndObject();
	}

}