package hello.appmaster.support.jackson;

import java.io.IOException;

import org.apache.hadoop.yarn.api.records.Container;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public final class ContainerSerializer extends StdScalarSerializer<Container> {

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