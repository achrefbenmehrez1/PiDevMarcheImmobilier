package tn.esprit.realestate.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private String price;

    private String description;

    private TypeAd typeAd;

    private String foreignAdUrl;

    private boolean scraped;

    private LocalDateTime created_at;

    // Property
    private PropertyDto property;


    // user's info
    private String username;



    public AdvertisementDto fromEntityToDTO(Advertisement advertisement){
        String userName = advertisement.getUser() != null ? advertisement.getUser().getUsername(): null;

        String newPrice;
        if(advertisement.getCurrency()==null){
             newPrice=advertisement.getPrice().toString();
        }else {
            newPrice=advertisement.getPrice().toString()+advertisement.getCurrency();
        }


        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .price(newPrice)
                .description(advertisement.getDescription())
                .typeAd(advertisement.getTypeAd())
                .foreignAdUrl(advertisement.getForeignAdUrl())
                .scraped(advertisement.isScraped())
                .property(new PropertyDto().fromEntityToDTO(advertisement.getProperty()))
                .username(userName)
                .created_at(advertisement.getCreated_at())
                .build();



    }

    public Advertisement fromDTOtoEntity(AdvertisementDto advertisementDto){
        return  Advertisement.builder()
                .id(advertisementDto.getId())
                .title(advertisementDto.getTitle())
                .price(Double.parseDouble(advertisementDto.getPrice()))
                .description(advertisementDto.getDescription())
                .typeAd(advertisementDto.getTypeAd())
                .foreignAdUrl(advertisementDto.getForeignAdUrl())
                .scraped(advertisementDto.isScraped())
                .property(new PropertyDto().fromDTOtoEntity(advertisementDto.getProperty()))
                .build();
    }



}