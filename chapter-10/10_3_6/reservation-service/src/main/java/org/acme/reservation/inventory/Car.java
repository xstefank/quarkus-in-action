package org.acme.reservation.inventory;

public class Car {

    public Long id;
    public String licensePlateNumber;
    public String manufacturer;
    public String model;

    public Car() {
    }

    public Car(Long id, String licensePlateNumber,
               String manufacturer, String model) {
        this.id = id;
        this.licensePlateNumber = licensePlateNumber;
        this.manufacturer = manufacturer;
        this.model = model;
    }
}
