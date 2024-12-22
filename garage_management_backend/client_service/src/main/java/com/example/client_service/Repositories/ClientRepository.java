package com.example.client_service.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.client_service.Entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {


    
    
}
