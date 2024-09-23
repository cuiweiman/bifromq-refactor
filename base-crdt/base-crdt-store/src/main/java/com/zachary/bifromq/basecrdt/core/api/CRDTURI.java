package com.zachary.bifromq.basecrdt.core.api;

import com.google.common.base.Preconditions;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;

import java.util.regex.Pattern;

/**
 * @description: CRDT URI
 * @author: cuiweiman
 * @date: 2024/9/23 17:57
 */
public final class CRDTURI {
    /**
     * 名称 模型匹配, 至少一个 字母、数字、下划线_、短横线-、冒号:、点. 组成的字符串
     */
    private static final Pattern NAME;
    /**
     * URI 模型匹配, {@link CausalCRDTType 枚举开头的地址}
     */
    private static final Pattern URI;

    /*
     * 初始化模型匹配静态变量值
     */
    static {
        // 能够匹配一个或多个由字母（无论大小写）、数字、下划线（_）、短横线（-）、冒号（:）和点（.）组成的字符串
        String namePattern = "[\\w-:.]+";
        StringBuilder uriPattern = new StringBuilder("^(");
        // uri 模型匹配 拼接上 所有 CRDT Type 的 name
        for (CausalCRDTType type : CausalCRDTType.values()) {
            uriPattern.append(type.name());
            uriPattern.append("|");
        }
        // uri 模型匹配 移除最后一个 "|"
        uriPattern.deleteCharAt(uriPattern.length() - 1);
        // uri 模型匹配 拼接上 namePattern
        uriPattern.append("):" + namePattern);
        NAME = Pattern.compile(namePattern);
        URI = Pattern.compile(uriPattern.toString());
    }

    /**
     * 判断 uri 是否匹配 {@link #URI}
     *
     * @param uri uri
     * @return boolean
     */
    public static boolean isValidURI(String uri) {
        return URI.matcher(uri).matches();
    }

    /**
     * 拼接 uri type:name
     *
     * @param type type
     * @param name name
     * @return uri
     */
    public static String toURI(CausalCRDTType type, String name) {
        Preconditions.checkArgument(NAME.matcher(name).matches(), "Invalid name");
        return type.name() + ":" + name;
    }

    /**
     * 根据 uri 解析 CausalCRDTType 枚举值
     *
     * @param uri uri
     * @return CausalCRDTType
     */
    public static CausalCRDTType parseType(String uri) {
        checkURI(uri);
        return CausalCRDTType.valueOf(uri.split(":")[0]);
    }

    /**
     * 根据 uri 解析 name
     *
     * @param uri uri
     * @return CausalCRDTType
     */
    public static String parseName(String uri) {
        checkURI(uri);
        return uri.substring(uri.indexOf(":") + 1);
    }

    public static void checkURI(String uri) {
        Preconditions.checkArgument(isValidURI(uri), "Invalid CRDT replica uri format");
    }
}
