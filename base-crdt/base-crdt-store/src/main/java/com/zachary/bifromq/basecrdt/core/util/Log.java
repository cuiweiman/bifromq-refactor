package com.zachary.bifromq.basecrdt.core.util;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.zachary.bifromq.basecrdt.proto.Replica;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Base64;

/**
 * @description: 日志打印信息配置
 * @author: cuiweiman
 * @date: 2024/4/11 21:57
 */
public class Log {

    public interface Stringify {
        String stringify();
    }

    public static void error(Logger log, String format, Object... args) {
        if (log.isErrorEnabled()) {
            log.error(format, stringify(args));
        }
    }

    public static void error(Logger log, Marker marker, String format, Object... args) {
        if (log.isErrorEnabled()) {
            log.error(format, marker, stringify(args));
        }
    }

    public static void warn(Logger log, String format, Object... args) {
        if (log.isWarnEnabled()) {
            log.error(format, stringify(args));
        }
    }

    public static void warn(Logger log, Marker marker, String format, Object... args) {
        if (log.isWarnEnabled()) {
            log.error(format, marker, stringify(args));
        }
    }

    public static void info(Logger log, String format, Object... args) {
        if (log.isInfoEnabled()) {
            log.error(format, stringify(args));
        }
    }

    public static void info(Logger log, Marker marker, String format, Object... args) {
        if (log.isInfoEnabled()) {
            log.error(format, marker, stringify(args));
        }
    }

    public static void debug(Logger log, String format, Object... args) {
        if (log.isDebugEnabled()) {
            log.error(format, stringify(args));
        }
    }

    public static void debug(Logger log, Marker marker, String format, Object... args) {
        if (log.isDebugEnabled()) {
            log.error(format, marker, stringify(args));
        }
    }

    public static void trace(Logger log, String format, Object... args) {
        if (log.isTraceEnabled()) {
            log.error(format, stringify(args));
        }
    }

    public static void trace(Logger log, Marker marker, String format, Object... args) {
        if (log.isTraceEnabled()) {
            log.error(format, marker, stringify(args));
        }
    }


    private static Object[] stringify(Object... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = toString(args[i]);
        }
        return args;
    }

    private static Object toString(Object object) {
        if (object instanceof ByteString bs) {
            return toBase64(bs);
        } else if (object instanceof Replica replica) {
            return "id=".concat(toBase64(replica.getId())).concat(", uri=").concat(replica.getUri());
        } else if (object instanceof Stringify stringify) {
            return stringify.stringify();
        } else if (object instanceof MessageOrBuilder messageOrBuilder) {
            try {
                return JsonFormat.printer().print(messageOrBuilder);
            } catch (InvalidProtocolBufferException e) {
                // ignore
            }
        } else if (object instanceof Throwable throwable) {
            return object;
        }
        return object.toString();
    }


    private static String toBase64(ByteString bs) {
        return Base64.getEncoder().encodeToString(bs.toByteArray());
    }

}
