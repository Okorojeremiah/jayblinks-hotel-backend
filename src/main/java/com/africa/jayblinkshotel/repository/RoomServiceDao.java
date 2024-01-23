package com.africa.jayblinkshotel.repository;

import com.africa.jayblinkshotel.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceDao {

    private final RoomRepository roomRepository;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<String> selectDistinctRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    public List<Room> selectAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> selectById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public void deleteRoomById(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
