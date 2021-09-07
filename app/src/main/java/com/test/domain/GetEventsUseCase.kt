package com.test.domain

import com.test.data.repository.EventsRepository
import com.test.domain.mapper.EventsMapper
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: EventsRepository,
    private val mapper: EventsMapper,
) {

    operator fun invoke() = mapper.map(repository.getEvents())
}