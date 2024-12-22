import axios from "axios";

const BASE_URL = "http://localhost:8085/vehicles";

export const getVehicles = async () => {
  const response = await axios.get(BASE_URL);
  return response.data;
};

export const addVehicle = async (vehicle) => {
  const response = await axios.post(BASE_URL, vehicle);
  return response.data;
};
export const updateVehicle = async (id, updatedVehicle) => {
  try {
    const response = await axios.put(`${BASE_URL}/${id}`, updatedVehicle);
    return response.data;
  } catch (error) {
    console.error("Error updating vehicle:", error);
    throw error;
  }
};

export const deleteVehicle = async (id) => {
  try {
    const response = await axios.delete(`${BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting vehicle:", error);
    throw error;
  }
};
