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

package org.matrix.android.sdk.api.permalinks

import android.text.Spannable

/**
 *  MatrixLinkify take a piece of text and turns all of the
 *  matrix patterns matches in the text into clickable links.
 */
object MatrixLinkify {

    /**
     * Find the matrix spans i.e matrix id , user id ... to display them as URL.
     *
     * @param spannable the text in which the matrix items has to be clickable.
     */
    @Suppress("UNUSED_PARAMETER")
    fun addLinks(spannable: Spannable, callback: MatrixPermalinkSpan.Callback?): Boolean {
        /**
         * I disable it because it mess up with pills, and even with pills, it does not work correctly:
         * The url is not correct. Ex: for @user:matrix.org, the url will be @user:matrix.org, instead of a matrix.to
         */
        /*
        // sanity checks
        if (spannable.isEmpty()) {
            return false
        }
        val text = spannable.toString()
        var hasMatch = false
        for (pattern in MatrixPatterns.MATRIX_PATTERNS) {
            for (match in pattern.findAll(spannable)) {
                hasMatch = true
                val startPos = match.range.first
                if (startPos == 0 || text[startPos - 1] != '/') {
                    val endPos = match.range.last + 1
                    val url = text.substring(match.range)
                    val span = MatrixPermalinkSpan(url, callback)
                    spannable.setSpan(span, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return hasMatch
         */
        return false
    }
}