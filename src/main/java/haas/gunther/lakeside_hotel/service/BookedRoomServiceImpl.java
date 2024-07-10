package haas.gunther.lakeside_hotel.service;

import haas.gunther.lakeside_hotel.model.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookedRoomServiceImpl implements BookedRoomService {
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return List.of();
    }
}
