package tn.esprit.realestate.Dto;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.Entities.Type;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertyDto {


    private long id;


    private Double size;


    private Type type;


    private Integer rooms;


    private Boolean parking;


    private Double yardSpace;


    private Boolean garage;


    private String ville;

    private String region;


    private List<String> photo;

    public PropertyDto fromEntityToDTO(Property property){
        return PropertyDto.builder()
                .id(property.getId())
                .size(property.getSize())
                .type(property.getType())
                .rooms(property.getRooms())
                .parking(property.isParking())
                .yardSpace(property.getYardSpace())
                .garage(property.isGarage())
                .ville(property.getVille())
                .region(property.getRegion())
                .photo(property.getPhoto())
                .build();
    }

    public Property fromDTOtoEntity(PropertyDto propertyDto){
        return Property.builder()
                .id(propertyDto.getId())
                .size(propertyDto.getSize())
                .type(propertyDto.getType())
                .rooms(propertyDto.getRooms())
                .parking(propertyDto.getParking())
                .yardSpace(propertyDto.getYardSpace())
                .garage(propertyDto.getGarage())
                .ville(propertyDto.getVille())
                .region(propertyDto.getRegion())
                .photo(propertyDto.getPhoto())
                .build();
    }
}
