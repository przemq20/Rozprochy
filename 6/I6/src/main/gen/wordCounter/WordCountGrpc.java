package wordCounter;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.37.0)",
    comments = "Source: wordCounter.proto")
public final class WordCountGrpc {

  private WordCountGrpc() {}

  public static final String SERVICE_NAME = "counter.WordCount";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<wordCounter.Words,
      wordCounter.Result> getCountWordMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CountWord",
      requestType = wordCounter.Words.class,
      responseType = wordCounter.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<wordCounter.Words,
      wordCounter.Result> getCountWordMethod() {
    io.grpc.MethodDescriptor<wordCounter.Words, wordCounter.Result> getCountWordMethod;
    if ((getCountWordMethod = WordCountGrpc.getCountWordMethod) == null) {
      synchronized (WordCountGrpc.class) {
        if ((getCountWordMethod = WordCountGrpc.getCountWordMethod) == null) {
          WordCountGrpc.getCountWordMethod = getCountWordMethod =
              io.grpc.MethodDescriptor.<wordCounter.Words, wordCounter.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CountWord"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  wordCounter.Words.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  wordCounter.Result.getDefaultInstance()))
              .setSchemaDescriptor(new WordCountMethodDescriptorSupplier("CountWord"))
              .build();
        }
      }
    }
    return getCountWordMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WordCountStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WordCountStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WordCountStub>() {
        @java.lang.Override
        public WordCountStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WordCountStub(channel, callOptions);
        }
      };
    return WordCountStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WordCountBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WordCountBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WordCountBlockingStub>() {
        @java.lang.Override
        public WordCountBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WordCountBlockingStub(channel, callOptions);
        }
      };
    return WordCountBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WordCountFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WordCountFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WordCountFutureStub>() {
        @java.lang.Override
        public WordCountFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WordCountFutureStub(channel, callOptions);
        }
      };
    return WordCountFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class WordCountImplBase implements io.grpc.BindableService {

    /**
     */
    public void countWord(wordCounter.Words request,
        io.grpc.stub.StreamObserver<wordCounter.Result> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCountWordMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCountWordMethod(),
            io.grpc.stub.ServerCalls.asyncServerStreamingCall(
              new MethodHandlers<
                wordCounter.Words,
                wordCounter.Result>(
                  this, METHODID_COUNT_WORD)))
          .build();
    }
  }

  /**
   */
  public static final class WordCountStub extends io.grpc.stub.AbstractAsyncStub<WordCountStub> {
    private WordCountStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WordCountStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WordCountStub(channel, callOptions);
    }

    /**
     */
    public void countWord(wordCounter.Words request,
        io.grpc.stub.StreamObserver<wordCounter.Result> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getCountWordMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class WordCountBlockingStub extends io.grpc.stub.AbstractBlockingStub<WordCountBlockingStub> {
    private WordCountBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WordCountBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WordCountBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<wordCounter.Result> countWord(
        wordCounter.Words request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getCountWordMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class WordCountFutureStub extends io.grpc.stub.AbstractFutureStub<WordCountFutureStub> {
    private WordCountFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WordCountFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WordCountFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_COUNT_WORD = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final WordCountImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(WordCountImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COUNT_WORD:
          serviceImpl.countWord((wordCounter.Words) request,
              (io.grpc.stub.StreamObserver<wordCounter.Result>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class WordCountBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WordCountBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return wordCounter.WordCounter.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WordCount");
    }
  }

  private static final class WordCountFileDescriptorSupplier
      extends WordCountBaseDescriptorSupplier {
    WordCountFileDescriptorSupplier() {}
  }

  private static final class WordCountMethodDescriptorSupplier
      extends WordCountBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    WordCountMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WordCountGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WordCountFileDescriptorSupplier())
              .addMethod(getCountWordMethod())
              .build();
        }
      }
    }
    return result;
  }
}
