package haas.gunther.lakeside_hotel.service;

import haas.gunther.lakeside_hotel.model.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImplt implements BookingService {
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return List.of();
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return List.of();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return null;
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        return "";
    }

    @Override
    public void cancelBooking(Long bookingId) {

    }
}
