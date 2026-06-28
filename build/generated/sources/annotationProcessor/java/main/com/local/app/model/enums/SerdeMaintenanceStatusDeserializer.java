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
public final class SerdeMaintenanceStatusDeserializer implements FormattedDeserializer<MaintenanceStatus> {
  private static final Argument ARGUMENT_STRING = Argument.STRING;

  private final Deserializer stringDeserializer;

  public SerdeMaintenanceStatusDeserializer(@Parameter Deserializer.DecoderContext context, @Parameter Argument<? super MaintenanceStatus> type)
      throws SerdeException {
    this.stringDeserializer = context.findDeserializer(SerdeMaintenanceStatusDeserializer.ARGUMENT_STRING).createSpecific(context, SerdeMaintenanceStatusDeserializer.ARGUMENT_STRING);
  }

  public Deserializer<MaintenanceStatus> createSpecific(Deserializer.DecoderContext context, Argument type) throws SerdeException {
    return (Deserializer<MaintenanceStatus>) GeneratedSerdeFallbackUtil.withRuntimeEnumFallback(this, context, type);
  }

  public Deserializer<MaintenanceStatus> createSpecific(Deserializer.DecoderContext context, Argument<? super MaintenanceStatus> type,
      FormatConfiguration format) throws SerdeException {
    return (Deserializer<MaintenanceStatus>) GeneratedSerdeFallbackUtil.runtimeFormattedEnumDeserializer(context, type, format);
  }

  public MaintenanceStatus deserialize(Decoder decoder, Deserializer.DecoderContext context, Argument type) throws IOException {
    Deserializer stringDeserializer = this.stringDeserializer;
    String decodedValue = (String) stringDeserializer.deserialize(decoder, context, SerdeMaintenanceStatusDeserializer.ARGUMENT_STRING);
    try {
      return (MaintenanceStatus) GeneratedSerdeEnumUtil.enumValueOf(com.local.app.model.enums.MaintenanceStatus.class, decodedValue, context);
    } catch (IllegalArgumentException e0) {
      return (MaintenanceStatus) GeneratedSerdeExceptionUtil.handleUnknownEnumValue(context, type, decodedValue);
    }
  }
}
