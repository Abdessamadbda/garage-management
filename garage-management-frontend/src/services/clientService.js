import axios from "axios";

const BASE_URL = "http://localhost:8081/clients"; // Remplacez par l'URL de votre service Client

export const getClients = async () => {
  const response = await axios.get(BASE_URL);
  return response.data;
};
export const deleteClient = async (clientId) => {
  const response = await axios.delete(`${BASE_URL}/${clientId}`);
  return response.data;
};
export const addClient = async (client) => {
  const response = await axios.post(BASE_URL, client);
  return response.data;
};

export const updateClient = async (clientData) => {
  try {
    // Ensure we're using the correct ID from the client data
    const id = clientData.id;

    const response = await fetch(`${BASE_URL}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(clientData),
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    return await response.json();
  } catch (error) {
    console.error("Error updating client:", error);
    throw error;
  }
};
