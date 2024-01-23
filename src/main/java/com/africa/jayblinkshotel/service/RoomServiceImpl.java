package com.africa.jayblinkshotel.service;

import com.africa.jayblinkshotel.model.Room;
import com.africa.jayblinkshotel.exception.ResourceNotFoundException;
import com.africa.jayblinkshotel.repository.RoomServiceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomServiceDao roomServiceDao;
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice)
            throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomServiceDao.saveRoom(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomServiceDao.selectDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomServiceDao.selectAllRooms();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomServiceDao.selectById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Room with id: %s not found!", roomId));
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }

        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomServiceDao.selectById(roomId);
        if (theRoom.isPresent()) {
            roomServiceDao.deleteRoomById(roomId);
        }
    }
}
