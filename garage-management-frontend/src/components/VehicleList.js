import React, { useEffect, useState } from "react";
import axiosInstance from "../axiosConfig";
import { getClients } from "../services/clientService";

const ClientList = () => {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchClients = async () => {
    try {
      setLoading(true);
      const clientsData = await getClients();
      setClients(clientsData);
      setError(null);
    } catch (err) {
      setError("Failed to fetch clients");
      console.error("Error fetching clients:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClients();
  }, []);

  if (loading) {
    return <div>Loading clients...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h2>Clients</h2>
      {clients.length === 0 ? (
        <p>No clients found</p>
      ) : (
        <ul>
          {clients.map((client) => (
            <li key={client.id}>
              {client.firstname} {client.lastname} - {client.phone}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default ClientList;
