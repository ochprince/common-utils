/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.colasoft.opensearch.commons.utils

import com.colasoft.opensearch.common.xcontent.DeprecationHandler
import com.colasoft.opensearch.common.xcontent.NamedXContentRegistry
import com.colasoft.opensearch.common.xcontent.ToXContent
import com.colasoft.opensearch.common.xcontent.XContentFactory
import com.colasoft.opensearch.common.xcontent.XContentParser
import com.colasoft.opensearch.common.xcontent.XContentType
import java.io.ByteArrayOutputStream

fun getJsonString(xContent: ToXContent): String {
    ByteArrayOutputStream().use { byteArrayOutputStream ->
        val builder = XContentFactory.jsonBuilder(byteArrayOutputStream)
        xContent.toXContent(builder, ToXContent.EMPTY_PARAMS)
        builder.close()
        return byteArrayOutputStream.toString("UTF8")
    }
}

inline fun <reified CreateType> createObjectFromJsonString(
    jsonString: String,
    block: (XContentParser) -> CreateType
): CreateType {
    val parser = XContentType.JSON.xContent()
        .createParser(NamedXContentRegistry.EMPTY, DeprecationHandler.IGNORE_DEPRECATIONS, jsonString)
    parser.nextToken()
    return block(parser)
}
