package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.CreateReservationRequest
import oganesyan.rsoi_lab2.model.ReservationByUsernameItemResponse
import org.springframework.transaction.annotation.Transactional

interface ReservationService {
    @Transactional(readOnly = true)
    fun getReservationsByUsername(username: String): ReservationByUsernameItemResponse

    fun putReservation(request: CreateReservationRequest)
}