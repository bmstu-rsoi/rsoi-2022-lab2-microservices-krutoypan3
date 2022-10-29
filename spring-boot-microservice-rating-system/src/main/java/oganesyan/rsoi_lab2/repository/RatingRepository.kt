package oganesyan.rsoi_lab2.repository

import oganesyan.rsoi_lab2.database.RatingEntities
import oganesyan.rsoi_lab2.model.RatingResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface RatingRepository: JpaRepository<RatingEntities, Int> {
    fun getFirstByUsername(@Param("username") username: String): RatingEntities?
    fun deleteByUsername(@Param("username") username: String)
}