package haas.gunther.lakeside_hotel.service;

import haas.gunther.lakeside_hotel.model.BookedRoom;

import java.util.List;

public interface BookingService {

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);
}
