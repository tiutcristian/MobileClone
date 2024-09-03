package ro.msg.mobile_clone.model.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LowercaseConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute.toLowerCase();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
