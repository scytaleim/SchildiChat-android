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

package org.matrix.android.sdk.internal.session.sync

import org.matrix.android.sdk.api.session.room.model.tag.RoomTagContent
import org.matrix.android.sdk.internal.database.model.RoomSummaryEntity
import org.matrix.android.sdk.internal.database.model.RoomTagEntity
import org.matrix.android.sdk.internal.database.query.where
import io.realm.Realm
import javax.inject.Inject

internal class RoomTagHandler @Inject constructor() {

    fun handle(realm: Realm, roomId: String, content: RoomTagContent?) {
        if (content == null) {
            return
        }
        val tags = content.tags.entries.map { (tagName, params) ->
            RoomTagEntity(tagName, params["order"] as? Double)
        }
        val roomSummaryEntity = RoomSummaryEntity.where(realm, roomId).findFirst()
                                ?: RoomSummaryEntity(roomId)

        roomSummaryEntity.tags.clear()
        roomSummaryEntity.tags.addAll(tags)
        realm.insertOrUpdate(roomSummaryEntity)
    }
}