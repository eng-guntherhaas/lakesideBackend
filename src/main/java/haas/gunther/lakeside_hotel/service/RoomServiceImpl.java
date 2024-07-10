package haas.gunther.lakeside_hotel.service;

import haas.gunther.lakeside_hotel.exception.ResourceNotFoundException;
import haas.gunther.lakeside_hotel.model.Room;
import haas.gunther.lakeside_hotel.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            room.setPhoto(photoBytes);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
     return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()) {
            throw new ResourceNotFoundException("Room not found!");
        }
        byte[] photoByte = room.get().getPhoto();
        if(photoByte != null) {
            return photoByte;
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if(roomOptional.isPresent()) {
            roomRepository.deleteById(roomId);
        }
    }
}
