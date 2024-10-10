package system.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import system.java.reservation.client.RestaurantClient;
import system.java.reservation.controller.ReservationController;
import system.java.reservation.dto.ReservationDTO;
import system.java.reservation.mapper.ReservationMapper;
import system.java.reservation.model.Reservation;
import system.java.reservation.service.ReservationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReservationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private RestaurantClient restaurantClient;

	@Mock
	private ReservationMapper reservationMapper;

	@InjectMocks
	private ReservationController reservationController;

	private Reservation testReservation;
	private ReservationDTO reservationDTO;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);

		testReservation = new Reservation();
		testReservation.setRestaurantId(1L);
		testReservation.setGuestId(1L);
		testReservation.setReservationTime(LocalDateTime.of(2024, 10, 15, 19, 0));
		testReservation = reservationService.createReservation(testReservation); // Save to persist and get ID

		reservationDTO = new ReservationDTO();
		reservationDTO.setRestaurantId(1L);
		reservationDTO.setGuestId(1L);
		reservationDTO.setReservationTime(LocalDateTime.of(2024, 10, 15, 19, 0)); // Ensure reservation time is set
	}

	@Test
	void testCreateReservation_Success() throws Exception {
		when(restaurantClient.checkAvailability(reservationDTO.getRestaurantId())).thenReturn(true);

		ResultActions resultActions = mockMvc.perform(post("/reservations")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reservationDTO)))
				.andExpect(status().isOk());

		ReservationDTO reservationResponseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ReservationDTO.class);

		assertNotNull(reservationResponseDto);
		assertEquals(1, reservationResponseDto.getRestaurantId());
	}

	@Test
	public void getAllReservationsSuccess() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/reservations")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		List<Reservation> reservations = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<List<Reservation>>() {});

		assertNotNull(reservations);
		assertFalse(reservations.isEmpty());
	}

	@Test
	public void cancelReservationSuccess() throws Exception {
		mockMvc.perform(delete("/reservations/" + testReservation.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("Reservation canceled successfully."));
	}

	@Test
	public void getReservationSuccess() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/reservations/" + testReservation.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Reservation retrievedReservation = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Reservation.class);

		assertNotNull(retrievedReservation);
		assertEquals(testReservation.getId(), retrievedReservation.getId());
		assertEquals(testReservation.getRestaurantId(), retrievedReservation.getRestaurantId());
		assertEquals(testReservation.getGuestId(), retrievedReservation.getGuestId());
		assertEquals(testReservation.getReservationTime(), retrievedReservation.getReservationTime());
	}
}
