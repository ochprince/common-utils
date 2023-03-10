/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.notifications.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.colasoft.opensearch.commons.notifications.model.MethodType.Companion.enumParser
import com.colasoft.opensearch.commons.notifications.model.MethodType.Companion.fromTagOrDefault

internal class MethodTypeTests {

    @Test
    fun `toString should return tag`() {
        MethodType.values().forEach {
            assertEquals(it.tag, it.toString())
        }
    }

    @Test
    fun `fromTagOrDefault should return corresponding enum`() {
        MethodType.values().forEach {
            assertEquals(it, fromTagOrDefault(it.tag))
        }
    }

    @Test
    fun `EnumParser fromTagOrDefault should return corresponding enum`() {
        MethodType.values().forEach {
            assertEquals(it, enumParser.fromTagOrDefault(it.tag))
        }
    }
}
