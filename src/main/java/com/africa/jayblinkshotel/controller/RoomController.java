package com.africa.jayblinkshotel.controller;

import com.africa.jayblinkshotel.dto.RoomResponse;
import com.africa.jayblinkshotel.exception.PhotoRetrievalException;
import com.africa.jayblinkshotel.model.Bookings;
import com.africa.jayblinkshotel.model.Room;
import com.africa.jayblinkshotel.service.BookingService;
import com.africa.jayblinkshotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Room controller.
 *
 * @author Jerry
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final BookingService bookingService;


    /**
     * Add new room response entity.
     *
     * @param photo     the photo
     * @param roomType  the room type
     * @param roomPrice the room price
     * @return response entity
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    @PostMapping("/addNewRoom")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {

        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                savedRoom.getRoomPrice());
        return  ResponseEntity.ok(response);
    }

    /**
     * Gets room types.
     *
     * @return the room types
     */
    @GetMapping("/roomTypes")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    /**
     * Gets all rooms.
     *
     * @return all rooms
     * @throws SQLException sql exception
     */
    @GetMapping("/allRooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/deleteRoom/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private RoomResponse getRoomResponse(Room room) {
        List<Bookings> bookings = getAllBookingsByRoomId(room.getId());
//        List<BookingResponse> bookingResponses = bookings.stream().map(booking -> new BookingResponse(
//                booking.getBookingId(),
//                booking.getCheckInDate(),
//                booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            }catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(),
                room.isRoomBooked(), photoBytes);
    }

    private List<Bookings> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }

}
