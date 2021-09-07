package com.test.data.mappers

import com.test.data.model.EventRawItem
import javax.inject.Inject

class EventsItemValidator @Inject constructor() {

    fun isValid(item: EventRawItem): Boolean {
        return !item.id.isNullOrEmpty()
                && !item.title.isNullOrEmpty()
                && !item.utsTime.isNullOrEmpty()
                && !item.url.isNullOrEmpty()
    }
}