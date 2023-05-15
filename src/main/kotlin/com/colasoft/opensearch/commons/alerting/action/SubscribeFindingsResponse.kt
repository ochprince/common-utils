package com.colasoft.opensearch.commons.alerting.action

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.commons.notifications.action.BaseResponse
import com.colasoft.opensearch.core.xcontent.ToXContent
import com.colasoft.opensearch.core.xcontent.XContentBuilder
import com.colasoft.opensearch.rest.RestStatus
import java.io.IOException

class SubscribeFindingsResponse : BaseResponse {

    private var status: RestStatus

    constructor(status: RestStatus) : super() {
        this.status = status
    }

    @Throws(IOException::class)
    constructor(sin: StreamInput) {
        this.status = sin.readEnum(RestStatus::class.java)
    }

    @Throws(IOException::class)
    override fun writeTo(out: StreamOutput) {
        out.writeEnum(status)
    }

    override fun toXContent(builder: XContentBuilder, params: ToXContent.Params): XContentBuilder {
        builder.startObject()
            .field("status", status.status)
        return builder.endObject()
    }

    override fun getStatus(): RestStatus {
        return this.status
    }
}
