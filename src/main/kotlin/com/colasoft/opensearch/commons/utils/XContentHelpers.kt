/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.colasoft.opensearch.commons.utils

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.xcontent.DeprecationHandler
import com.colasoft.opensearch.common.xcontent.NamedXContentRegistry
import com.colasoft.opensearch.common.xcontent.ToXContent
import com.colasoft.opensearch.common.xcontent.ToXContentObject
import com.colasoft.opensearch.common.xcontent.XContentBuilder
import com.colasoft.opensearch.common.xcontent.XContentParser
import com.colasoft.opensearch.common.xcontent.XContentParserUtils
import com.colasoft.opensearch.common.xcontent.XContentType
import com.colasoft.opensearch.rest.RestRequest

fun StreamInput.createJsonParser(): XContentParser {
    return XContentType.JSON.xContent()
        .createParser(NamedXContentRegistry.EMPTY, DeprecationHandler.IGNORE_DEPRECATIONS, this)
}

fun RestRequest.contentParserNextToken(): XContentParser {
    val parser = this.contentParser()
    parser.nextToken()
    return parser
}

fun XContentParser.stringList(): List<String> {
    val retList: MutableList<String> = mutableListOf()
    XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_ARRAY, currentToken(), this)
    while (nextToken() != XContentParser.Token.END_ARRAY) {
        retList.add(text())
    }
    return retList
}

fun XContentBuilder.fieldIfNotNull(name: String, value: Any?): XContentBuilder {
    if (value != null) {
        this.field(name, value)
    }
    return this
}

fun XContentBuilder.objectIfNotNull(name: String, xContentObject: ToXContentObject?): XContentBuilder {
    if (xContentObject != null) {
        this.field(name)
        xContentObject.toXContent(this, ToXContent.EMPTY_PARAMS)
    }
    return this
}

fun <T : ToXContent> XContentParser.objectList(block: (XContentParser) -> T): List<T> {
    val retList: MutableList<T> = mutableListOf()
    XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_ARRAY, currentToken(), this)
    while (nextToken() != XContentParser.Token.END_ARRAY) {
        retList.add(block(this))
    }
    return retList
}
