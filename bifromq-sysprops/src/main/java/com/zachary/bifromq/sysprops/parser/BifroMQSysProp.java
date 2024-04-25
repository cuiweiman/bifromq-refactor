package com.zachary.bifromq.sysprops.parser;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.sysprops.parser.parser.BooleanParser;
import com.zachary.bifromq.sysprops.parser.parser.DoubleParser;
import com.zachary.bifromq.sysprops.parser.parser.IntegerParser;
import com.zachary.bifromq.sysprops.parser.parser.LongParser;
import com.zachary.bifromq.sysprops.parser.parser.PropParser;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: bifromq 系统属性配置
 * @author: cuiweiman
 * @date: 2024/4/3 11:52
 */
@Slf4j
public enum BifroMQSysProp {
    // further check if utf8 string contains any control character or non character according to [MQTT-1.5.3]
    // 根据 [MQTT-1.5.3] 进一步检查 utf8 字符串是否包含任何控制字符或非字符

    /**
     * mqtt utf8 健康性 检查
     */
    MQTT_UTF8_SANITY_CHECK("mqtt_utf8_sanity_check", false, BooleanParser.INSTANCE),

    /**
     * Client ID 最大长度检查
     */
    MAX_CLIENT_ID_LENGTH("max_client_id_length", 65535, IntegerParser.from(23, 65536)),

    /**
     * 最大共享组成员 数量校验
     */
    MAX_SHARE_GROUP_MEMBERS("max_shared_group_members", 200, IntegerParser.POSITIVE),

    /**
     * 每个收件箱的 最大主题 过滤器数
     */
    MAX_TOPIC_FILTERS_PER_INBOX("max_topic_filters_per_inbox", 100, IntegerParser.POSITIVE),

    /**
     * dist 客户端 每个队列的 最大调用数
     */
    DIST_CLIENT_MAX_INFLIGHT_CALLS_PER_QUEUE("dist_client_max_calls_per_queue", 1, IntegerParser.POSITIVE),

    /**
     * dist 服务器 每个队列的 最大调用数
     */
    DIST_SERVER_MAX_INFLIGHT_CALLS_PER_QUEUE("dist_server_max_calls_per_queue", 1, IntegerParser.POSITIVE),

    /**
     * dist 服务器 最大 批量 主题数
     */
    DIST_MAX_TOPICS_IN_BATCH("dist_server_max_topics_in_batch", 200, IntegerParser.POSITIVE),

    /**
     * dist 服务器 最大 批量 更新
     */
    DIST_MAX_UPDATES_IN_BATCH("dist_server_max_updates_in_batch", 100, IntegerParser.POSITIVE),

    /**
     * dist 服务器 dist 工作线程 调用队列
     */
    DIST_WORKER_CALL_QUEUES("dist_server_dist_worker_call_queues", 16, IntegerParser.POSITIVE),

    /**
     * Dist Worker 订阅模式 并发
     * CPU核心数 / 2
     */
    DIST_FAN_OUT_PARALLELISM("dist_worker_fanout_parallelism", Math.max(2, EnvProvider.INSTANCE.availableProcessors() / 2), IntegerParser.POSITIVE),

    /**
     * Dist Worker 最大 inflight 发送
     */
    DIST_WORKER_MAX_INFLIGHT_CALLS_PER_QUEUE("dist_worker_max_inflight_send", 1, IntegerParser.POSITIVE),

    /**
     * dist worker 最大批量发送消息
     */
    DIST_MAX_BATCH_SEND_MESSAGES("dist_worker_max_batch_send_messages", 100, IntegerParser.POSITIVE),

    /**
     * 每个租户的 Dist Worker 最大缓存 订阅数
     */
    DIST_MAX_CACHED_SUBS_PER_TENANT("dist_worker_max_cached_subs_per_tenant", 100_000L, LongParser.POSITIVE),

    /**
     * dist 工作线程 主题匹配 过期秒数
     */
    DIST_TOPIC_MATCH_EXPIRY("dist_worker_topic_match_expiry_seconds", 5, IntegerParser.POSITIVE),

    /**
     * dist worker 匹配 并行量
     */
    DIST_MATCH_PARALLELISM("dist_worker_match_parallelism", Math.max(2, EnvProvider.INSTANCE.availableProcessors() / 2), IntegerParser.POSITIVE),

    /**
     * dist worker 最大 范围 负载
     */
    DIST_MAX_RANGE_LOAD("dist_worker_max_range_load", 300_000L, IntegerParser.POSITIVE),

    /**
     * dist worker 分割键 阈值
     */
    DIST_SPLIT_KEY_EST_THRESHOLD("dist_worker_split_key_threshold", 0.7D, DoubleParser.from(0.0, 1.0, true)),

    /**
     * dist worker 负载跟踪 秒数
     */
    DIST_LOAD_TRACKING_SECONDS("dist_worker_load_tracking_seconds", 5, IntegerParser.POSITIVE),

    /**
     * dist worker 节点副本投票 计数
     */
    DIST_WORKER_VOTER_COUNT("dist_worker_replica_voter_count", 3, IntegerParser.POSITIVE),

    /**
     * dist worker 副本 学习者 计数
     */
    DIST_WORKER_LEARNER_COUNT("dist_worker_replica_learner_count", 3, IntegerParser.POSITIVE),

    /**
     * Dist Worker 恢复超时 毫秒
     */
    DIST_WORKER_RECOVERY_TIMEOUT_MILLIS("dist_worker_recovery_timeout_millis", 10000L, LongParser.NON_NEGATIVE),

    /**
     * 消息投递者 数量
     */
    INBOX_DELIVERERS("inbox_deliverers", 100, IntegerParser.POSITIVE),

    /**
     * 收件箱 获取管道 创建 速率 限制
     */
    INBOX_FETCH_PIPELINE_CREATION_RATE_LIMIT("inbox_fetch_pipeline_creation_rate_limit", 5000.0, DoubleParser.from(0.0, Double.MAX_VALUE, true)),

    /**
     * 每次插入 收件箱的 最大 信息数
     */
    INBOX_MAX_INBOXES_PER_INSERT("inbox_max_inboxes_per_insert", 500, IntegerParser.POSITIVE),

    /**
     * 每次 收件箱 提交的  最大 信息数
     */
    INBOX_MAX_INBOXES_PER_COMMIT("inbox_max_inboxes_per_commit", 500, IntegerParser.POSITIVE),

    /**
     * 每次 收件箱 创建的 最大 信息数
     */
    INBOX_MAX_INBOXES_PER_CREATE("inbox_max_inboxes_per_create", 500, IntegerParser.POSITIVE),

    /**
     * 收件箱最大
     */
    INBOX_MAX_INBOXES_PER_TOUCH("inbox_max_inboxes_per_touch", 500, IntegerParser.POSITIVE),

    /**
     * 收件箱最大 拉取数量
     */
    INBOX_MAX_INBOXES_PER_FETCH("inbox_max_inboxes_per_fetch", 500, IntegerParser.POSITIVE),

    /**
     * 收件箱 每次检查消息的 最大数量
     */
    INBOX_MAX_INBOXES_PER_CHECK("inbox_max_inboxes_per_check", 500, IntegerParser.POSITIVE),

    /**
     * 收件箱 每次插入 消息的 最大字节数
     * 1MB
     */
    INBOX_MAX_BYTES_PER_INSERT("inbox_max_bytes_per_insert", 1024 * 1024 * 1024, Integer::parseUnsignedInt),

    /**
     * 收件箱 拉取消息 的 速率
     */
    INBOX_FETCH_QUEUES_PER_RANGE("inbox_fetch_queues_per_range", Math.max(1, EnvProvider.INSTANCE.availableProcessors() / 4), IntegerParser.POSITIVE),

    /**
     * 收件箱 检查消息 的 速率
     */
    INBOX_CHECK_QUEUES_PER_RANGE("inbox_check_queues_per_range", 1, IntegerParser.POSITIVE),

    /**
     * 每台服务器的 MQTT 发送者数量
     */
    MQTT_DELIVERERS_PER_SERVER("mqtt_deliverers_per_server", 4, IntegerParser.POSITIVE);


    public final String propKey;
    private final Object propDefValue;
    private final PropParser<?> parser;

    BifroMQSysProp(String propKey, Object propDefValue, PropParser<?> parser) {
        this.propKey = propKey;
        this.propDefValue = propDefValue;
        this.parser = parser;
    }

    private String sysPropValue(final String key) {
        String value = null;
        try {
            value = System.getProperty(key);
        } catch (SecurityException e) {
            log.warn("Failed to retrieve a system property '{}'", key, e);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> T defVal() {
        return (T) propDefValue;
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        String value = sysPropValue(propKey);
        if (value == null) {
            return defVal();
        }

        value = value.trim().toLowerCase();
        if (value.isEmpty()) {
            return defVal();
        }
        try {
            return (T) parser.parse(value);
        } catch (Throwable e) {
            log.warn("Failed to parse system prop '{}':{} - using the default value: {}", propKey, value, defVal());
            return defVal();
        }
    }


}
