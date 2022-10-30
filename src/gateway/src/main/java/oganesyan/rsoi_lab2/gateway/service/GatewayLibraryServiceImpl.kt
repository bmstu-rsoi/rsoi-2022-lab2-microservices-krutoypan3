package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.error.ErrorBadRequest
import oganesyan.rsoi_lab2.gateway.error.ErrorNotFound
import oganesyan.rsoi_lab2.gateway.model.GatewayLibraryInfo
import oganesyan.rsoi_lab2.gateway.model.GatewayLibraryRequest
import oganesyan.rsoi_lab2.gateway.model.GatewayLibraryResponse
import org.json.JSONObject
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.ArrayList

@Transactional
@Service
open class GatewayLibraryServiceImpl(restTemplateBuilder: RestTemplateBuilder): GatewayLibraryService {

    private val restTemplate = restTemplateBuilder.build()

    override fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse {
        val url = UriComponentsBuilder.fromHttpUrl("http://localhost:8060/library-system/getLibraryByCity")
            .queryParam("page", libraryRequest.page)
            .queryParam("size", libraryRequest.size)
            .queryParam("city", libraryRequest.city)
            .toUriString()

        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)

        val restOperations: RestOperations = RestTemplate()
        val response: ResponseEntity<String> = try {
            restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String::class.java
            )
        } catch (e: HttpClientErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: HttpServerErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: RestClientException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        }
        if (response.statusCode == HttpStatus.NOT_FOUND) {
            throw ErrorNotFound(response.body?: "")
        }
        if (response.statusCode == HttpStatus.BAD_REQUEST) {
            throw ErrorBadRequest(response.body ?: "", ArrayList())
        }

        val obj = JSONObject(response.body)

        val totalElements = obj.getInt("totalElements")
        val librariesInfoJsonArray = obj.getJSONArray("items")

        val numCars: Int = librariesInfoJsonArray.length()
        val librariesInfo: ArrayList<GatewayLibraryInfo> = ArrayList(numCars)
        for (i in 0 until numCars) {
            val jsonLibrary: JSONObject = librariesInfoJsonArray.getJSONObject(i)
            val libraryInfo: GatewayLibraryInfo = parseGatewayLibraryInfo(jsonLibrary)
            librariesInfo.add(libraryInfo)
        }
        return GatewayLibraryResponse(libraryRequest.page, libraryRequest.size, totalElements, librariesInfo)
    }

    private fun parseGatewayLibraryInfo(obj: JSONObject): GatewayLibraryInfo {
        val libraryUid = obj.getString("libraryUid")
        val name = obj.getString("name")
        val address = obj.getString("address")
        val city = obj.getString("city")
        return GatewayLibraryInfo(libraryUid, name, address, city)
    }
}