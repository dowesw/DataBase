package com.lymytz.entitymanager.component;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DoubleAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value);
    }


    @Override
    public Double read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String stringValue = in.nextString().replace("'","");
        try {
            Double value = Double.valueOf(stringValue);
            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
