package com.example.toolsleasing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity(name = "leases")
public class CLease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    https://dev.to/tienbku/jpa-manytoone-example-in-spring-boot-3n97
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tool_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CTool tool;

    @Column(name = "lessee")
    private String lessee;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;
}
