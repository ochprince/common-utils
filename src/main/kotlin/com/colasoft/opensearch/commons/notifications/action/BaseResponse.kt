/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.colasoft.opensearch.commons.notifications.action

import com.colasoft.opensearch.action.ActionResponse
import com.colasoft.opensearch.common.io.stream.StreamInput
import com.colasoft.opensearch.core.xcontent.ToXContentObject
import com.colasoft.opensearch.rest.RestStatus
import java.io.IOException

/**
 * Base response which give REST status.
 */
abstract class BaseResponse : ActionResponse, ToXContentObject {

    /**
     * constructor for creating the class
     */
    constructor()

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class)
    constructor(input: StreamInput) : super(input)

    /**
     * get rest status for the response. Useful override for multi-status response.
     * @return RestStatus for the response
     */
    open fun getStatus(): RestStatus {
        return RestStatus.OK
    }
}
