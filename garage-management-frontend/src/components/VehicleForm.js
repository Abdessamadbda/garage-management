import React, { useState } from "react";
import { addMaintenanceJob } from "../services/maintenanceService";

const MaintenanceForm = ({ initialData = {}, onSubmit, onCancel }) => {
  const [job, setJob] = useState({
    id: initialData.id || "",
    vehicleVin: initialData.vehicleVin || "",
    description: initialData.description || "",
    statut: initialData.statut || "",
    tempsDebut: initialData.tempsDebut || "",
    tempsFin: initialData.tempsFin || "",
  });

  const handleChange = (e) => {
    setJob({ ...job, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const apiJob = {
      id: parseInt(job.id, 10),
      vehicleId: parseInt(job.vehicleVin, 10),
      startTime: job.tempsDebut,
      endTime: job.tempsFin,
      description: job.description,
      status: job.statut.toUpperCase(),
    };

    try {
      await addMaintenanceJob(apiJob);
      if (onSubmit) onSubmit(apiJob);
    } catch (error) {
      console.error(
        "Erreur lors de l'ajout/mise à jour du travail de maintenance:",
        error
      );
      alert("Erreur lors de l'ajout/mise à jour du travail de maintenance.");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {[
        "id",
        "vehicleVin",
        "description",
        "statut",
        "tempsDebut",
        "tempsFin",
      ].map((field) => (
        <input
          key={field}
          type={field.includes("temps") ? "datetime-local" : "text"}
          name={field}
          value={job[field]}
          placeholder={field}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded-md"
        />
      ))}
      <div className="flex justify-between">
        <button
          type="submit"
          className="p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
        >
          {initialData.id ? "Mettre à Jour" : "Ajouter Maintenance"}
        </button>
        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="p-2 bg-gray-300 text-black rounded-md hover:bg-gray-400"
          >
            Annuler
          </button>
        )}
      </div>
    </form>
  );
};

export default MaintenanceForm;
