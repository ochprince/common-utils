/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.colasoft.opensearch.commons.notifications.model

import com.colasoft.opensearch.common.io.stream.Writeable
import com.colasoft.opensearch.core.xcontent.ToXContentObject

/**
 * interface for representing objects.
 */
interface BaseModel : Writeable, ToXContentObject
