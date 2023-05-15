/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.notifications.action

import com.colasoft.opensearch.action.ActionRequest
import com.colasoft.opensearch.action.ActionRequestValidationException
import com.colasoft.opensearch.action.ValidateActions
import com.colasoft.opensearch.common.Strings
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.common.io.stream.Writeable
import com.colasoft.opensearch.common.xcontent.XContentParserUtils
import com.colasoft.opensearch.commons.notifications.NotificationConstants.CONFIG_ID_TAG
import com.colasoft.opensearch.commons.notifications.NotificationConstants.CONFIG_TAG
import com.colasoft.opensearch.commons.notifications.model.NotificationConfig
import com.colasoft.opensearch.commons.utils.logger
import com.colasoft.opensearch.core.xcontent.ToXContent
import com.colasoft.opensearch.core.xcontent.ToXContentObject
import com.colasoft.opensearch.core.xcontent.XContentBuilder
import com.colasoft.opensearch.core.xcontent.XContentParser
import java.io.IOException

/**
 * Action request for updating notification configuration.
 */
class UpdateNotificationConfigRequest : ActionRequest, ToXContentObject {
    val configId: String
    val notificationConfig: NotificationConfig

    companion object {
        private val log by logger(UpdateNotificationConfigRequest::class.java)

        /**
         * reader to create instance of class from writable.
         */
        val reader = Writeable.Reader { UpdateNotificationConfigRequest(it) }

        /**
         * Creator used in REST communication.
         * @param parser XContentParser to deserialize data from.
         * @param id optional id to use if missed in XContent
         */
        @JvmStatic
        @Throws(IOException::class)
        fun parse(parser: XContentParser, id: String? = null): UpdateNotificationConfigRequest {
            var configId: String? = id
            var notificationConfig: NotificationConfig? = null

            XContentParserUtils.ensureExpectedToken(
                XContentParser.Token.START_OBJECT,
                parser.currentToken(),
                parser
            )
            while (parser.nextToken() != XContentParser.Token.END_OBJECT) {
                val fieldName = parser.currentName()
                parser.nextToken()
                when (fieldName) {
                    CONFIG_ID_TAG -> configId = parser.text()
                    CONFIG_TAG -> notificationConfig = NotificationConfig.parse(parser)
                    else -> {
                        parser.skipChildren()
                        log.info("Unexpected field: $fieldName, while parsing UpdateNotificationConfigRequest")
                    }
                }
            }
            configId ?: throw IllegalArgumentException("$CONFIG_ID_TAG field absent")
            notificationConfig ?: throw IllegalArgumentException("$CONFIG_TAG field absent")
            return UpdateNotificationConfigRequest(configId, notificationConfig)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun toXContent(builder: XContentBuilder?, params: ToXContent.Params?): XContentBuilder {
        builder!!
        return builder.startObject()
            .field(CONFIG_ID_TAG, configId)
            .field(CONFIG_TAG, notificationConfig)
            .endObject()
    }

    /**
     * constructor for creating the class
     * @param configId the id notification config object
     * @param notificationConfig the notification config object
     */
    constructor(configId: String, notificationConfig: NotificationConfig) {
        this.configId = configId
        this.notificationConfig = notificationConfig
    }

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    constructor(input: StreamInput) : super(input) {
        configId = input.readString()
        notificationConfig = NotificationConfig.reader.read(input)!!
    }

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    override fun writeTo(output: StreamOutput) {
        super.writeTo(output)
        output.writeString(configId)
        notificationConfig.writeTo(output)
    }

    /**
     * {@inheritDoc}
     */
    override fun validate(): ActionRequestValidationException? {
        var validationException: ActionRequestValidationException? = null
        if (Strings.isNullOrEmpty(configId)) {
            validationException = ValidateActions.addValidationError("configId is null or empty", validationException)
        }
        return validationException
    }
}
