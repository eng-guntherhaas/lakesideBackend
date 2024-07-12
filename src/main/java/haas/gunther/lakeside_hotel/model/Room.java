package haas.gunther.lakeside_hotel.model;

import ch.qos.logback.core.util.StringUtil;
import haas.gunther.lakeside_hotel.utils.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Room_Type")
    private String roomType;
    @Column(name = "Room_Price")
    private BigDecimal roomPrice;
    @Column(name = "Is_Booked")
    private boolean isBooked = false;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "Bookings")
    private List<BookedRoom> bookings;
    @Lob
    @Column(name = "Photo")
    private Blob photo;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedRoom bookedRoom) {
        if(bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(bookedRoom);
        bookedRoom.setRoom(this);
        isBooked = true;
        String bookingCode = Utils.generateRandomString(10);
        bookedRoom.setBookingConfirmationCode(bookingCode);
    }
}
