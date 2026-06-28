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
public final class SerdeInfrastructureProviderDeserializer implements FormattedDeserializer<InfrastructureProvider> {
  private static final Argument ARGUMENT_STRING = Argument.STRING;

  private final Deserializer stringDeserializer;

  public SerdeInfrastructureProviderDeserializer(@Parameter Deserializer.DecoderContext context,
      @Parameter Argument<? super InfrastructureProvider> type) throws SerdeException {
    this.stringDeserializer = context.findDeserializer(SerdeInfrastructureProviderDeserializer.ARGUMENT_STRING).createSpecific(context, SerdeInfrastructureProviderDeserializer.ARGUMENT_STRING);
  }

  public Deserializer<InfrastructureProvider> createSpecific(Deserializer.DecoderContext context, Argument type) throws SerdeException {
    return (Deserializer<InfrastructureProvider>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Deserializer<InfrastructureProvider> createSpecific(Deserializer.DecoderContext context, Argument<? super InfrastructureProvider> type,
      FormatConfiguration format) throws SerdeException {
    return (Deserializer<InfrastructureProvider>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumDeserializer(context, type, format);
  }

  public InfrastructureProvider deserialize(Decoder decoder, Deserializer.DecoderContext context, Argument type) throws IOException {
    Deserializer stringDeserializer = this.stringDeserializer;
    String decodedValue = (String) stringDeserializer.deserialize(decoder, context, SerdeInfrastructureProviderDeserializer.ARGUMENT_STRING);
    try {
      return (InfrastructureProvider) GeneratedSerdeEnumUtil.enumValueOf(com.local.app.model.enums.InfrastructureProvider.class, decodedValue, context);
    } catch (IllegalArgumentException e0) {
      return (InfrastructureProvider) GeneratedSerdeExceptionUtil.handleUnknownEnumValue(context, type, decodedValue);
    }
  }
}
