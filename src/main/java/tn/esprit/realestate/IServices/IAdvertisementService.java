package tn.esprit.realestate.IServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;

import java.io.IOException;
import java.util.List;

public interface IAdvertisementService {

    public void addAd(String title,Double price, String description, TypeAd typeAd,
                      Double size, Type type, int rooms, boolean parking,
                      Double yardSpace, boolean garage, String region, MultipartFile photo, long userId)throws IOException;
    public Advertisement addAdvertisement(Advertisement add, long userId);
    public boolean deleteAdvertisement(long id);

    public Advertisement updateAdvertisement(Advertisement ad);
    public List<Advertisement> getAllAds();

    public List<Advertisement> getUserAds(long userid);

    public List<Advertisement> getAds(TypeAd typeAd, Type typeProp, String region, Integer rooms);
}
