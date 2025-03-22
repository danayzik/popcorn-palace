package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PopcornPalaceApplicationTests {

	@LocalServerPort
	private int port;

	private final MockMvc mockMvc;


	private final ObjectMapper objectMapper = new ObjectMapper();


	@Autowired PopcornPalaceApplicationTests(MockMvc mockMvc){
		this.mockMvc = mockMvc;
	}



	public String getMovieJsonBody(String title, String genre, int duration, double rating, int releaseYear){
		return "{ \"title\": \"" + title + "\", \"genre\": \"" + genre + "\", \"duration\": " + duration + ", \"rating\": " + rating + ", \"releaseYear\": " + releaseYear + " }";
	}
	public String getShowtimeJsonBody(long movieId, double price, String theater, String startTime, String endTime) {
		return "{ \"movieId\": " + movieId +
				", \"price\": " + price +
				", \"theater\": \"" + theater + "\"" +
				", \"startTime\": \"" + startTime + "\"" +
				", \"endTime\": \"" + endTime + "\"" +
				" }";
	}
	public String getBookingJsonBody(long showtimeId, int seatNumber, String userId){
		return "{ \"showtimeId\": " + showtimeId + ", \"seatNumber\": " + seatNumber + ", \"userId\": \"" + userId + "\" }";

	}

	public MvcResult addMovie(String title, String genre, int duration, double rating, int releaseYear) throws Exception {
		String jsonBody = getMovieJsonBody(title, genre, duration, rating, releaseYear);

		return mockMvc.perform(post("/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)).andReturn();
	}

	public MvcResult updateMovie(String title, String genre, int duration, double rating, int releaseYear, String oldTitle) throws Exception {
		String jsonBody = getMovieJsonBody(title, genre, duration, rating, releaseYear);

		return mockMvc.perform(post(String.format("/movies/update/%s", oldTitle))
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)).andReturn();
	}

	public MvcResult addBooking(long showtimeId, int seatNumber, String userId) throws Exception {
		String jsonBody = getBookingJsonBody(showtimeId, seatNumber, userId);

		return mockMvc.perform(post("/bookings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)).andReturn();
	}

	public MvcResult updateShowtime(long movieId, double price, String theater, String startTime, String endTime, long showtimeId) throws Exception {
		String jsonBody = getShowtimeJsonBody(movieId, price, theater, startTime, endTime);

		return mockMvc.perform(post(String.format("/showtimes/update/%d", showtimeId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)).andReturn();
	}

	public MvcResult addShowtime(long movieId, double price, String theater, String startTime, String endTime) throws Exception {
		String jsonBody = getShowtimeJsonBody(movieId, price, theater, startTime, endTime);

		return mockMvc.perform(post("/showtimes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)).andReturn();
	}

	public void getShowtimeAndCompare(long movieId, double price, String theater, String startTime, String endTime, long showtimeId) throws Exception {
		MvcResult result = mockMvc.perform(get(String.format("/showtimes/%d", showtimeId)))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
		});
		assertThat(responseMap.get("movieId")).isEqualTo(String.valueOf(movieId));
		assertThat(responseMap.get("price")).isEqualTo(String.valueOf(price));
		assertThat(responseMap.get("theater")).isEqualTo(theater);
		assertThat(responseMap.get("startTime")).isEqualTo(startTime);
		assertThat(responseMap.get("endTime")).isEqualTo(endTime);
	}

	@Test
	@Order(1)
	@Transactional
	@DirtiesContext
	void testAddMovieAndGetAllMovies() throws Exception {
		int n = 3;
		for (int i = 0; i < n; i++) {
			MvcResult result = addMovie(String.format("Movie %d", i), "Action", 120, 8.7, 2025);
			int status = result.getResponse().getStatus();
			assertThat(status).isEqualTo(201);
		}
		MvcResult result = mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		List<Movie> movieList = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
		assertThat(movieList).hasSize(3);
		for (int i = 0; i < n; i++) {
			Movie movie = movieList.get(i);
			assertThat(movie.getTitle()).isEqualTo(String.format("Movie %d", i));
			assertThat(movie.getGenre()).isEqualTo("Action");
			assertThat(movie.getDuration()).isEqualTo(120);
			assertThat(movie.getRating()).isEqualTo(8.7);
			assertThat(movie.getReleaseYear()).isEqualTo(2025);
		}
	}

	@Test
	@Order(2)
	@Transactional
	@DirtiesContext
	public void testAddShowtimes() throws Exception {
		int n = 3;
		for (int i = 0; i < n; i++) {
			MvcResult result = addMovie(String.format("Movie %d", i), "Action", 120, 8.7, 2025);
			int status = result.getResponse().getStatus();
			assertThat(status).isEqualTo(201);
		}
		int status;
		MvcResult result1 = addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		MvcResult result2 = addShowtime(2, 15.0, "Theater 1", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z");
		MvcResult result3 = addShowtime(3, 25.0, "Theater 2", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z");

		status = result1.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		status = result2.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		status = result3.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		getShowtimeAndCompare(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z",1);
		getShowtimeAndCompare(2, 15.0, "Theater 1", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z",2);
		getShowtimeAndCompare(3, 25.0, "Theater 2", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z", 3);
	}

	@Test
	@Order(3)
	@Transactional
	@DirtiesContext
	public void testUpdateShowtime() throws Exception{
		int n = 3;
		for (int i = 0; i < n; i++) {
			MvcResult result = addMovie(String.format("Movie %d", i), "Action", 120, 8.7, 2025);
			int status = result.getResponse().getStatus();
			assertThat(status).isEqualTo(201);
		}

		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		addShowtime(2, 15.0, "Theater 1", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z");
		addShowtime(3, 25.0, "Theater 2", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z");

		MvcResult updateResult = updateShowtime(3, 5.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z", 1);
		int status = updateResult.getResponse().getStatus();
		assertThat(status).isEqualTo(200);
		getShowtimeAndCompare(3, 5.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z",1);
		getShowtimeAndCompare(2, 15.0, "Theater 1", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z",2);
		getShowtimeAndCompare(3, 25.0, "Theater 2", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z", 3);
	}

	@Test
	@Order(4)
	@Transactional
	@DirtiesContext
	public void testAddBooking() throws Exception{
		int n = 3;
		for (int i = 0; i < 1; i++) {
			MvcResult result = addMovie(String.format("Movie %d", i), "Action", 120, 8.7, 2025);
			int status = result.getResponse().getStatus();
			assertThat(status).isEqualTo(201);
		}

		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		addShowtime(1, 15.0, "Theater 1", "2025-02-14T14:00:00.125405Z", "2025-02-14T16:00:00.125405Z");
		int status;
		MvcResult result1 = addBooking(1, 15, "84438967-f68f-4fa0-b620-0f08217e76af");
		MvcResult result2 = addBooking(2, 16, "84438967-f68f-4fa0-b620-0f08217e76af");
		MvcResult result3 = addBooking(2, 15, "84438967-f68f-4fa0-b620-0f08217e76af");

		status = result1.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		status = result2.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		status = result3.getResponse().getStatus();
		assertThat(status).isEqualTo(201);

		String responseBody = result1.getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
		});
		assertThat(responseMap.containsKey("bookingId")).isTrue();
		assertThat(responseMap.size()).isEqualTo(1);


	}

	@Test
	@Order(5)
	@Transactional
	@DirtiesContext
	void testUpdateMovie() throws Exception {
		int n = 3;
		for (int i = 0; i < n; i++) {
			MvcResult result = addMovie(String.format("Movie %d", i), "Action", 120, 8.7, 2025);
			int status = result.getResponse().getStatus();
			assertThat(status).isEqualTo(201);
		}
		MvcResult updateResult = updateMovie("newTitle", "comedy", 115, 6.1, 2020, "Movie 0");
		assertThat(updateResult.getResponse().getStatus()).isEqualTo(200);
		MvcResult result = mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		List<Movie> movieList = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
		assertThat(movieList).hasSize(3);
		Movie movie = movieList.get(0);
		assertThat(movie.getTitle()).isEqualTo("newTitle");
		assertThat(movie.getGenre()).isEqualTo("comedy");
		assertThat(movie.getDuration()).isEqualTo(115);
		assertThat(movie.getRating()).isEqualTo(6.1);
		assertThat(movie.getReleaseYear()).isEqualTo(2020);
	}

	@Test
	@Order(6)
	@Transactional
	@DirtiesContext
	void deleteMovie() throws Exception {
		int n = 3;
		for (int i = 0; i < n; i++) {
			MvcResult result = addMovie(String.format("Movie %d", i), "Action", 120, 8.7, 2025);
			int status = result.getResponse().getStatus();
			assertThat(status).isEqualTo(201);
		}
		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		mockMvc.perform(delete("/movies/Movie 0"))
				.andExpect(status().isOk());
		mockMvc.perform(get(String.format("/showtimes/%d", 1))) //Check cascading delete
				.andExpect(status().isNotFound());

		MvcResult result = mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		List<Movie> movieList = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
		assertThat(movieList).hasSize(2);

	}

	@Test
	@Order(7)
	@Transactional
	@DirtiesContext
	void testInvalidAddMovie() throws Exception {

		MvcResult result = addMovie("Movie", "Action", -120, 8.7, 2025);
		int status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		result = addMovie("Movie", "Action", 120, 18.7, 2025);
		status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		result = addMovie("Movie", "Action", 120, 8.7, -2025);
		status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		result = addMovie("", "Action", 120, 18.7, 2025);
		status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		result = mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		List<Movie> movieList = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
		assertThat(movieList).hasSize(0);
	}

	@Test
	@Order(8)
	@Transactional
	@DirtiesContext
	void deleteNonExisting() throws Exception {

		mockMvc.perform(delete("/movies/Movie 0"))
				.andExpect(status().isNotFound());
		mockMvc.perform(delete("/showtimes/2"))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(9)
	@Transactional
	@DirtiesContext
	void addShowtimeToUnexistingMovie() throws Exception {

		MvcResult result = addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		assertThat(result.getResponse().getStatus()).isEqualTo(400);
	}

	@Test
	@Order(10)
	@Transactional
	@DirtiesContext
	void failUpdateShowtime() throws Exception {


		MvcResult result = addMovie("Movie", "Action", 120, 8.7, 2025);
		int status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(201);


		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");

		MvcResult updateResult = updateShowtime(2, 5.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z", 1);
		status = updateResult.getResponse().getStatus();
		assertThat(status).isEqualTo(404);
		updateResult = updateShowtime(1, -5.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z", 1);
		status = updateResult.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		updateResult = updateShowtime(1, 5.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T08:00:00.125405Z", 1);
		status = updateResult.getResponse().getStatus();
		assertThat(status).isEqualTo(400);


	}

	@Test
	@Order(11)
	@Transactional
	@DirtiesContext
	void createShowtimeWithOverlap() throws Exception {

		MvcResult result = addMovie("Movie", "Action", 120, 8.7, 2025);
		int status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(201);

		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		MvcResult creationResult = addShowtime(1, 15.0, "Theater 1", "2025-02-14T09:00:00.125405Z", "2025-02-14T10:15:00.125405Z");
		status = creationResult.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		creationResult = addShowtime(1, 15.0, "Theater 1", "2025-02-14T11:45:00.125405Z", "2025-02-14T15:15:00.125405Z");
		status = creationResult.getResponse().getStatus();
		assertThat(status).isEqualTo(400);

	}

	@Test
	@Order(12)
	@Transactional
	@DirtiesContext
	void updateShowtimeWithOverlap() throws Exception {

		MvcResult result = addMovie("Movie", "Action", 120, 8.7, 2025);
		int status = result.getResponse().getStatus();
		assertThat(status).isEqualTo(201);

		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		addShowtime(1, 20.0, "Theater 2", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		MvcResult updateResult = updateShowtime(1, 20.0, "Theater 1", "2025-02-14T9:00:00.125405Z", "2025-02-14T12:00:00.125405Z", 2);
		status = updateResult.getResponse().getStatus();
		assertThat(status).isEqualTo(400);
		getShowtimeAndCompare(1, 20.0, "Theater 2", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z", 2);

	}

	@Test
	@Order(13)
	@Transactional
	@DirtiesContext
	void doubleBookSeats() throws Exception {

		MvcResult result = addMovie("Movie", "Action", 120, 8.7, 2025);

		addShowtime(1, 20.0, "Theater 1", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");
		addShowtime(1, 20.0, "Theater 2", "2025-02-14T10:00:00.125405Z", "2025-02-14T12:00:00.125405Z");

		int status;
		MvcResult result1 = addBooking(1, 15, "84438967-f68f-4fa0-b620-0f08217e76af");
		MvcResult result2 = addBooking(2, 15, "84438967-f68f-4fa0-b620-0f08217e76af");
		MvcResult result3 = addBooking(2, 15, "84438967-f68f-4fa0-b620-0f08217e76af");

		status = result1.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		status = result2.getResponse().getStatus();
		assertThat(status).isEqualTo(201);
		status = result3.getResponse().getStatus();
		assertThat(status).isEqualTo(400);


	}








}
