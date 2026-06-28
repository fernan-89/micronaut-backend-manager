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
public final class SerdeLicenseTypeDeserializer implements FormattedDeserializer<LicenseType> {
  private static final Argument ARGUMENT_STRING = Argument.STRING;

  private final Deserializer stringDeserializer;

  public SerdeLicenseTypeDeserializer(@Parameter Deserializer.DecoderContext context, @Parameter Argument<? super LicenseType> type) throws
      SerdeException {
    this.stringDeserializer = context.findDeserializer(SerdeLicenseTypeDeserializer.ARGUMENT_STRING).createSpecific(context, SerdeLicenseTypeDeserializer.ARGUMENT_STRING);
  }

  public Deserializer<LicenseType> createSpecific(Deserializer.DecoderContext context, Argument type) throws SerdeException {
    return (Deserializer<LicenseType>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Deserializer<LicenseType> createSpecific(Deserializer.DecoderContext context, Argument<? super LicenseType> type, FormatConfiguration format)
      throws SerdeException {
    return (Deserializer<LicenseType>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumDeserializer(context, type, format);
  }

  public LicenseType deserialize(Decoder decoder, Deserializer.DecoderContext context, Argument type) throws IOException {
    Deserializer stringDeserializer = this.stringDeserializer;
    String decodedValue = (String) stringDeserializer.deserialize(decoder, context, SerdeLicenseTypeDeserializer.ARGUMENT_STRING);
    try {
      return (LicenseType) GeneratedSerdeEnumUtil.enumValueOf(com.local.app.model.enums.LicenseType.class, decodedValue, context);
    } catch (IllegalArgumentException e0) {
      return (LicenseType) GeneratedSerdeExceptionUtil.handleUnknownEnumValue(context, type, decodedValue);
    }
  }
}
