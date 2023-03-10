/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.alerting

import com.colasoft.opensearch.action.ActionListener
import com.colasoft.opensearch.action.ActionResponse
import com.colasoft.opensearch.client.node.NodeClient
import com.colasoft.opensearch.common.io.stream.NamedWriteableRegistry
import com.colasoft.opensearch.common.io.stream.Writeable
import com.colasoft.opensearch.commons.alerting.action.AcknowledgeAlertRequest
import com.colasoft.opensearch.commons.alerting.action.AcknowledgeAlertResponse
import com.colasoft.opensearch.commons.alerting.action.AlertingActions
import com.colasoft.opensearch.commons.alerting.action.DeleteMonitorRequest
import com.colasoft.opensearch.commons.alerting.action.DeleteMonitorResponse
import com.colasoft.opensearch.commons.alerting.action.GetAlertsRequest
import com.colasoft.opensearch.commons.alerting.action.GetAlertsResponse
import com.colasoft.opensearch.commons.alerting.action.GetFindingsRequest
import com.colasoft.opensearch.commons.alerting.action.GetFindingsResponse
import com.colasoft.opensearch.commons.alerting.action.IndexMonitorRequest
import com.colasoft.opensearch.commons.alerting.action.IndexMonitorResponse
import com.colasoft.opensearch.commons.notifications.action.BaseResponse
import com.colasoft.opensearch.commons.utils.recreateObject

/**
 * All the transport action plugin interfaces for the Alerting plugin
 */
object AlertingPluginInterface {

    /**
     * Index monitor interface.
     * @param client Node client for making transport action
     * @param request The request object
     * @param namedWriteableRegistry Registry for building aggregations
     * @param listener The listener for getting response
     */
    fun indexMonitor(
        client: NodeClient,
        request: IndexMonitorRequest,
        namedWriteableRegistry: NamedWriteableRegistry,
        listener: ActionListener<IndexMonitorResponse>
    ) {
        client.execute(
            AlertingActions.INDEX_MONITOR_ACTION_TYPE,
            request,
            wrapActionListener(listener) { response ->
                recreateObject(response, namedWriteableRegistry) {
                    IndexMonitorResponse(
                        it
                    )
                }
            }
        )
    }
    fun deleteMonitor(
        client: NodeClient,
        request: DeleteMonitorRequest,
        listener: ActionListener<DeleteMonitorResponse>
    ) {
        client.execute(
            AlertingActions.DELETE_MONITOR_ACTION_TYPE,
            request,
            wrapActionListener(listener) { response ->
                recreateObject(response) {
                    DeleteMonitorResponse(
                        it
                    )
                }
            }
        )
    }

    /**
     * Get Alerts interface.
     * @param client Node client for making transport action
     * @param request The request object
     * @param listener The listener for getting response
     */
    fun getAlerts(
        client: NodeClient,
        request: GetAlertsRequest,
        listener: ActionListener<GetAlertsResponse>
    ) {
        client.execute(
            AlertingActions.GET_ALERTS_ACTION_TYPE,
            request,
            wrapActionListener(listener) { response ->
                recreateObject(response) {
                    GetAlertsResponse(
                        it
                    )
                }
            }
        )
    }

    /**
     * Get Findings interface.
     * @param client Node client for making transport action
     * @param request The request object
     * @param listener The listener for getting response
     */
    fun getFindings(
        client: NodeClient,
        request: GetFindingsRequest,
        listener: ActionListener<GetFindingsResponse>
    ) {
        client.execute(
            AlertingActions.GET_FINDINGS_ACTION_TYPE,
            request,
            wrapActionListener(listener) { response ->
                recreateObject(response) {
                    GetFindingsResponse(
                        it
                    )
                }
            }
        )
    }

    /**
     * Acknowledge Alerts interface.
     * @param client Node client for making transport action
     * @param request The request object
     * @param listener The listener for getting response
     */
    fun acknowledgeAlerts(
        client: NodeClient,
        request: AcknowledgeAlertRequest,
        listener: ActionListener<AcknowledgeAlertResponse>
    ) {
        client.execute(
            AlertingActions.ACKNOWLEDGE_ALERTS_ACTION_TYPE,
            request,
            wrapActionListener(listener) { response ->
                recreateObject(response) {
                    AcknowledgeAlertResponse(
                        it
                    )
                }
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun <Response : BaseResponse> wrapActionListener(
        listener: ActionListener<Response>,
        recreate: (Writeable) -> Response
    ): ActionListener<Response> {
        return object : ActionListener<ActionResponse> {
            override fun onResponse(response: ActionResponse) {
                val recreated = response as? Response ?: recreate(response)
                listener.onResponse(recreated)
            }

            override fun onFailure(exception: java.lang.Exception) {
                listener.onFailure(exception)
            }
        } as ActionListener<Response>
    }
}
