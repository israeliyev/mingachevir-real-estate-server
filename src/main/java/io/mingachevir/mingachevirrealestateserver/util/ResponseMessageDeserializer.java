package io.mingachevir.mingachevirrealestateserver.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ResponseMessageDeserializer extends StdDeserializer<ResponseMessage> {
    public ResponseMessageDeserializer() {
        super(ResponseMessage.class);
    }

    @Override
    public ResponseMessage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode jsonNode = (JsonNode)jsonParser.getCodec().readTree(jsonParser);
        String code = jsonNode.get("code").asText();
        String message = jsonNode.get("defaultMessage").asText();
        return new ResponseMessage(code, message);
    }

}
