package com.local.app.dto.request;

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
public final class SerdeAddCommentRequestSerializer implements Serializer<AddCommentRequest>, ObjectSerializer<AddCommentRequest> {
  private static final String KEY_0 = "author";

  private static final Argument ARGUMENT_0 = Argument.STRING.withName(SerdeAddCommentRequestSerializer.KEY_0);

  private static final String KEY_1 = "message";

  private static final Argument ARGUMENT_1 = Argument.STRING.withName(SerdeAddCommentRequestSerializer.KEY_1);

  public Serializer<AddCommentRequest> createSpecific(Serializer.EncoderContext context, Argument<? extends AddCommentRequest> type) throws
      SerdeException {
    return (Serializer<AddCommentRequest>) GeneratedSerdeFallbackUtil.withRuntimeObjectFallback(this, context, type);
  }

  public void serialize(Encoder encoder, Serializer.EncoderContext context, Argument type, AddCommentRequest value) throws IOException {
    Encoder objectEncoder = encoder.encodeObject(type);
    this.serializeInto(objectEncoder, context, type, value);
    objectEncoder.finishStructure();
  }

  public void serializeInto(Encoder encoder, Serializer.EncoderContext context, Argument type, AddCommentRequest value) throws IOException {
    encoder.encodeKey(SerdeAddCommentRequestSerializer.KEY_0);
    try {
      String value0 = value.author();
      if (value0 == null) {
        encoder.encodeNull();
      } else {
        encoder.encodeString(value0);
      }
    } catch (Throwable e0) {
      throw GeneratedSerdeExceptionUtil.withPropertyPath(e0, type, SerdeAddCommentRequestSerializer.ARGUMENT_0);
    }
    encoder.encodeKey(SerdeAddCommentRequestSerializer.KEY_1);
    try {
      String value1 = value.message();
      if (value1 == null) {
        encoder.encodeNull();
      } else {
        encoder.encodeString(value1);
      }
    } catch (Throwable e0) {
      throw GeneratedSerdeExceptionUtil.withPropertyPath(e0, type, SerdeAddCommentRequestSerializer.ARGUMENT_1);
    }
  }
}
