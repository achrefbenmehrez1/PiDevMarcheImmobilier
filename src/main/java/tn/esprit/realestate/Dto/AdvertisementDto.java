package tn.esprit.realestate.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdvertisementDto {

    private long id;

    private String title;

    private Double price;

    private String description;

    private TypeAd typeAd;

    private String foreignAdUrl;

    private boolean scraped;

    private PropertyDto property;
    // Property

    // user's info
    private String firstname;

    private String lastname;

    public AdvertisementDto fromEntityToDTO(Advertisement advertisement){
        String firstName = advertisement.getUser() != null ? advertisement.getUser().getFirstname() : null;
        String lastName = advertisement.getUser() != null ? advertisement.getUser().getLastname() : null;
        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .price(advertisement.getPrice())
                .description(advertisement.getDescription())
                .typeAd(advertisement.getTypeAd())
                .foreignAdUrl(advertisement.getForeignAdUrl())
                .scraped(advertisement.isScraped())
                .property(new PropertyDto().fromEntityToDTO(advertisement.getProperty()))
                .firstname(firstName)
                .lastname(lastName)
                .build();



    }

    public Advertisement fromDTOtoEntity(AdvertisementDto advertisementDto){
        return  Advertisement.builder()
                .id(advertisementDto.getId())
                .title(advertisementDto.getTitle())
                .price(advertisementDto.getPrice())
                .description(advertisementDto.getDescription())
                .typeAd(advertisementDto.getTypeAd())
                .foreignAdUrl(advertisementDto.getForeignAdUrl())
                .scraped(advertisementDto.isScraped())
                .property(new PropertyDto().fromDTOtoEntity(advertisementDto.getProperty()))
                .build();
    }



}