package com.local.app.model.enums;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Deserializer;
import io.micronaut.serde.FormatConfiguration;
import io.micronaut.serde.FormattedDeserializer;
import io.micronaut.serde.exceptions.SerdeException;
import io.micronaut.serde.util.GeneratedSerdeEnumUtil;
import io.micronaut.serde.util.GeneratedSerdeExceptionUtil;
import io.micronaut.serde.util.GeneratedSerdeFallbackUtil;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.String;
import javax.annotation.processing.Generated;

@Prototype
@Generated("Micronaut")
public final class SerdeResourceTypeDeserializer implements FormattedDeserializer<ResourceType> {
  private static final Argument ARGUMENT_STRING = Argument.STRING;

  private final Deserializer stringDeserializer;

  public SerdeResourceTypeDeserializer(@Parameter Deserializer.DecoderContext context, @Parameter Argument<? super ResourceType> type) throws
      SerdeException {
    this.stringDeserializer = context.findDeserializer(SerdeResourceTypeDeserializer.ARGUMENT_STRING).createSpecific(context, SerdeResourceTypeDeserializer.ARGUMENT_STRING);
  }

  public Deserializer<ResourceType> createSpecific(Deserializer.DecoderContext context, Argument type) throws SerdeException {
    return (Deserializer<ResourceType>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Deserializer<ResourceType> createSpecific(Deserializer.DecoderContext context, Argument<? super ResourceType> type,
      FormatConfiguration format) throws SerdeException {
    return (Deserializer<ResourceType>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumDeserializer(context, type, format);
  }

  public ResourceType deserialize(Decoder decoder, Deserializer.DecoderContext context, Argument type) throws IOException {
    Deserializer stringDeserializer = this.stringDeserializer;
    String decodedValue = (String) stringDeserializer.deserialize(decoder, context, SerdeResourceTypeDeserializer.ARGUMENT_STRING);
    try {
      return (ResourceType) GeneratedSerdeEnumUtil.enumValueOf(com.local.app.model.enums.ResourceType.class, decodedValue, context);
    } catch (IllegalArgumentException e0) {
      return (ResourceType) GeneratedSerdeExceptionUtil.handleUnknownEnumValue(context, type, decodedValue);
    }
  }
}
