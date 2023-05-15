package com.colasoft.opensearch.commons.alerting.action

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.commons.alerting.util.IndexUtils
import com.colasoft.opensearch.commons.notifications.action.BaseResponse
import com.colasoft.opensearch.core.xcontent.ToXContent
import com.colasoft.opensearch.core.xcontent.XContentBuilder

class DeleteMonitorResponse : BaseResponse {
    var id: String
    var version: Long

    constructor(
        id: String,
        version: Long
    ) : super() {
        this.id = id
        this.version = version
    }

    constructor(sin: StreamInput) : this(
        sin.readString(), // id
        sin.readLong() // version
    )

    override fun writeTo(out: StreamOutput) {
        out.writeString(id)
        out.writeLong(version)
    }

    override fun toXContent(builder: XContentBuilder, params: ToXContent.Params): XContentBuilder {
        return builder.startObject()
            .field(IndexUtils._ID, id)
            .field(IndexUtils._VERSION, version)
            .endObject()
    }
}
