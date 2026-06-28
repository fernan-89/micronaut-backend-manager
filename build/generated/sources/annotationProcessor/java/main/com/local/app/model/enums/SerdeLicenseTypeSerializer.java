package com.local.app.model.enums;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.FormatConfiguration;
import io.micronaut.serde.FormattedSerializer;
import io.micronaut.serde.ObjectSerializer;
import io.micronaut.serde.Serializer;
import io.micronaut.serde.exceptions.SerdeException;
import io.micronaut.serde.util.GeneratedSerdeFallbackUtil;
import java.io.IOException;
import java.lang.String;
import javax.annotation.processing.Generated;

@Prototype
@Generated("Micronaut")
public final class SerdeLicenseTypeSerializer implements FormattedSerializer<LicenseType>, ObjectSerializer<LicenseType> {
  public Serializer<LicenseType> createSpecific(Serializer.EncoderContext context, Argument<? extends LicenseType> type) throws SerdeException {
    return (Serializer<LicenseType>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Serializer<LicenseType> createSpecific(Serializer.EncoderContext context, Argument<? extends LicenseType> type, FormatConfiguration format)
      throws SerdeException {
    return (Serializer<LicenseType>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumSerializer(context, type, format);
  }

  public void serialize(Encoder encoder, Serializer.EncoderContext context, Argument type, LicenseType value) throws IOException {
    String enumName = value.name();
    encoder.encodeString(enumName);
  }

  public void serializeInto(Encoder encoder, Serializer.EncoderContext context, Argument type, LicenseType value) throws IOException {
    String enumName = value.name();
    encoder.encodeString(enumName);
  }
}
