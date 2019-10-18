package com.thoughtworks.parking_lot.Controller;

import com.thoughtworks.parking_lot.Service.ParkingLotService;
import com.thoughtworks.parking_lot.core.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/parkinglots")
public class ParkingLotController {
    private final int pageSize = 15;
    @Autowired
    ParkingLotService parkingLotService;

    @PostMapping(headers = {"Content-type=application/json"})
    public ResponseEntity addParkingLot(@RequestBody ParkingLot parkingLot){
        if (parkingLotService.addParkingLot(parkingLot))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/{name}", headers = {"Content-type=application/json"})
    public ResponseEntity deleteParkingLot(@PathVariable("name") String name){
        if (parkingLotService.deleteParkingLot(name))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(headers = {"Content-type=application/json"})
    public Page<ParkingLot> getAllParkingLot(@RequestParam(required = false, defaultValue = "0") int page){
        return parkingLotService.getAllParkingLot(page,pageSize);
    }

    @GetMapping(path = "/{name}", headers = {"Content-type=application/json"})
    public ResponseEntity getParkingLotByName(@PathVariable("name") String name){
        ParkingLot parkingLot = parkingLotService.getParkingLotByName(name);
        if (parkingLot != null)
            return new ResponseEntity<>(parkingLot,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/{name}", headers = {"Content-type=application/json"})
    public ResponseEntity getParkingLotByName(@PathVariable("name") String name,
                                              @RequestBody ParkingLot updatedParkingLotCapacity){
        ParkingLot updatedParkingLot = parkingLotService.updateParkingLotCapacity(name,updatedParkingLotCapacity);
        if (updatedParkingLot != null)
            return new ResponseEntity<>(updatedParkingLot,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
