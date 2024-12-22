import React, { useEffect, useState } from "react";
import axiosInstance from "../axiosConfig";
import { getClients } from "../services/clientService";

const ClientList = () => {
  const [clients, setClients] = useState([]);

  useEffect(() => {
    // Fetch clients from the API
    axiosInstance
      .get("/clients")
      .then((response) => setClients(response.data))
      .catch((error) => console.error("Error fetching clients:", error));
  }, []);

  return (
    <div>
      <h2>Clients</h2>
      <ul>
        {clients.map((client) => (
          <li key={client.id}>
            {client.firstname} {client.lastname} - {client.phone}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ClientList;
