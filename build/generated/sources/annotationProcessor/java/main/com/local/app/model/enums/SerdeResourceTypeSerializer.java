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
public final class SerdeResourceTypeSerializer implements FormattedSerializer<ResourceType>, ObjectSerializer<ResourceType> {
  public Serializer<ResourceType> createSpecific(Serializer.EncoderContext context, Argument<? extends ResourceType> type) throws SerdeException {
    return (Serializer<ResourceType>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Serializer<ResourceType> createSpecific(Serializer.EncoderContext context, Argument<? extends ResourceType> type, FormatConfiguration format)
      throws SerdeException {
    return (Serializer<ResourceType>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumSerializer(context, type, format);
  }

  public void serialize(Encoder encoder, Serializer.EncoderContext context, Argument type, ResourceType value) throws IOException {
    String enumName = value.name();
    encoder.encodeString(enumName);
  }

  public void serializeInto(Encoder encoder, Serializer.EncoderContext context, Argument type, ResourceType value) throws IOException {
    String enumName = value.name();
    encoder.encodeString(enumName);
  }
}
