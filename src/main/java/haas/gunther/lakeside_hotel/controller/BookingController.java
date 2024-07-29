package haas.gunther.lakeside_hotel.controller;

import haas.gunther.lakeside_hotel.exception.InvalidBookingRequestException;
import haas.gunther.lakeside_hotel.exception.ResourceNotFoundException;
import haas.gunther.lakeside_hotel.model.BookedRoom;
import haas.gunther.lakeside_hotel.response.BookingResponse;
import haas.gunther.lakeside_hotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();

        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }

        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confimation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try{
         BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
         BookingResponse bookingResponse = getBookingResponse(booking);
         return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException exception) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest) {
        try {
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room booked successfully. Your booking confirmation code is: " + confirmationCode);

        }catch (InvalidBookingRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
