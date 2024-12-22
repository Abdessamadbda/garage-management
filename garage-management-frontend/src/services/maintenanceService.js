import axios from "axios";

const BASE_URL = "http://localhost:8082/maintenance-jobs"; // Remplacez par l'URL de votre service Maintenance

export const getMaintenanceJobs = async () => {
  const response = await axios.get(BASE_URL);
  return response.data;
};

export const addMaintenanceJob = async (job) => {
  const response = await axios.post(BASE_URL, job);
  return response.data;
};

export const updateMaintenanceJob = async (id, updatedJob) => {
  try {
    const response = await axios.put(`${BASE_URL}/${id}`, updatedJob);
    return response.data;
  } catch (error) {
    console.error("Error updating maintenance job:", error);
    throw error;
  }
};

export const deleteMaintenanceJob = async (id) => {
  try {
    const response = await axios.delete(`${BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting maintenance job:", error);
    throw error;
  }
};
