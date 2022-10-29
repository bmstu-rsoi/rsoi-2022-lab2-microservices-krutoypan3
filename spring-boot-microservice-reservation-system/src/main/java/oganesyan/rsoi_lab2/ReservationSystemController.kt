package oganesyan.rsoi_lab2

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.model.CreateReservationRequest
import oganesyan.rsoi_lab2.model.ReservationByUsernameItemResponse
import oganesyan.rsoi_lab2.service.ReservationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid


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



    @Operation(
        summary = "put_reservation", responses = [
            ApiResponse(
                responseCode = "201",
                description = "Created new reservation",
                headers = [Header(name = "Location", description = "Path to new Reservation")]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid data"
            ),
            ApiResponse(
                responseCode = "501", description = "Сервер так смешно делает пых-пых"
            )
        ]
    )
    @PostMapping("/putReservation", consumes = [MediaType.APPLICATION_JSON_VALUE])
    private fun putReservation(@Valid @RequestBody request: CreateReservationRequest): ResponseEntity<Void> {
        reservationService.putReservation(request)
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).build()
    }
}