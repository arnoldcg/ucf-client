package com.iqsa.ucf.rest.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity(name = "company")
@Table(name = "company", schema = "public")
@SequenceGenerator(name = "hibernate_sequence",
        sequenceName = "hibernate_sequence",
        schema = "public")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Audited
public class CompanyModel {

    @Id
    @Column(name = "id_company", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company")
    private List<UserModel> users;
}