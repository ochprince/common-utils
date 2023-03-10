/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.notifications.model

import com.colasoft.opensearch.common.xcontent.XContentParser

/**
 * Functional interface to create config data object using XContentParser
 */
fun interface XParser<V> {
    /**
     * Creator used in REST communication.
     * @param parser XContentParser to deserialize data from.
     */
    fun parse(parser: XContentParser): V
}
