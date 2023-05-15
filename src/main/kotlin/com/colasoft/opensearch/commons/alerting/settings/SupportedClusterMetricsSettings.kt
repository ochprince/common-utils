package com.colasoft.opensearch.commons.alerting.settings

import com.colasoft.opensearch.commons.alerting.model.ClusterMetricsInput

interface SupportedClusterMetricsSettings {
    fun validateApiType(clusterMetricsInput: ClusterMetricsInput)
}
