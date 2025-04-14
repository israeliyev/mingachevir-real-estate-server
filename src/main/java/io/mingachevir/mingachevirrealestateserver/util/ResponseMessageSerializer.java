package io.mingachevir.mingachevirrealestateserver.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ResponseMessageSerializer extends StdSerializer<ResponseMessage> {
    public ResponseMessageSerializer() {
        super(ResponseMessage.class);
    }

    public void serialize(ResponseMessage responseMessage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("code");
        jsonGenerator.writeString(responseMessage.getCode());
        jsonGenerator.writeFieldName("defaultMessage");
        jsonGenerator.writeString(responseMessage.getDefaultMessage());
        jsonGenerator.writeEndObject();
    }
}
