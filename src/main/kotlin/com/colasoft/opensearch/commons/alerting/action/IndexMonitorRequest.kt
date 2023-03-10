package com.colasoft.opensearch.commons.alerting.action

import com.colasoft.opensearch.action.ActionRequest
import com.colasoft.opensearch.action.ActionRequestValidationException
import com.colasoft.opensearch.action.support.WriteRequest
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.common.io.stream.StreamOutput
import com.colasoft.opensearch.commons.alerting.model.Monitor
import com.colasoft.opensearch.rest.RestRequest
import java.io.IOException

class IndexMonitorRequest : ActionRequest {
    val monitorId: String
    val seqNo: Long
    val primaryTerm: Long
    val refreshPolicy: WriteRequest.RefreshPolicy
    val method: RestRequest.Method
    var monitor: Monitor
    val rbacRoles: List<String>?

    constructor(
        monitorId: String,
        seqNo: Long,
        primaryTerm: Long,
        refreshPolicy: WriteRequest.RefreshPolicy,
        method: RestRequest.Method,
        monitor: Monitor,
        rbacRoles: List<String>? = null
    ) : super() {
        this.monitorId = monitorId
        this.seqNo = seqNo
        this.primaryTerm = primaryTerm
        this.refreshPolicy = refreshPolicy
        this.method = method
        this.monitor = monitor
        this.rbacRoles = rbacRoles
    }

    @Throws(IOException::class)
    constructor(sin: StreamInput) : this(
        monitorId = sin.readString(),
        seqNo = sin.readLong(),
        primaryTerm = sin.readLong(),
        refreshPolicy = WriteRequest.RefreshPolicy.readFrom(sin),
        method = sin.readEnum(RestRequest.Method::class.java),
        monitor = Monitor.readFrom(sin) as Monitor,
        rbacRoles = sin.readOptionalStringList()
    )

    override fun validate(): ActionRequestValidationException? {
        return null
    }

    @Throws(IOException::class)
    override fun writeTo(out: StreamOutput) {
        out.writeString(monitorId)
        out.writeLong(seqNo)
        out.writeLong(primaryTerm)
        refreshPolicy.writeTo(out)
        out.writeEnum(method)
        monitor.writeTo(out)
        out.writeOptionalStringCollection(rbacRoles)
    }
}
