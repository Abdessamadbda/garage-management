package com.example.client_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.client_service.Entities.Client;
import com.example.client_service.Repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableKafka
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
     @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/clients")
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/clients/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @PostMapping("/clients")    
    public Client addClient(@RequestBody Client client) {
                kafkaTemplate.send("clients-topic", "client.created", client.toString());
        return clientRepository.save(client);
    }

    @PutMapping("/clients/{id}")
    public Client updateClient(@PathVariable Long id, @RequestBody Client client) {

        Client existingClient = clientRepository.findById(id).orElse(null);
                kafkaTemplate.send("clients-topic", "client.updated", client.toString());

        if (existingClient != null) {
            existingClient.setCin(client.getCin());
            existingClient.setFirstname(client.getFirstname());
            existingClient.setLastname(client.getLastname());
            existingClient.setPhone(client.getPhone());
            existingClient.setAddress(client.getAddress());
            existingClient.setEmail(client.getEmail());
            return clientRepository.save(existingClient);
        } else {
            return null;
        }
    }

    @DeleteMapping("/clients/{id}")
    public void deleteClient(@PathVariable Long id) {
        Client existingClient = clientRepository.findById(id).orElse(null);
        if (existingClient != null) {
            clientRepository.delete(existingClient);
            kafkaTemplate.send("clients-topic", "client.deleted", existingClient.toString());
        }
    }

}