package com.famigo.website.model;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class MessageJSONSerializer {

    public static class Serialize extends JsonSerializer<Message> {

        @Override
        public void serialize(Message value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            // TODO Auto-generated method stub
            jgen.writeStartObject();
            jgen.writeStringField("id", value.getId());
            jgen.writeStringField("sender", value.getSender());
            jgen.writeStringField("timestamp", value.getTimestamp().toString());
            jgen.writeStringField("content", value.getContent());
            jgen.writeStringField("edited", value.getEdited().toString());
            jgen.writeStringField("conversation", value.getConversation());
            jgen.writeEndObject();
        }
    }

}
