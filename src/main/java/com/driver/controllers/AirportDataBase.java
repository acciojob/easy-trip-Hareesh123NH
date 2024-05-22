package com.driver.controllers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
public class AirportDataBase {
    HashMap<String,Airport> airportmap=new HashMap<>();
    HashMap<Integer,Flight> flightmap=new HashMap<>();
    HashMap<Integer, Passenger> passengermap=new HashMap<>();
    HashMap<Integer,List<Integer>> ticketmap = new HashMap<>();

    public void addAirport(Airport airport){
        airportmap.put(airport.getAirportName(),airport);
    }
    public String getLargestAirport(){
        int count=0;
        for(Airport air:airportmap.values()){
            if(air.getNoOfTerminals()>=count){
                count=air.getNoOfTerminals();
            }
        }
        List<String> li=new ArrayList<>();
        for(Airport air:airportmap.values()){
            if(air.getNoOfTerminals()==count){
                li.add(air.getAirportName());
            }
        }
        Collections.sort(li);
        return li.get(0);
    }
    public void addFlight(Flight flight){
        flightmap.put(flight.getFlightId(),flight);
    }
    public String getAirportFromFlightId(Integer flightId){
        for(Flight fli:flightmap.values()){
            if(fli.getFlightId()==flightId){
                City city=fli.getFromCity();
                for(Airport air:airportmap.values()){
                    if(air.getCity().equals(city)){
                        return air.getAirportName();
                    }
                }
            }
        }
        return null;
    }
    public void addPassenger(Passenger passenger){
        passengermap.put(passenger.getPassengerId(),passenger);
    }
    public String bookATicket(Integer flightId,Integer passengerId){
        if(ticketmap.containsKey(flightId)){
            List<Integer> list=ticketmap.get(flightId);
            Flight flight=flightmap.get(flightId);
            if(list.size()==flight.getMaxCapacity()){
                return "FAILURE";
            }
            if(list.contains(passengerId)){
                return "FAILURE";
            }
            list.add(passengerId);
            ticketmap.put(flightId,list);
            return "SUCCESS";
        }
        else{
            List<Integer> list=new ArrayList<>();
            list.add(passengerId);
            ticketmap.put(flightId,list);
            return "SUCCESS"; 
        }
    }
    public String cancelATicket(Integer flightId, Integer passengerId) {
        if(ticketmap.containsKey(flightId)){
            boolean removed = false;
            List<Integer> passengerList = ticketmap.get(flightId);
            if(passengerList == null)
                return "FAILURE";
            if(passengerList.contains(passengerId)){
                passengerList.remove(passengerId);
                removed = true;
            }
            if(removed) {
                ticketmap.put(flightId, passengerList);
                return "SUCCESS";
            }
            else
                return "FAILURE";
        }
        return "FAILURE";
    }
    public int calculateFlightFare(Integer flightId){
        int size=ticketmap.get(flightId).size();
        return 3000+(size*50);
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity,City toCity){
        double time=Double.MAX_VALUE;
        for(Flight flight:flightmap.values()){
            if(flight.getFromCity()==fromCity && flight.getToCity()==toCity){
                time=Math.min(time,flight.getDuration());
            }
        }
        return (time==Double.MAX_VALUE)? -1:time;
    }
    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        int count=0;
        for(List<Integer> list:ticketmap.values()){
            for(Integer i:list){
                if(Objects.equals(i, passengerId)){
                    count++;
                }
            }
        }
        return count;
    }
    public int calculateRevenueOfAFlight(Integer flightId){
        if(ticketmap.containsKey(flightId)){
            int count=ticketmap.get(flightId).size();
            int revenue=0;
            for(int i=0;i<count;i++){
                revenue+=3000+(i*50);
            }
            return revenue;
        }
        return 0;
    }
    public int getNumberOfPeopleOn(Date date,String airportName){
        int ans=0;
        if(airportmap.containsKey(airportName)){
            City city=airportmap.get(airportName).getCity();
            for(Integer flightId:ticketmap.keySet()){
                Flight flight=flightmap.get(flightId);
                if(flight.getFlightDate().equals(date) && (flight.getFromCity().equals(city) || flight.getFromCity().equals(city))){
                    ans+=ticketmap.get(flightId).size();
                }
            }
        }
        return ans;
    }
}
