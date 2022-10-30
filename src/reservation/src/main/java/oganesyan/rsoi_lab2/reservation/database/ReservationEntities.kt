package oganesyan.rsoi_lab2.reservation.database

import javax.persistence.*

@Entity
@Table(name = "reservation")
class ReservationEntities {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
    @Column var reservation_uid: String? = null
    @Column var username: String? = null
    @Column var book_uid: String? = null
    @Column var library_uid: String? = null
    @Column var status: String? = null
    @Column var start_date: String? = null
    @Column var till_date: String? = null
}