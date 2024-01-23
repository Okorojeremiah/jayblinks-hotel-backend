package com.africa.jayblinkshotel.service;

import com.africa.jayblinkshotel.model.Bookings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Override
    public List<Bookings> getAllBookingsByRoomId(Long roomId) {
        return null;
    }
}
