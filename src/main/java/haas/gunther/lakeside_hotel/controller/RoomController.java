package haas.gunther.lakeside_hotel.controller;

import haas.gunther.lakeside_hotel.exception.ResourceNotFoundException;
import haas.gunther.lakeside_hotel.model.BookedRoom;
import haas.gunther.lakeside_hotel.model.Room;
import haas.gunther.lakeside_hotel.response.RoomResponse;
import haas.gunther.lakeside_hotel.service.BookingService;
import haas.gunther.lakeside_hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final BookingService bookedRoomService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
            ) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for(Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/room/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo
    ) throws IOException, SQLException {
        byte[] photoByes = photo != null && !photo.isEmpty() ?  photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoByes != null && photoByes.length > 0 ? new SerialBlob(photoByes) : null;
        Room room = roomService.updateRoom(roomId, roomType, roomPrice, photoByes);
        room.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(room);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> room = roomService.getRoomById(roomId);
        return room.map(roomResp -> {
            RoomResponse roomResponse = null;
            try {
                roomResponse = getRoomResponse(roomResp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    private RoomResponse getRoomResponse(Room room) throws SQLException {
        List<BookedRoom> bookedRooms = getAllBookingsByRoomId(room.getId());
//        List<BookingResponse> bookignsInfo = bookedRooms
//                .stream()
//                .map(booking -> new BookingResponse(
//                        booking.getBookingId(),
//                        booking.getCheckInDate(),
//                        booking.getCheckOutDate(),
//                        booking.getBookingConfirmationCode())
//                ).toList();
        byte[] photoBytes = null;
//        Blob photoBlob = room.getPhoto();
//        photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
//            if (photoBlob != null) {
//                try{
//                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
//                }catch (SQLException e) {
//                    throw new PhotoRetrievalException("Unable to find room photo");
//                }
//            }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookedRoomService.getAllBookingsByRoomId(roomId);
    }
}
