package com.thoughtworks.parking_lot.Service;

import com.thoughtworks.parking_lot.Repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.core.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    @Autowired
    ParkingLotRepository parkingLotRepository;

    public boolean addParkingLot(ParkingLot parkingLot){
        if(parkingLotRepository.save(parkingLot) != null)
            return true;
        return false;
    }

    public boolean deleteParkingLot(String parkingLotName){
        ParkingLot parkingLot = parkingLotRepository.findByName(parkingLotName);
        if (parkingLot != null) {
            parkingLotRepository.delete(parkingLot);
            return true;
        }
        return false;
    }

    public Page<ParkingLot> getAllParkingLot(int page, int pagesize){
        return parkingLotRepository.findAll(PageRequest.of(page,pagesize));
    }
}
