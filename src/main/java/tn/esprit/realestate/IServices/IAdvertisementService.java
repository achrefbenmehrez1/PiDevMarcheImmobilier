package tn.esprit.realestate.IServices;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Dto.AdvertisementDto;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;
import tn.esprit.realestate.Entities.User;

import java.io.IOException;
import java.util.List;

public interface IAdvertisementService {

    public AdvertisementDto getAdById(long id);


    //ADD without DTO:

    public void addAd(String title,Double price, String description, TypeAd typeAd,
                      Double size, Type type, int rooms, boolean parking,
                      Double yardSpace, boolean garage, String region,String ville,List<MultipartFile> photos,@NonNull HttpServletRequest request)throws IOException;


    //public void addAd(AdvertisementDto advertisementDto, List<MultipartFile> photos, HttpServletRequest request) throws IOException;





    //public Advertisement addAdvertisement(Advertisement add, long userId);
    public boolean deleteAdvertisement(long id,@NonNull HttpServletRequest request);


    public Advertisement updateAdvertisement(long id,String title,Double price, String description, TypeAd typeAd,
                                             Double size, Type type, Integer rooms, Boolean parking,
                                             Double yardSpace, Boolean garage, String region,
                                             String ville,List<MultipartFile> photo,@NonNull HttpServletRequest request) throws IOException;


/* UPDAAATEE !!
    public AdvertisementDto updateAdvertisement(Long id,
                                                 AdvertisementDto advertisementDTO,
                                                @NonNull HttpServletRequest request,
                                                 MultipartFile photo) throws IOException;

 */
    //public List<Advertisement> getAllAds();

    // DTO
    //public Page<Advertisement> getAllAds(int pageNumber, int pageSize);
    public Page<AdvertisementDto> getAllAds(int pageNumber, int pageSize);

    //public List<Advertisement> getUserAds(@NonNull HttpServletRequest request);
    public Page<AdvertisementDto> getUserAds(@NonNull HttpServletRequest request, int pageNumber, int pageSize);

    public Page<AdvertisementDto> getAds(TypeAd typeAd, Type typeProp,
                                      String region,String ville,
                                      Integer rooms,
                                      Boolean parking, Boolean garage,
                                      Double maxPrice, Double minPrice,
                                      Double minSize, Double maxSize,
                                      int pageNumber, int pageSize);

    //public List<Advertisement> getScrappedAds() throws IOException;

    public Page<AdvertisementDto> getAdsByUsersLocation(HttpServletRequest request,int pageNumber, int pageSize)throws IOException;

     public ResponseEntity<?> contactAdOwner(long id,String message, HttpServletRequest request) throws MessagingException;

    public void addFavorite(HttpServletRequest request, Long id);

    public List<AdvertisementDto> getUsersFavorites(HttpServletRequest request);

    public void removeFavorite(HttpServletRequest request, Long id);

    //to know how many poeple liked your Ad
    public List<User> consultingFavorite(HttpServletRequest request, Long idAd);
}
