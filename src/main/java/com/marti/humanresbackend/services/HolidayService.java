package com.marti.humanresbackend.services;

import com.marti.humanresbackend.models.entities.Holiday;
import com.marti.humanresbackend.models.views.APIHolidayView;
import com.marti.humanresbackend.repositories.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class HolidayService {

    private final RestTemplate restTemplate;
    private final HolidayRepository holidayRep;
    private final DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("M-d-y");


    public HolidayService(RestTemplate restTemplate, HolidayRepository holidayRep) {
        this.restTemplate = restTemplate;
        this.holidayRep = holidayRep;
    }

    public void getAllAPIHolidays(int year) {
        APIHolidayView[] forObject = restTemplate.getForObject("https://date.nager.at/api/v3/PublicHolidays/" + year + "/BG", APIHolidayView[].class);
        if(forObject == null)
        {throw new RuntimeException("Could not get holidays from api");}
        else{
            for (APIHolidayView holiday: forObject) {
                if(!holidayRep.existsByHoliday(holiday.date())){
                    holidayRep.save(new Holiday(holiday));
                }
            }
        }

    }

    public void createHoliday(APIHolidayView holiday){
        if(!holidayRep.existsByHoliday(holiday.date())){
            holidayRep.save(new Holiday(holiday.date(), holiday.localName()));
        }
    }

    public List<Holiday> getAllHolidays(){return holidayRep.findAll();}

    public void deleteHoliday(Long Id){holidayRep.delete(holidayRep.getById(Id));}
}
