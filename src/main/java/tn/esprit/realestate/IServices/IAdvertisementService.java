package tn.esprit.realestate.IServices;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;

import java.io.IOException;
import java.util.List;

public interface IAdvertisementService {

    public void addAd(String title,Double price, String description, TypeAd typeAd,
                      Double size, Type type, int rooms, boolean parking,
                      Double yardSpace, boolean garage, String region,String ville,MultipartFile photo,@NonNull HttpServletRequest request)throws IOException;
    //public Advertisement addAdvertisement(Advertisement add, long userId);
    public boolean deleteAdvertisement(long id,@NonNull HttpServletRequest request);

    public Advertisement updateAdvertisement(long id,String title,Double price, String description, TypeAd typeAd,
                                             Double size, Type type, Integer rooms, Boolean parking,
                                             Double yardSpace, Boolean garage, String region,
                                             String ville,MultipartFile photo,@NonNull HttpServletRequest request) throws IOException;
    public List<Advertisement> getAllAds();

    public List<Advertisement> getUserAds(@NonNull HttpServletRequest request);

    public List<Advertisement> getAds(TypeAd typeAd, Type typeProp,
                                      String region,String ville,
                                      Integer rooms,
                                      Boolean parking, Boolean garage,
                                      Double maxPrice, Double minPrice,
                                      Double minSize, Double maxSize);

    //public List<Advertisement> getScrappedAds() throws IOException;

    public List<Advertisement> getAdsByUsersLocation(HttpServletRequest request)throws IOException;

}
