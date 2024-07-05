package haas.gunther.lakeside_hotel.repository;

import haas.gunther.lakeside_hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
