package haas.gunther.lakeside_hotel.service;

import haas.gunther.lakeside_hotel.model.BookedRoom;

import java.util.List;

public interface BookingService {

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
}
