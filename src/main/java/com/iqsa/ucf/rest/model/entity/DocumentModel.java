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

@Entity(name = "document")
@Table(name = "document", schema = "public")
@SequenceGenerator(name = "hibernate_sequence",
        sequenceName = "hibernate_sequence",
        schema = "public")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Audited
public class DocumentModel {

    @Id
    @Column(name = "id_document", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "password")
    private String password;

    @Column(name = "absolutePath")
    private String absolutePath;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserModel.class)
    @JoinColumn(name = "id_user")
    @NotAudited
    private UserModel user;

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