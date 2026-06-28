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
public final class SerdeResourceStatusDeserializer implements FormattedDeserializer<ResourceStatus> {
  private static final Argument ARGUMENT_STRING = Argument.STRING;

  private final Deserializer stringDeserializer;

  public SerdeResourceStatusDeserializer(@Parameter Deserializer.DecoderContext context, @Parameter Argument<? super ResourceStatus> type) throws
      SerdeException {
    this.stringDeserializer = context.findDeserializer(SerdeResourceStatusDeserializer.ARGUMENT_STRING).createSpecific(context, SerdeResourceStatusDeserializer.ARGUMENT_STRING);
  }

  public Deserializer<ResourceStatus> createSpecific(Deserializer.DecoderContext context, Argument type) throws SerdeException {
    return (Deserializer<ResourceStatus>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Deserializer<ResourceStatus> createSpecific(Deserializer.DecoderContext context, Argument<? super ResourceStatus> type,
      FormatConfiguration format) throws SerdeException {
    return (Deserializer<ResourceStatus>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumDeserializer(context, type, format);
  }

  public ResourceStatus deserialize(Decoder decoder, Deserializer.DecoderContext context, Argument type) throws IOException {
    Deserializer stringDeserializer = this.stringDeserializer;
    String decodedValue = (String) stringDeserializer.deserialize(decoder, context, SerdeResourceStatusDeserializer.ARGUMENT_STRING);
    try {
      return (ResourceStatus) GeneratedSerdeEnumUtil.enumValueOf(com.local.app.model.enums.ResourceStatus.class, decodedValue, context);
    } catch (IllegalArgumentException e0) {
      return (ResourceStatus) GeneratedSerdeExceptionUtil.handleUnknownEnumValue(context, type, decodedValue);
    }
  }
}
