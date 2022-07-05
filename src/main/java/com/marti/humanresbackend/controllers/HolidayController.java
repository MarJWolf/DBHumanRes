package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.models.entities.Holiday;
import com.marti.humanresbackend.models.views.APIHolidayView;
import com.marti.humanresbackend.services.HolidayService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(path = "holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService){this.holidayService = holidayService;}

    @PutMapping(path = "allAPI")
    public void allAPIHolidays(@RequestParam int year){ holidayService.getAllAPIHolidays(year);}

    @PutMapping(path = "createHoliday")
    public void createHoliday(@RequestBody APIHolidayView holidayView){holidayService.createHoliday(holidayView);}

    @GetMapping(path = "allHolidays")
    public List<Holiday> getAllHolidays(){return holidayService.getAllHolidays();}

    @DeleteMapping(path = "deleteHoliday")
    public void deleteHoliday(@RequestParam Long Id){holidayService.deleteHoliday(Id);}
}
