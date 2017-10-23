package edu.akarimin.esutils.commons;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author akarimin
 */
public class JacksonMapperConfig {

  private JacksonMapperConfig() {

  }

  public static final ObjectMapper objectMapper = new ObjectMapper();

  static {

    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setDateFormat(dateFormat);

  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
