package tn.esprit.realestate.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


public enum Role {
    USER,
    ADMIN,
    BANKING_AGENT,
    PROMOTER
}