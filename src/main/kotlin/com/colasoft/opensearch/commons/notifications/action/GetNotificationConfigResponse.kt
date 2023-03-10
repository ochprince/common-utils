/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.notifications.action

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.common.io.stream.Writeable
import com.colasoft.opensearch.common.xcontent.ToXContent
import com.colasoft.opensearch.common.xcontent.XContentBuilder
import com.colasoft.opensearch.common.xcontent.XContentParser
import com.colasoft.opensearch.commons.notifications.model.NotificationConfigSearchResult
import java.io.IOException

/**
 * Action Response for getting notification configuration.
 */
class GetNotificationConfigResponse : BaseResponse {
    val searchResult: NotificationConfigSearchResult

    companion object {

        /**
         * reader to create instance of class from writable.
         */
        val reader = Writeable.Reader { GetNotificationConfigResponse(it) }

        /**
         * Creator used in REST communication.
         * @param parser XContentParser to deserialize data from.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun parse(parser: XContentParser): GetNotificationConfigResponse {
            return GetNotificationConfigResponse(NotificationConfigSearchResult(parser))
        }
    }

    /**
     * constructor for creating the class
     * @param searchResult the notification configuration list
     */
    constructor(searchResult: NotificationConfigSearchResult) {
        this.searchResult = searchResult
    }

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    constructor(input: StreamInput) : super(input) {
        searchResult = NotificationConfigSearchResult(input)
    }

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    override fun writeTo(output: StreamOutput) {
        searchResult.writeTo(output)
    }

    /**
     * {@inheritDoc}
     */
    override fun toXContent(builder: XContentBuilder?, params: ToXContent.Params?): XContentBuilder {
        return searchResult.toXContent(builder, params)
    }
}
