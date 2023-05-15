/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.notifications.model

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.common.io.stream.Writeable
import com.colasoft.opensearch.common.xcontent.XContentParserUtils
import com.colasoft.opensearch.commons.notifications.NotificationConstants.RECIPIENT_LIST_TAG
import com.colasoft.opensearch.commons.utils.logger
import com.colasoft.opensearch.commons.utils.objectList
import com.colasoft.opensearch.core.xcontent.ToXContent
import com.colasoft.opensearch.core.xcontent.XContentBuilder
import com.colasoft.opensearch.core.xcontent.XContentParser
import java.io.IOException

/**
 * Data class representing Email group.
 */
data class EmailGroup(
    val recipients: List<EmailRecipient>
) : BaseConfigData {

    companion object {
        private val log by logger(EmailGroup::class.java)

        /**
         * reader to create instance of class from writable.
         */
        val reader = Writeable.Reader { EmailGroup(it) }

        /**
         * Parser to parse xContent
         */
        val xParser = XParser { parse(it) }

        /**
         * Creator used in REST communication.
         * @param parser XContentParser to deserialize data from.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun parse(parser: XContentParser): EmailGroup {
            var recipients: List<EmailRecipient>? = null

            XContentParserUtils.ensureExpectedToken(
                XContentParser.Token.START_OBJECT,
                parser.currentToken(),
                parser
            )
            while (parser.nextToken() != XContentParser.Token.END_OBJECT) {
                val fieldName = parser.currentName()
                parser.nextToken()
                when (fieldName) {
                    RECIPIENT_LIST_TAG -> recipients = parser.objectList { EmailRecipient.parse(it) }
                    else -> {
                        parser.skipChildren()
                        log.info("Unexpected field: $fieldName, while parsing EmailGroup")
                    }
                }
            }
            recipients ?: throw IllegalArgumentException("$RECIPIENT_LIST_TAG field absent")
            return EmailGroup(recipients)
        }
    }

    /**
     * Constructor used in transport action communication.
     * @param input StreamInput stream to deserialize data from.
     */
    constructor(input: StreamInput) : this(
        recipients = input.readList(EmailRecipient.reader)
    )

    /**
     * {@inheritDoc}
     */
    override fun writeTo(output: StreamOutput) {
        output.writeList(recipients)
    }

    /**
     * {@inheritDoc}
     */
    override fun toXContent(builder: XContentBuilder?, params: ToXContent.Params?): XContentBuilder {
        builder!!
        return builder.startObject()
            .field(RECIPIENT_LIST_TAG, recipients)
            .endObject()
    }
}
