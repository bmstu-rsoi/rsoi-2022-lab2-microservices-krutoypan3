package oganesyan.rsoi_lab2

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.model.GatewayLibraryRequest
import oganesyan.rsoi_lab2.model.GatewayLibraryResponse
import oganesyan.rsoi_lab2.service.GatewayLibraryService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "library_system_controller")
@RestController
@RequestMapping("/api/v1")
class GatewayController(private val gatewayLibraryService: GatewayLibraryService) {

    @Operation(
        summary = "get_library_by_city",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Library by city",
                content = [Content(schema = Schema(implementation = GatewayLibraryResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found library for city"
            ),
        ]
    )
    @GetMapping("/libraries", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLibraryByCity(
        @RequestParam("city") city: String?, @RequestParam("page") page: Int?, @RequestParam("size") size: Int?,
    ) = gatewayLibraryService.getLibraryByCity(GatewayLibraryRequest(page, size, city))
}