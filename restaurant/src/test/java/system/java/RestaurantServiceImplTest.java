package system.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import system.java.restaurant.model.Restaurant;
import system.java.restaurant.repository.RestaurantRepository;
import system.java.restaurant.serviceImpl.RestaurantServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private RestaurantServiceImpl restaurantService;

	private Restaurant testRestaurant;

	@BeforeEach
	public void setUp() {
		testRestaurant = new Restaurant();
		testRestaurant.setId(1L);
		testRestaurant.setName("Test Restaurant");
	}

	@Test
	public void testAddRestaurant() {
		when(restaurantRepository.save(testRestaurant)).thenReturn(testRestaurant);

		Restaurant result = restaurantService.addRestaurant(testRestaurant);

		assertEquals(testRestaurant, result);
		verify(restaurantRepository).save(testRestaurant);
	}

	@Test
	public void testGetAllRestaurants() {
		Pageable pageable = Pageable.ofSize(10);
		Page<Restaurant> mockPage = mock(Page.class);
		when(restaurantRepository.findAll(pageable)).thenReturn(mockPage);

		Page<Restaurant> result = restaurantService.getAllRestaurants(pageable);

		assertEquals(mockPage, result);
		verify(restaurantRepository).findAll(pageable);
	}

	@Test
	public void testGetRestaurantById_Success() {
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));

		Restaurant result = restaurantService.getRestaurantById(1L);

		assertEquals(testRestaurant, result);
		verify(restaurantRepository).findById(1L);
	}

	@Test
	public void testGetRestaurantById_NotFound() {
		lenient().when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			restaurantService.getRestaurantById(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
		assertEquals("Restaurant not found", exception.getReason());
	}

	@Test
	public void testDeleteRestaurant() {
		restaurantService.deleteRestaurant(1L);
		verify(restaurantRepository).deleteById(1L);
	}

	@Test
	public void testIsAvailable() {
		boolean result = restaurantService.isAvailable(1L);
		assertTrue(result);
	}
}
