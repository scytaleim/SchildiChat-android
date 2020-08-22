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
package org.matrix.android.sdk.internal.session.pushers

import org.matrix.android.sdk.api.pushrules.RuleKind
import org.matrix.android.sdk.api.pushrules.rest.PushRule
import org.matrix.android.sdk.internal.network.executeRequest
import org.matrix.android.sdk.internal.task.Task
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

internal interface RemovePushRuleTask : Task<RemovePushRuleTask.Params, Unit> {
    data class Params(
            val kind: RuleKind,
            val pushRule: PushRule
    )
}

internal class DefaultRemovePushRuleTask @Inject constructor(
        private val pushRulesApi: PushRulesApi,
        private val eventBus: EventBus
) : RemovePushRuleTask {

    override suspend fun execute(params: RemovePushRuleTask.Params) {
        return executeRequest(eventBus) {
            apiCall = pushRulesApi.deleteRule(params.kind.value, params.pushRule.ruleId)
        }
    }
}