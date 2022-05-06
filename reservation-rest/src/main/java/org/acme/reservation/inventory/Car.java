package org.acme.reservation.inventory;

public class Car {

    public final Long id;
    public final String licensePlateNumber;
    public final String manufacturer;
    public final String model;

    public Car(Long id, String licensePlateNumber, String manufacturer, String model) {
        this.id = id;
        this.licensePlateNumber = licensePlateNumber;
        this.manufacturer = manufacturer;
        this.model = model;
    }
}
