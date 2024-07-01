package com.zachary.bifromq.retain.test.util;

import com.google.protobuf.ByteString;
import org.testng.annotations.Test;

import static com.zachary.bifromq.retain.utils.KeyUtil.isTenantNS;
import static com.zachary.bifromq.retain.utils.KeyUtil.parseTenantNS;
import static com.zachary.bifromq.retain.utils.KeyUtil.parseTopic;
import static com.zachary.bifromq.retain.utils.KeyUtil.retainKey;
import static com.zachary.bifromq.retain.utils.KeyUtil.retainKeyPrefix;
import static com.zachary.bifromq.retain.utils.KeyUtil.tenantNS;
import static com.zachary.bifromq.retain.utils.TopicUtil.parse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class KeyUtilTest {
    @Test
    public void testParseTenantNS() {
        ByteString tenantNS = tenantNS("tenantA");
        assertEquals(parseTenantNS(tenantNS), tenantNS);

        assertEquals(parseTenantNS(retainKey(tenantNS, "/a/b/c")), tenantNS);
        assertEquals(parseTenantNS(retainKeyPrefix(tenantNS, parse("/a/b/c", false))), tenantNS);
    }

    @Test
    public void testIsTenantNS() {
        ByteString tenantNS = tenantNS("tenantA");
        assertTrue(isTenantNS(tenantNS));
        assertFalse(isTenantNS(retainKey(tenantNS, "/a/b/c")));
        assertFalse(isTenantNS(retainKeyPrefix(tenantNS, parse("/a/b/c", false))));
    }

    @Test
    public void testParseTopic() {
        ByteString tenantNS = tenantNS("tenantA");
        String topic = "/a/b/c";
        assertEquals(parse(topic, false), parseTopic(retainKey(tenantNS, topic)));
    }
}
