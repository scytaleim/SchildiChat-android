/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.android.sdk.internal.database.model

import org.matrix.android.sdk.api.session.homeserver.HomeServerCapabilities
import io.realm.RealmObject

internal open class HomeServerCapabilitiesEntity(
        var canChangePassword: Boolean = true,
        var maxUploadFileSize: Long = HomeServerCapabilities.MAX_UPLOAD_FILE_SIZE_UNKNOWN,
        var lastVersionIdentityServerSupported: Boolean = false,
        var defaultIdentityServerUrl: String? = null,
        var adminE2EByDefault: Boolean = true,
        var lastUpdatedTimestamp: Long = 0L
) : RealmObject() {

    companion object
}