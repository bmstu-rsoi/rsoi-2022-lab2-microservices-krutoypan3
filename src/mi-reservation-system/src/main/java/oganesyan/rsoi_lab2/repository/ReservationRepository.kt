package oganesyan.rsoi_lab2.repository

import oganesyan.rsoi_lab2.database.ReservationEntities
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface ReservationRepository : JpaRepository<ReservationEntities, Int> {
    fun findAllByUsername(@Param("username") username: String): List<ReservationEntities>
}