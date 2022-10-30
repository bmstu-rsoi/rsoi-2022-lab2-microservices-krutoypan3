package oganesyan.rsoi_lab2.model

data class CreateReservationRequest(
    var username: String,
    var bookUid: String,
    var libraryUid: String,
    var tillDate: String,
)