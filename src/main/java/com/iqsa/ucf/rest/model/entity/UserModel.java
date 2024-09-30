package com.iqsa.ucf.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;

@Entity(name = "user")
@Table(name = "user", schema = "public")
@SequenceGenerator(name = "hibernate_sequence",
        sequenceName = "hibernate_sequence",
        schema = "public")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Audited
public class UserModel {

    @Id
    @Column(name = "id_user", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String password;

    @Column(name = "role")
    private String role;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = CompanyModel.class)
    @JoinColumn(name = "id_company")
    @NotAudited
    private CompanyModel company;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, targetEntity = DocumentModel.class)
    @JoinColumn(name = "id_user")
    private List<DocumentModel> documents;

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        var result = "";
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}