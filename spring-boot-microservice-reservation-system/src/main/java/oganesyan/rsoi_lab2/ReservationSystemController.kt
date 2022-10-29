package oganesyan.rsoi_lab2

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.model.ReservationByUsernameItemResponse
import oganesyan.rsoi_lab2.model.library.LibraryRequest
import oganesyan.rsoi_lab2.model.library.LibraryResponse
import oganesyan.rsoi_lab2.service.ReservationService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@Tag(name = "library_system_controller")
@RestController
@RequestMapping("/reservation-system")
class ReservationSystemController(private val reservationService: ReservationService) {

    @Operation(
        summary = "get_reservations_by_username",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "reservations by username",
                content = [Content(schema = Schema(implementation = ReservationByUsernameItemResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found reservations for username"
            ),
        ]
    )
    @GetMapping("/getReservationsByUsername", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getReservationsByUsername(@RequestParam("username") username: String)
    = reservationService.getReservationsByUsername(username)

}