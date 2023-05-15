package com.colasoft.opensearch.commons.alerting.action

import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.commons.alerting.model.Monitor
import com.colasoft.opensearch.commons.alerting.util.IndexUtils.Companion._ID
import com.colasoft.opensearch.commons.alerting.util.IndexUtils.Companion._PRIMARY_TERM
import com.colasoft.opensearch.commons.alerting.util.IndexUtils.Companion._SEQ_NO
import com.colasoft.opensearch.commons.alerting.util.IndexUtils.Companion._VERSION
import com.colasoft.opensearch.commons.notifications.action.BaseResponse
import com.colasoft.opensearch.core.xcontent.ToXContent
import com.colasoft.opensearch.core.xcontent.XContentBuilder
import java.io.IOException

class IndexMonitorResponse : BaseResponse {
    var id: String
    var version: Long
    var seqNo: Long
    var primaryTerm: Long
    var monitor: Monitor

    constructor(
        id: String,
        version: Long,
        seqNo: Long,
        primaryTerm: Long,
        monitor: Monitor
    ) : super() {
        this.id = id
        this.version = version
        this.seqNo = seqNo
        this.primaryTerm = primaryTerm
        this.monitor = monitor
    }

    @Throws(IOException::class)
    constructor(sin: StreamInput) : this(
        sin.readString(), // id
        sin.readLong(), // version
        sin.readLong(), // seqNo
        sin.readLong(), // primaryTerm
        Monitor.readFrom(sin) as Monitor // monitor
    )

    @Throws(IOException::class)
    override fun writeTo(out: StreamOutput) {
        out.writeString(id)
        out.writeLong(version)
        out.writeLong(seqNo)
        out.writeLong(primaryTerm)
        monitor.writeTo(out)
    }

    @Throws(IOException::class)
    override fun toXContent(builder: XContentBuilder, params: ToXContent.Params): XContentBuilder {
        return builder.startObject()
            .field(_ID, id)
            .field(_VERSION, version)
            .field(_SEQ_NO, seqNo)
            .field(_PRIMARY_TERM, primaryTerm)
            .field("monitor", monitor)
            .endObject()
    }
}
