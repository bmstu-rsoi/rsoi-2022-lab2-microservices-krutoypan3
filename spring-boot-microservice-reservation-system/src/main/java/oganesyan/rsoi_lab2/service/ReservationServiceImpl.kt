package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.database.ReservationEntities
import oganesyan.rsoi_lab2.model.*
import oganesyan.rsoi_lab2.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

@Transactional
@Service
open class ReservationServiceImpl@Autowired constructor(
    private val entityManager: EntityManager,
    private val reservationRepository: ReservationRepository,
    restTemplateBuilder: RestTemplateBuilder,): ReservationService {
    private val restTemplate = restTemplateBuilder.build()
    override fun getReservationsByUsername(username: String): ReservationByUsernameItemResponse {
        return entitiesToResponse(reservationRepository.findAllByUsername(username))
    }

    override fun putReservation(request: CreateReservationRequest) {

        // TODO Тут нужно проверять кол-во уже взятых книг
        val reservations = getReservationsByUsername(request.username).reservations

        // TODO Тут нужно рассчитывать максимальное кол-во книг, которое может взять пользователь
        // Здесь отправляем запрос на получение рейтинга пользователя по username
        val url = "http://localhost:8050/rating-system/getRatingByUsername?username=${request.username}"
        val stars = restTemplate.getForObject(url, ReservationRatingResponse::class.java)?.stars ?: 30
        val maxBooksCount = stars / 10
        if (reservations.size < maxBooksCount) {
            val saveEntity = ReservationEntities()

            // TODO Нужно узнать, есть ли книга в библиотеке
            val url2 = "http://localhost:8070/library-system/library-books/getAvailableCountByBookUidAndLibraryUid?book_uid=${request.bookUid}&library_uid=${request.libraryUid}"
            val availableCount = restTemplate.getForObject(url, ReservationLibraryBookInfo::class.java)?.available_count
            if (availableCount != null) {
                if (availableCount > 0) {
                    val entity = ReservationEntities()
                    entity.book_uid = request.bookUid
                    entity.library_uid = request.libraryUid
                    entity.username = request.username
                    entity.reservation_uid = UUID.randomUUID().toString()
                    entity.start_date = Date().time.toString()
                    entity.till_date = Date.parse(request.tillDate).toString()

                    // TODO Тут еще нужно получить статус книги по ее UID

                    reservationRepository.save(entity)
                }
            }

        }
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