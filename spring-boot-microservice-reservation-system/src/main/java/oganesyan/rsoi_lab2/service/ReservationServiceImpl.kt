package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.database.ReservationEntities
import oganesyan.rsoi_lab2.model.ReservationByUsernameItem
import oganesyan.rsoi_lab2.model.ReservationByUsernameItemResponse
import oganesyan.rsoi_lab2.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Transactional
@Service
open class ReservationServiceImpl@Autowired constructor(
    private val entityManager: EntityManager,
    private val reservationRepository: ReservationRepository): ReservationService {

    override fun getReservationsByUsername(username: String): ReservationByUsernameItemResponse {
        return entitiesToResponse(reservationRepository.findAllByUsername(username))
    }

    private fun entitiesToResponse(entities: List<ReservationEntities>): ReservationByUsernameItemResponse {
        val list = ReservationByUsernameItemResponse(arrayListOf())
        entities.forEach {
            list.reservations.add(ReservationByUsernameItem(
                it.reservation_uid,
                it.username,
                it.book_uid,
                it.library_uid,
                it.status,
                it.start_date,
                it.till_date
            )
            )
        }
        return list
    }
}