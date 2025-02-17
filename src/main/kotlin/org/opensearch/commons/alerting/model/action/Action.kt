package org.opensearch.commons.alerting.model.action

import org.opensearch.common.UUIDs
import org.opensearch.common.io.stream.StreamInput
import org.opensearch.common.io.stream.StreamOutput
import org.opensearch.common.xcontent.XContentParserUtils
import org.opensearch.commons.notifications.model.BaseModel
import org.opensearch.core.xcontent.ToXContent
import org.opensearch.core.xcontent.XContentBuilder
import org.opensearch.core.xcontent.XContentParser
import org.opensearch.script.Script
import java.io.IOException

data class Action(
    val name: String,
    val destinationId: String,
    val subjectTemplate: Script?,
    val messageTemplate: Script,
    val throttleEnabled: Boolean,
    val throttle: Throttle?,
    val id: String = UUIDs.base64UUID(),
    val actionExecutionPolicy: ActionExecutionPolicy? = null
) : BaseModel {

    init {
        if (subjectTemplate != null) {
            require(subjectTemplate.lang == MUSTACHE) { "subject_template must be a mustache script" }
        }
        require(messageTemplate.lang == MUSTACHE) { "message_template must be a mustache script" }

        if (actionExecutionPolicy?.actionExecutionScope is PerExecutionActionScope) {
            require(throttle == null) { "Throttle is currently not supported for per execution action scope" }
        }
    }

    @Throws(IOException::class)
    constructor(sin: StreamInput) : this(
        sin.readString(), // name
        sin.readString(), // destinationId
        sin.readOptionalWriteable(::Script), // subjectTemplate
        Script(sin), // messageTemplate
        sin.readBoolean(), // throttleEnabled
        sin.readOptionalWriteable(::Throttle), // throttle
        sin.readString(), // id
        sin.readOptionalWriteable(::ActionExecutionPolicy) // actionExecutionPolicy
    )

    override fun toXContent(builder: XContentBuilder, params: ToXContent.Params): XContentBuilder {
        val xContentBuilder = builder.startObject()
            .field(ID_FIELD, id)
            .field(NAME_FIELD, name)
            .field(DESTINATION_ID_FIELD, destinationId)
            .field(MESSAGE_TEMPLATE_FIELD, messageTemplate)
            .field(THROTTLE_ENABLED_FIELD, throttleEnabled)
        if (subjectTemplate != null) {
            xContentBuilder.field(SUBJECT_TEMPLATE_FIELD, subjectTemplate)
        }
        if (throttle != null) {
            xContentBuilder.field(THROTTLE_FIELD, throttle)
        }
        if (actionExecutionPolicy != null) {
            xContentBuilder.field(ACTION_EXECUTION_POLICY_FIELD, actionExecutionPolicy)
        }
        return xContentBuilder.endObject()
    }

    fun asTemplateArg(): Map<String, Any> {
        return mapOf(NAME_FIELD to name)
    }

    @Throws(IOException::class)
    override fun writeTo(out: StreamOutput) {
        out.writeString(name)
        out.writeString(destinationId)
        if (subjectTemplate != null) {
            out.writeBoolean(true)
            subjectTemplate.writeTo(out)
        } else {
            out.writeBoolean(false)
        }
        messageTemplate.writeTo(out)
        out.writeBoolean(throttleEnabled)
        if (throttle != null) {
            out.writeBoolean(true)
            throttle.writeTo(out)
        } else {
            out.writeBoolean(false)
        }
        out.writeString(id)
        if (actionExecutionPolicy != null) {
            out.writeBoolean(true)
            actionExecutionPolicy.writeTo(out)
        } else {
            out.writeBoolean(false)
        }
    }

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val DESTINATION_ID_FIELD = "destination_id"
        const val SUBJECT_TEMPLATE_FIELD = "subject_template"
        const val MESSAGE_TEMPLATE_FIELD = "message_template"
        const val THROTTLE_ENABLED_FIELD = "throttle_enabled"
        const val THROTTLE_FIELD = "throttle"
        const val ACTION_EXECUTION_POLICY_FIELD = "action_execution_policy"
        const val MUSTACHE = "mustache"
        const val SUBJECT = "subject"
        const val MESSAGE = "message"
        const val MESSAGE_ID = "messageId"

        @JvmStatic
        @Throws(IOException::class)
        fun parse(xcp: XContentParser): Action {
            var id = UUIDs.base64UUID() // assign a default action id if one is not specified
            lateinit var name: String
            lateinit var destinationId: String
            var subjectTemplate: Script? = null // subject template could be null for some destinations
            lateinit var messageTemplate: Script
            var throttleEnabled = false
            var throttle: Throttle? = null
            var actionExecutionPolicy: ActionExecutionPolicy? = null

            XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_OBJECT, xcp.currentToken(), xcp)
            while (xcp.nextToken() != XContentParser.Token.END_OBJECT) {
                val fieldName = xcp.currentName()
                xcp.nextToken()
                when (fieldName) {
                    ID_FIELD -> id = xcp.text()
                    NAME_FIELD -> name = xcp.textOrNull()
                    DESTINATION_ID_FIELD -> destinationId = xcp.textOrNull()
                    SUBJECT_TEMPLATE_FIELD -> {
                        subjectTemplate = if (xcp.currentToken() == XContentParser.Token.VALUE_NULL) null else
                            Script.parse(xcp, Script.DEFAULT_TEMPLATE_LANG)
                    }
                    MESSAGE_TEMPLATE_FIELD -> messageTemplate = Script.parse(xcp, Script.DEFAULT_TEMPLATE_LANG)
                    THROTTLE_FIELD -> {
                        throttle = if (xcp.currentToken() == XContentParser.Token.VALUE_NULL) null else Throttle.parse(xcp)
                    }
                    THROTTLE_ENABLED_FIELD -> {
                        throttleEnabled = xcp.booleanValue()
                    }
                    ACTION_EXECUTION_POLICY_FIELD -> {
                        actionExecutionPolicy = if (xcp.currentToken() == XContentParser.Token.VALUE_NULL) {
                            null
                        } else {
                            ActionExecutionPolicy.parse(xcp)
                        }
                    }
                    else -> {
                        throw IllegalStateException("Unexpected field: $fieldName, while parsing action")
                    }
                }
            }

            if (throttleEnabled) {
                requireNotNull(throttle, { "Action throttle enabled but not set throttle value" })
            }

            return Action(
                requireNotNull(name) { "Action name is null" },
                requireNotNull(destinationId) { "Destination id is null" },
                subjectTemplate,
                requireNotNull(messageTemplate) { "Action message template is null" },
                throttleEnabled,
                throttle,
                id = requireNotNull(id),
                actionExecutionPolicy = actionExecutionPolicy
            )
        }

        @JvmStatic
        @Throws(IOException::class)
        fun readFrom(sin: StreamInput): Action {
            return Action(sin)
        }
    }
}
