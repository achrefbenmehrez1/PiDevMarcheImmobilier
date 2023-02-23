package tn.esprit.realestate.IServices;

import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;

import java.util.List;

public interface IAdvertisementService {

    public Advertisement addAdvertisement(Advertisement add, long userId);
    public boolean deleteAdvertisement(long id);

    public Advertisement updateAdvertisement(Advertisement ad);
    public List<Advertisement> getAllAds();

    public List<Advertisement> getUserAds(long userid);

    public List<Advertisement> getAds(TypeAd typeAd, Type typeProp, String region, Integer rooms);
}
