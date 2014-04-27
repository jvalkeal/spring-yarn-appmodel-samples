package hello.appmaster.support.jackson;

import java.io.IOException;

import org.apache.hadoop.yarn.api.records.ContainerId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public final class ContainerIdSerializer extends StdScalarSerializer<ContainerId> {

	public ContainerIdSerializer() {
		super(ContainerId.class);
	}

	@Override
	public void serialize(ContainerId value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(value.toString());
	}

}