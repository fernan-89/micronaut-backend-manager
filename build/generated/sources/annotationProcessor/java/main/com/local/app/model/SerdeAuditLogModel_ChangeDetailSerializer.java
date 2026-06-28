package com.local.app.model;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.ObjectSerializer;
import io.micronaut.serde.Serializer;
import io.micronaut.serde.exceptions.SerdeException;
import io.micronaut.serde.util.GeneratedSerdeExceptionUtil;
import io.micronaut.serde.util.GeneratedSerdeFallbackUtil;
import java.io.IOException;
import java.lang.String;
import java.lang.Throwable;
import javax.annotation.processing.Generated;

@Prototype
@Generated("Micronaut")
public final class SerdeAuditLogModel_ChangeDetailSerializer implements Serializer<AuditLogModel.ChangeDetail>, ObjectSerializer<AuditLogModel.ChangeDetail> {
  private static final String KEY_0 = "from";

  private static final Argument ARGUMENT_0 = Argument.STRING.withName(SerdeAuditLogModel_ChangeDetailSerializer.KEY_0);

  private static final String KEY_1 = "to";

  private static final Argument ARGUMENT_1 = Argument.STRING.withName(SerdeAuditLogModel_ChangeDetailSerializer.KEY_1);

  public Serializer<AuditLogModel.ChangeDetail> createSpecific(Serializer.EncoderContext context, Argument<? extends AuditLogModel.ChangeDetail> type)
      throws SerdeException {
    return (Serializer<AuditLogModel.ChangeDetail>) GeneratedSerdeFallbackUtil.withRuntimeObjectFallback(this, context, type);
  }

  public void serialize(Encoder encoder, Serializer.EncoderContext context, Argument type, AuditLogModel.ChangeDetail value) throws IOException {
    Encoder objectEncoder = encoder.encodeObject(type);
    this.serializeInto(objectEncoder, context, type, value);
    objectEncoder.finishStructure();
  }

  public void serializeInto(Encoder encoder, Serializer.EncoderContext context, Argument type, AuditLogModel.ChangeDetail value) throws IOException {
    encoder.encodeKey(SerdeAuditLogModel_ChangeDetailSerializer.KEY_0);
    try {
      String value0 = value.from();
      if (value0 == null) {
        encoder.encodeNull();
      } else {
        encoder.encodeString(value0);
      }
    } catch (Throwable e0) {
      throw GeneratedSerdeExceptionUtil.withPropertyPath(e0, type, SerdeAuditLogModel_ChangeDetailSerializer.ARGUMENT_0);
    }
    encoder.encodeKey(SerdeAuditLogModel_ChangeDetailSerializer.KEY_1);
    try {
      String value1 = value.to();
      if (value1 == null) {
        encoder.encodeNull();
      } else {
        encoder.encodeString(value1);
      }
    } catch (Throwable e0) {
      throw GeneratedSerdeExceptionUtil.withPropertyPath(e0, type, SerdeAuditLogModel_ChangeDetailSerializer.ARGUMENT_1);
    }
  }
}
