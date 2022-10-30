package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.error.ErrorBadRequest
import oganesyan.rsoi_lab2.gateway.error.ErrorNotFound
import oganesyan.rsoi_lab2.gateway.model.*
import org.json.JSONObject
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.ArrayList

@Transactional
@Service
class GatewayLibraryServiceImpl: GatewayLibraryService {


    override fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse {
        val url = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/getLibraryByCity")
            .queryParam("page", libraryRequest.page)
            .queryParam("size", libraryRequest.size)
            .queryParam("city", libraryRequest.city)
            .toUriString()

        val obj = getObjByUrl(url)

        val totalElements = obj.getInt("totalElements")
        val librariesInfoJsonArray = obj.getJSONArray("items")

        val count: Int = librariesInfoJsonArray.length()
        val librariesInfo: ArrayList<GatewayLibraryInfo> = ArrayList(count)
        for (i in 0 until count) {
            val jsonLibrary: JSONObject = librariesInfoJsonArray.getJSONObject(i)
            val libraryInfo: GatewayLibraryInfo = parseGatewayLibraryInfo(jsonLibrary)
            librariesInfo.add(libraryInfo)
        }
        return GatewayLibraryResponse(libraryRequest.page, libraryRequest.size, totalElements, librariesInfo)
    }

    override fun getBooksByLibrary(gatewayBooksByLibraryRequest: GatewayBooksByLibraryRequest): GatewayBookResponse {
        val url = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/books/getBooksByLibrary")
            .queryParam("library_uid", gatewayBooksByLibraryRequest.library_uid)
            .queryParam("page", gatewayBooksByLibraryRequest.page)
            .queryParam("size", gatewayBooksByLibraryRequest.size)
            .queryParam("showAll", gatewayBooksByLibraryRequest.showAll)
            .toUriString()

        val obj = getObjByUrl(url)

        val totalElements = obj.getInt("totalElements")

        val booksInfoJsonArray = obj.getJSONArray("items")

        val count: Int = booksInfoJsonArray.length()
        val booksInfo: ArrayList<GatewayBookInfo> = ArrayList(count)
        for (i in 0 until count) {
            val jsonBook: JSONObject = booksInfoJsonArray.getJSONObject(i)
            val bookInfo: GatewayBookInfo = parseGatewayBookInfo(jsonBook, gatewayBooksByLibraryRequest.library_uid?: "")
            booksInfo.add(bookInfo)
        }
        return GatewayBookResponse(gatewayBooksByLibraryRequest.page, gatewayBooksByLibraryRequest.size, totalElements, booksInfo)
    }

    override fun getRating(username: String): GatewayRatingResponse {
        val url = UriComponentsBuilder.fromHttpUrl("http://rating:8050/rating-system/getRatingByUsername")
            .queryParam("username", username)
            .toUriString()

        val obj = getObjByUrl(url)

        return GatewayRatingResponse(username, obj.getInt("stars"))
    }

    private fun parseGatewayBookInfo(obj: JSONObject, libraryUid: String): GatewayBookInfo {
        val bookUid = obj.getString("bookUid")
        val name = obj.getString("name")
        val author = obj.getString("author")
        val genre = obj.getString("genre")
        val condition = obj.getString("condition")

        val url = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/library-books/getAvailableCountByBookUidAndLibraryUid")
            .queryParam("library_uid", libraryUid)
            .queryParam("book_uid", bookUid)
            .toUriString()
        val obj2 = getObjByUrl(url)
        println("\n$obj2\n")
        val availableCount = obj2.getString("available_count")
        return GatewayBookInfo(bookUid, name, author, genre, condition, availableCount.toLong())
    }

    private fun parseGatewayLibraryInfo(obj: JSONObject): GatewayLibraryInfo {
        val libraryUid = obj.getString("libraryUid")
        val name = obj.getString("name")
        val address = obj.getString("address")
        val city = obj.getString("city")
        return GatewayLibraryInfo(libraryUid, name, address, city)
    }

    private fun getObjByUrl(url: String): JSONObject{
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
        return JSONObject(response.body)
    }
}