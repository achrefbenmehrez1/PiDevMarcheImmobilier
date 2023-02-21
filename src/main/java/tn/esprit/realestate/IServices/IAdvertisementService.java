package tn.esprit.realestate.IServices;

import tn.esprit.realestate.Entities.Advertisement;

import java.util.List;

public interface IAdvertisementService {

    public void addAdvertisement(Advertisement add, long userId);
    public boolean deleteAdvertisement(long id);

    public Advertisement updateAdvertisement(Advertisement ad);
    public List<Advertisement> getAllAds();
}
