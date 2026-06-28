package com.local.app.dto.request;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Deserializer;
import io.micronaut.serde.exceptions.SerdeException;
import io.micronaut.serde.util.GeneratedSerdeExceptionUtil;
import io.micronaut.serde.util.GeneratedSerdeFallbackUtil;
import java.io.IOException;
import java.lang.String;
import javax.annotation.processing.Generated;

@Prototype
@Generated("Micronaut")
public final class SerdeAddCommentRequestDeserializer implements Deserializer<AddCommentRequest> {
  private static final String KEY_0 = "author";

  private static final Argument ARGUMENT_0 = Argument.STRING.withName(SerdeAddCommentRequestDeserializer.KEY_0);

  private static final String KEY_1 = "message";

  private static final Argument ARGUMENT_1 = Argument.STRING.withName(SerdeAddCommentRequestDeserializer.KEY_1);

  private final boolean ignoreUnknown;

  public SerdeAddCommentRequestDeserializer(@Parameter Deserializer.DecoderContext context, @Parameter Argument type) throws SerdeException {
    this.ignoreUnknown = GeneratedSerdeExceptionUtil.ignoreUnknown(context);
  }

  public Deserializer<AddCommentRequest> createSpecific(Deserializer.DecoderContext context, Argument type) throws SerdeException {
    return (Deserializer<AddCommentRequest>) GeneratedSerdeFallbackUtil.withRuntimeObjectFallback(this, context, type);
  }

  public AddCommentRequest deserialize(Decoder decoder, Deserializer.DecoderContext context, Argument type) throws IOException {
    Decoder objectDecoder = decoder.decodeObject(type);
    boolean seenProperty0 = false;
    boolean seenProperty1 = false;
    String propertyValue0 = null;
    String propertyValue1 = null;
    while (true) {
      String key = objectDecoder.decodeKey();
      if (key == null) {
        objectDecoder.finishStructure();
        return new com.local.app.dto.request.AddCommentRequest(propertyValue0, propertyValue1);
      }
      GeneratedSerdeExceptionUtil.PropertyDispatchResult propertyDispatchResult = switch (key) {
        case "author" -> {
          io.micronaut.serde.util.GeneratedSerdeExceptionUtil.PropertyDispatchResult dispatchResult = io.micronaut.serde.util.GeneratedSerdeExceptionUtil.PropertyDispatchResult.HANDLED;
          if (seenProperty0) {
            dispatchResult = io.micronaut.serde.util.GeneratedSerdeExceptionUtil.PropertyDispatchResult.DUPLICATE;
          } else {
            seenProperty0 = true;
            try {
              propertyValue0 = objectDecoder.decodeStringNullable();
            } catch (java.lang.Throwable e0) {
              throw io.micronaut.serde.util.GeneratedSerdeExceptionUtil.withPropertyPath(e0, type, com.local.app.dto.request.SerdeAddCommentRequestDeserializer.ARGUMENT_0);
            }
          }
          yield dispatchResult;
        }
        case "message" -> {
          io.micronaut.serde.util.GeneratedSerdeExceptionUtil.PropertyDispatchResult dispatchResult = io.micronaut.serde.util.GeneratedSerdeExceptionUtil.PropertyDispatchResult.HANDLED;
          if (seenProperty1) {
            dispatchResult = io.micronaut.serde.util.GeneratedSerdeExceptionUtil.PropertyDispatchResult.DUPLICATE;
          } else {
            seenProperty1 = true;
            try {
              propertyValue1 = objectDecoder.decodeStringNullable();
            } catch (java.lang.Throwable e0) {
              throw io.micronaut.serde.util.GeneratedSerdeExceptionUtil.withPropertyPath(e0, type, com.local.app.dto.request.SerdeAddCommentRequestDeserializer.ARGUMENT_1);
            }
          }
          yield dispatchResult;
        }
        default -> GeneratedSerdeExceptionUtil.PropertyDispatchResult.UNKNOWN;
      };
      switch (propertyDispatchResult) {
        case GeneratedSerdeExceptionUtil.PropertyDispatchResult.NULL -> {
          throw GeneratedSerdeExceptionUtil.withPropertyPath(GeneratedSerdeExceptionUtil.nullValue(type, Argument.OBJECT_ARGUMENT.withName(key)), type, Argument.OBJECT_ARGUMENT.withName(key));
        }
        case GeneratedSerdeExceptionUtil.PropertyDispatchResult.UNKNOWN -> {
          if (this.ignoreUnknown) {
            objectDecoder.skipValue();
          } else {
            throw GeneratedSerdeExceptionUtil.unknownProperty(type, Argument.OBJECT_ARGUMENT.withName(key));
          }
        }
        case GeneratedSerdeExceptionUtil.PropertyDispatchResult.DUPLICATE -> {
          throw GeneratedSerdeExceptionUtil.duplicateProperty(type, Argument.OBJECT_ARGUMENT.withName(key));
        }
        case GeneratedSerdeExceptionUtil.PropertyDispatchResult.HANDLED -> {
        }
      }
    }
  }
}
