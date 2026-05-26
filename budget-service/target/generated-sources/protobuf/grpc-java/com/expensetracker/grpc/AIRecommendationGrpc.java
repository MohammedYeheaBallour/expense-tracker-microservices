package com.expensetracker.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: ai_recommendation.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AIRecommendationGrpc {

  private AIRecommendationGrpc() {}

  public static final String SERVICE_NAME = "com.expensetracker.AIRecommendation";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.expensetracker.grpc.RecommendationRequest,
      com.expensetracker.grpc.RecommendationResponse> getGetRecommendationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRecommendation",
      requestType = com.expensetracker.grpc.RecommendationRequest.class,
      responseType = com.expensetracker.grpc.RecommendationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.expensetracker.grpc.RecommendationRequest,
      com.expensetracker.grpc.RecommendationResponse> getGetRecommendationMethod() {
    io.grpc.MethodDescriptor<com.expensetracker.grpc.RecommendationRequest, com.expensetracker.grpc.RecommendationResponse> getGetRecommendationMethod;
    if ((getGetRecommendationMethod = AIRecommendationGrpc.getGetRecommendationMethod) == null) {
      synchronized (AIRecommendationGrpc.class) {
        if ((getGetRecommendationMethod = AIRecommendationGrpc.getGetRecommendationMethod) == null) {
          AIRecommendationGrpc.getGetRecommendationMethod = getGetRecommendationMethod =
              io.grpc.MethodDescriptor.<com.expensetracker.grpc.RecommendationRequest, com.expensetracker.grpc.RecommendationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRecommendation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.expensetracker.grpc.RecommendationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.expensetracker.grpc.RecommendationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AIRecommendationMethodDescriptorSupplier("GetRecommendation"))
              .build();
        }
      }
    }
    return getGetRecommendationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AIRecommendationStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AIRecommendationStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AIRecommendationStub>() {
        @java.lang.Override
        public AIRecommendationStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AIRecommendationStub(channel, callOptions);
        }
      };
    return AIRecommendationStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AIRecommendationBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AIRecommendationBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AIRecommendationBlockingStub>() {
        @java.lang.Override
        public AIRecommendationBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AIRecommendationBlockingStub(channel, callOptions);
        }
      };
    return AIRecommendationBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AIRecommendationFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AIRecommendationFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AIRecommendationFutureStub>() {
        @java.lang.Override
        public AIRecommendationFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AIRecommendationFutureStub(channel, callOptions);
        }
      };
    return AIRecommendationFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class AIRecommendationImplBase implements io.grpc.BindableService {

    /**
     */
    public void getRecommendation(com.expensetracker.grpc.RecommendationRequest request,
        io.grpc.stub.StreamObserver<com.expensetracker.grpc.RecommendationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRecommendationMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetRecommendationMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.expensetracker.grpc.RecommendationRequest,
                com.expensetracker.grpc.RecommendationResponse>(
                  this, METHODID_GET_RECOMMENDATION)))
          .build();
    }
  }

  /**
   */
  public static final class AIRecommendationStub extends io.grpc.stub.AbstractAsyncStub<AIRecommendationStub> {
    private AIRecommendationStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AIRecommendationStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AIRecommendationStub(channel, callOptions);
    }

    /**
     */
    public void getRecommendation(com.expensetracker.grpc.RecommendationRequest request,
        io.grpc.stub.StreamObserver<com.expensetracker.grpc.RecommendationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRecommendationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AIRecommendationBlockingStub extends io.grpc.stub.AbstractBlockingStub<AIRecommendationBlockingStub> {
    private AIRecommendationBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AIRecommendationBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AIRecommendationBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.expensetracker.grpc.RecommendationResponse getRecommendation(com.expensetracker.grpc.RecommendationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRecommendationMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AIRecommendationFutureStub extends io.grpc.stub.AbstractFutureStub<AIRecommendationFutureStub> {
    private AIRecommendationFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AIRecommendationFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AIRecommendationFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.expensetracker.grpc.RecommendationResponse> getRecommendation(
        com.expensetracker.grpc.RecommendationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRecommendationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_RECOMMENDATION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AIRecommendationImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AIRecommendationImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_RECOMMENDATION:
          serviceImpl.getRecommendation((com.expensetracker.grpc.RecommendationRequest) request,
              (io.grpc.stub.StreamObserver<com.expensetracker.grpc.RecommendationResponse>) responseObserver);
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

  private static abstract class AIRecommendationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AIRecommendationBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.expensetracker.grpc.AIRecommendationProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AIRecommendation");
    }
  }

  private static final class AIRecommendationFileDescriptorSupplier
      extends AIRecommendationBaseDescriptorSupplier {
    AIRecommendationFileDescriptorSupplier() {}
  }

  private static final class AIRecommendationMethodDescriptorSupplier
      extends AIRecommendationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AIRecommendationMethodDescriptorSupplier(String methodName) {
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
      synchronized (AIRecommendationGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AIRecommendationFileDescriptorSupplier())
              .addMethod(getGetRecommendationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
