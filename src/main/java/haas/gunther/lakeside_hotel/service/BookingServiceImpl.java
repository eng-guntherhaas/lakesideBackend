package haas.gunther.lakeside_hotel.service;

import haas.gunther.lakeside_hotel.exception.InvalidBookingRequestException;
import haas.gunther.lakeside_hotel.model.BookedRoom;
import haas.gunther.lakeside_hotel.model.Room;
import haas.gunther.lakeside_hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate())) {
            throw new InvalidBookingRequestException("Check-out date must come after check-in date");
        }
        Room room = roomService.getRoomById(roomId).orElseThrow();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable) {
            room.addBooking(bookingRequest);
            try {
                bookingRepository.save(bookingRequest);
            } catch (Exception e) {
                // Handle database errors
                throw new RuntimeException("Error saving booking", e);
            }
        } else {
            throw new InvalidBookingRequestException("Room is not available in the selected date");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        try {
            bookingRepository.deleteById(bookingId);
        } catch (Exception e) {
            throw new RuntimeException("Error canceling booking", e);
        }
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        isOverlapping(bookingRequest, existingBooking)
                );
    }

    private boolean isOverlapping(BookedRoom bookingRequest, BookedRoom existingBooking) {
        return (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()) &&
                bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate()));
    }
}