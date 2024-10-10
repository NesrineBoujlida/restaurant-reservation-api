package system.java.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import system.java.reservation.client.RestaurantClient;
import system.java.reservation.dto.ReservationDTO;
import system.java.reservation.mapper.ReservationMapper;
import system.java.reservation.model.Reservation;
import system.java.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantClient restaurantClient;

    @Autowired
    private ReservationMapper reservationMapper;

    @Operation(summary = "Create a new reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation created successfully."),
            @ApiResponse(responseCode = "400", description = "Restaurant is not available for the requested guests.")
    })
    @PostMapping
    public EntityModel<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        boolean isAvailable = restaurantClient.checkAvailability(reservationDTO.getRestaurantId());
        if (isAvailable) {
            Reservation createdReservation = reservationService.createReservation(reservationMapper.toEntity(reservationDTO));
            return toHateoasEntityModel(reservationMapper.toDTO(createdReservation));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant is not available for the requested guests.");
        }
    }

    @Operation(summary = "Retrieve all reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reservations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No reservations found")
    })
    @GetMapping
    public List<EntityModel<ReservationDTO>> getAllReservations(@PageableDefault(size = 10) Pageable pageable) {
        Page<Reservation> reservationPage = reservationService.getAllReservations(pageable);
        return reservationPage.stream()
                .map(reservation -> toHateoasEntityModel(reservationMapper.toDTO(reservation)))
                .toList();
    }

    @Operation(summary = "Cancel a reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation canceled successfully."),
            @ApiResponse(responseCode = "404", description = "Reservation not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        boolean success = reservationService.cancelReservation(id);
        if (success) {
            return ResponseEntity.ok("Reservation canceled successfully.");
        } else {
            return ResponseEntity.status(404).body("Reservation not found.");
        }
    }

    @Operation(summary = "Retrieve a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Reservation not found.")
    })
    @GetMapping("/{id}")
    public EntityModel<ReservationDTO> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return toHateoasEntityModel(reservationMapper.toDTO(reservation));
    }

    private EntityModel<ReservationDTO> toHateoasEntityModel(ReservationDTO reservationDTO) {
        Link selfLink = linkTo(methodOn(ReservationController.class).getReservation(reservationDTO.getId())).withSelfRel();
        Link allReservationsLink = linkTo(methodOn(ReservationController.class).getAllReservations(Pageable.unpaged())).withRel("all-reservations");
        return EntityModel.of(reservationDTO, selfLink, allReservationsLink);
    }
}
