package org.example.backend_dip.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
@Builder
public class BookReaderForAdmin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstName;
    String lastName;
    String email;

    Long availableCount;
    Long reservedCount;
    Long returnedCount;
//
//    public BookReaderForAdmin(Long id,String firstName, String lastName, String email, Long availableCount, Long reservedCount, Long returnedCount) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.availableCount = availableCount;
//        this.reservedCount = reservedCount;
//        this.returnedCount = returnedCount;
//    }

    public BookReaderForAdmin(Long id, String firstName, String lastName, String email, Long availableCount, Long reservedCount, Long returnedCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.availableCount = availableCount != null ? availableCount.longValue() : 0L;
        this.reservedCount = reservedCount != null ? reservedCount.longValue() : 0L;
        this.returnedCount = returnedCount != null ? returnedCount.longValue() : 0L;
    }
}
