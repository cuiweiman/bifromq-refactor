package com.zachary.bifromq.sessiondict.server;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.baserpc.CertInfo;
import com.zachary.bifromq.baserpc.IRPCServer;
import com.zachary.bifromq.sessiondict.ISessionDictBuilder;
import com.zachary.bifromq.sessiondict.RPCBluePrint;
import com.google.common.base.Preconditions;
import io.netty.channel.EventLoopGroup;
import lombok.NonNull;

import java.io.File;
import java.util.concurrent.Executor;

public abstract class SessionDictionaryServerBuilder<T extends SessionDictionaryServerBuilder>
    implements ISessionDictBuilder {
    protected Executor executor;

    public T executor(Executor executor) {
        this.executor = executor;
        return (T) this;
    }

    public abstract ISessionDictionaryServer build();

    public static final class InProcServerBuilder extends SessionDictionaryServerBuilder<InProcServerBuilder> {
        @Override
        public ISessionDictionaryServer build() {
            return new SessionDictServer(SERVICE_NAME) {
                @Override
                protected IRPCServer buildRPCServer(SessionDictionaryService distService) {
                    return IRPCServer.inProcServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(distService)
                        .build();
                }
            };
        }
    }

    abstract static class InterProcBuilder<T extends InterProcBuilder> extends SessionDictionaryServerBuilder<T> {
        protected String id;
        protected String host;
        protected Integer port;
        protected ICRDTService crdtService;
        protected EventLoopGroup bossEventLoopGroup;
        protected EventLoopGroup workerEventLoopGroup;

        public T id(@NonNull String id) {
            this.id = id;
            return (T) this;
        }

        public T host(@NonNull String host) {
            Preconditions.checkArgument(!"0.0.0.0".equals(host), "Invalid host ip");
            this.host = host;
            return (T) this;
        }

        public T port(@NonNull Integer port) {
            this.port = port;
            return (T) this;
        }

        public T crdtService(@NonNull ICRDTService crdtService) {
            this.crdtService = crdtService;
            return (T) this;
        }

        public T bossEventLoopGroup(EventLoopGroup bossEventLoopGroup) {
            this.bossEventLoopGroup = bossEventLoopGroup;
            return (T) this;
        }

        public T workerEventLoopGroup(EventLoopGroup workerEventLoopGroup) {
            this.workerEventLoopGroup = workerEventLoopGroup;
            return (T) this;
        }
    }

    public static final class NonSSLServerBuilder extends InterProcBuilder<NonSSLServerBuilder> {
        @Override
        public ISessionDictionaryServer build() {
            return new SessionDictServer(SERVICE_NAME) {
                @Override
                protected IRPCServer buildRPCServer(SessionDictionaryService distService) {
                    return IRPCServer.nonSSLServerBuilder()
                        .serviceUniqueName(SERVICE_NAME)
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(distService)
                        .id(id)
                        .host(host)
                        .port(port)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .crdtService(crdtService)
                        .build();
                }
            };
        }
    }

    public static final class SSLServerBuilder extends InterProcBuilder<SSLServerBuilder> {
        private @NonNull File serviceIdentityCertFile;
        private @NonNull File privateKeyFile;
        private @NonNull File trustCertsFile;

        public SSLServerBuilder serviceIdentityCertFile(@NonNull File serviceIdentityCertFile) {
            this.serviceIdentityCertFile = serviceIdentityCertFile;
            CertInfo certInfo = CertInfo.parse(serviceIdentityCertFile);
            Preconditions.checkArgument(certInfo.serverAuth, "Not server auth cert");
            return this;
        }

        public SSLServerBuilder privateKeyFile(@NonNull File privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public SSLServerBuilder trustCertsFile(@NonNull File trustCertsFile) {
            this.trustCertsFile = trustCertsFile;
            return this;
        }

        @Override
        public ISessionDictionaryServer build() {
            return new SessionDictServer(SERVICE_NAME) {
                @Override
                protected IRPCServer buildRPCServer(SessionDictionaryService distService) {
                    return IRPCServer.sslServerBuilder()
                        .executor(executor)
                        .bluePrint(RPCBluePrint.INSTANCE)
                        .bindService(distService)
                        .id(id)
                        .host(host)
                        .port(port)
                        .serviceUniqueName(SERVICE_NAME)
                        .bossEventLoopGroup(bossEventLoopGroup)
                        .workerEventLoopGroup(workerEventLoopGroup)
                        .crdtService(crdtService)
                        .serviceIdentityCertFile(serviceIdentityCertFile)
                        .privateKeyFile(privateKeyFile)
                        .trustCertsFile(trustCertsFile)
                        .build();
                }
            };
        }
    }
}
