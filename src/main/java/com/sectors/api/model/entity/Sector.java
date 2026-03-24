package com.sectors.api.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sectors")
@AllArgsConstructor
@NoArgsConstructor
public class Sector {

    @Id
    private Long id;
    private Long parentId;
    private int displayOrder;
    private String name;
}
