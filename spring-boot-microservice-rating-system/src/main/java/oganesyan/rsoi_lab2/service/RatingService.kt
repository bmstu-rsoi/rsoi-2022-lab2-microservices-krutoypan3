package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.RatingResponse
import oganesyan.rsoi_lab2.model.SetRatingRequest
import org.springframework.transaction.annotation.Transactional

interface RatingService {
    @Transactional(readOnly = true)
    fun getRatingByUsername(username: String): RatingResponse

    fun setRatingByUsername(setRatingRequest: SetRatingRequest)
}