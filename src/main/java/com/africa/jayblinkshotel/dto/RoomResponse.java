package com.africa.jayblinkshotel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.List;


@Data
@NoArgsConstructor
public class RoomResponse{

    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isRoomBooked;
    private String photo;
    private List<BookingResponse> bookings;

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice,
                        boolean isRoomBooked, byte[] photoByte) {

        this(id, roomType, roomPrice);
        this.isRoomBooked = isRoomBooked;
        this.photo = photoByte !=  null ? Base64.encodeBase64String(photoByte) : null;
//        this.bookings = bookings;
    }

}
