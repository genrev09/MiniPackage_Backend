package com.thoughtworks.parking_lot.Service;

import com.thoughtworks.parking_lot.Repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.core.ParkingLot;
import javassist.NotFoundException;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ParkingLotService.class)
@ActiveProfiles(profiles = "test")
public class ParkingLotServiceTest {

    @MockBean
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingLotService parkingLotService;

    @Test
    public void should_add_parking_lot() {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Genrev");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Santa Rosa");

        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        Assertions.assertThat(parkingLotService.addParkingLot(parkingLot)).isEqualTo(parkingLotService.PARKING_LOT_CREATED);
    }

    @Test
    public void should_not_add_existing_parking_lot_name() {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Genrev");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Santa Rosa");

        when(parkingLotRepository.save(parkingLot)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> {
            parkingLotService.addParkingLot(parkingLot);
        });

    }

    @Test
    public void should_delete_parking_lot() throws NotFoundException {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Genrev");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Santa Rosa");

        when(parkingLotRepository.findByName("Genrev")).thenReturn(parkingLot);

        Assertions.assertThat(parkingLotService.deleteParkingLot("Genrev")).isEqualTo(parkingLotService.PARKING_LOT_SUCCESSFULLY_DELETED);
    }

    @Test
    public void should_not_delete_invalid_parking_lot_name() throws NotFoundException {

        when(parkingLotRepository.findByName("Genrev")).thenReturn(null);
        assertThrows(NotFoundException.class, () ->{
            parkingLotService.deleteParkingLot("Genrev");
        });

    }

    @Test
    public void should_get_all_parking_lot() {
        List<ParkingLot> parkingLotList = new ArrayList<>();
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Genrev");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Santa Rosa");

        parkingLotList.add(parkingLot);
        Page<ParkingLot> parkingLotPage = new PageImpl<>(parkingLotList);

        when(parkingLotRepository.findAll(PageRequest.of(0,15))).thenReturn(parkingLotPage);

        Assertions.assertThat(parkingLotService.getAllParkingLot(0,15)).isEqualTo(parkingLotPage);
    }

    @Test
    public void should_get_parking_lot_by_name() throws NotFoundException {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Genrev");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Santa Rosa");

        when(parkingLotRepository.findByName("Genrev")).thenReturn(parkingLot);

        Assertions.assertThat(parkingLotService.getParkingLotByName("Genrev"))
                .isEqualToComparingFieldByField(parkingLot);
    }

    @Test
    public void should_update_parking_lot_capacity() throws NotFoundException {
        ParkingLot updatedParkingLotCapacity = new ParkingLot();
        updatedParkingLotCapacity.setCapacity(5);

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Genrev");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Santa Rosa");

        ParkingLot updatedParkingLot = new ParkingLot();
        updatedParkingLot.setName("Genrev");
        updatedParkingLot.setCapacity(5);
        updatedParkingLot.setLocation("Santa Rosa");

        when(parkingLotRepository.findByName("Genrev")).thenReturn(parkingLot);

        Assertions.assertThat(parkingLotService.updateParkingLotCapacity("Genrev",updatedParkingLotCapacity))
                .isEqualToComparingFieldByField(updatedParkingLot);
    }

}
