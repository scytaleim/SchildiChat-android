/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.android.sdk.internal.session.widgets

import com.zhuinden.monarchy.Monarchy
import org.matrix.android.sdk.api.session.events.model.Content
import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.internal.database.awaitNotEmptyResult
import org.matrix.android.sdk.internal.database.model.CurrentStateEventEntity
import org.matrix.android.sdk.internal.database.model.CurrentStateEventEntityFields
import org.matrix.android.sdk.internal.database.query.whereStateKey
import org.matrix.android.sdk.internal.di.SessionDatabase
import org.matrix.android.sdk.internal.di.UserId
import org.matrix.android.sdk.internal.network.executeRequest
import org.matrix.android.sdk.internal.session.room.RoomAPI
import org.matrix.android.sdk.internal.task.Task
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

internal interface CreateWidgetTask : Task<CreateWidgetTask.Params, Unit> {

    data class Params(
            val roomId: String,
            val widgetId: String,
            val content: Content
    )
}

internal class DefaultCreateWidgetTask @Inject constructor(@SessionDatabase private val monarchy: Monarchy,
                                                           private val roomAPI: RoomAPI,
                                                           @UserId private val userId: String,
                                                           private val eventBus: EventBus) : CreateWidgetTask {

    override suspend fun execute(params: CreateWidgetTask.Params) {
        executeRequest<Unit>(eventBus) {
            apiCall = roomAPI.sendStateEvent(
                    roomId = params.roomId,
                    stateEventType = EventType.STATE_ROOM_WIDGET_LEGACY,
                    stateKey = params.widgetId,
                    params = params.content
            )
        }
        awaitNotEmptyResult(monarchy.realmConfiguration, 30_000L) {
            CurrentStateEventEntity
                    .whereStateKey(it, params.roomId, type = EventType.STATE_ROOM_WIDGET_LEGACY, stateKey = params.widgetId)
                    .and()
                    .equalTo(CurrentStateEventEntityFields.ROOT.SENDER, userId)
        }
    }
}