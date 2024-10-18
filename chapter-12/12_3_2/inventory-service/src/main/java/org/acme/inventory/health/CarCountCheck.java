package org.acme.inventory.health;

import io.smallrye.health.api.Wellness;
import jakarta.inject.Inject;
import org.acme.inventory.repository.CarRepository;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Wellness
public class CarCountCheck implements HealthCheck {

    @Inject
    CarRepository carRepository;

    @Override
    public HealthCheckResponse call() {
        long carsCount = carRepository.findAll().count();
        boolean wellnessStatus = carsCount > 0;
        return HealthCheckResponse.builder()
            .name("car-count-check")
            .status(wellnessStatus)
            .withData("cars-count", carsCount)
            .build();
    }
}
