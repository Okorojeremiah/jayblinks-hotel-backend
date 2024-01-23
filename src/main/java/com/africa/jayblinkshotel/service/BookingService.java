package com.africa.jayblinkshotel.service;

import com.africa.jayblinkshotel.model.Bookings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<Bookings> getAllBookingsByRoomId(Long roomId);
}
