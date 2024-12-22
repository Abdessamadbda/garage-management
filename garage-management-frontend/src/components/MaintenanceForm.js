import React, { useState, useEffect } from "react";

const MaintenanceJobForm = ({ initialData = {}, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    id: "",
    vehicleVin: "",
    description: "",
    statut: "",
    tempsDebut: "",
    tempsFin: "",
    ...initialData,
  });

  useEffect(() => {
    setFormData((prevState) => ({ ...prevState, ...initialData }));
  }, [initialData]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const apiJob = {
      id: parseInt(formData.id, 10),
      vehicleId: parseInt(formData.vehicleVin, 10),
      description: formData.description,
      status: formData.statut.toUpperCase(),
      startTime: formData.tempsDebut,
      endTime: formData.tempsFin,
    };
    onSubmit(apiJob);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {[
        { name: "id", label: "ID" },
        { name: "vehicleVin", label: "Vehicle ID" },
        { name: "description", label: "Description" },
        { name: "statut", label: "Statut" },
        { name: "tempsDebut", label: "Temps Début", type: "datetime-local" },
        { name: "tempsFin", label: "Temps Fin", type: "datetime-local" },
      ].map(({ name, label, type = "text" }) => (
        <input
          key={name}
          type={type}
          name={name}
          value={formData[name] || ""}
          placeholder={label}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded-md"
        />
      ))}
      <button
        type="submit"
        className="w-full p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
      >
        {initialData.id ? "Mettre à jour" : "Ajouter"} Maintenance
      </button>
      {onCancel && (
        <button
          type="button"
          onClick={onCancel}
          className="w-full p-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
        >
          Annuler
        </button>
      )}
    </form>
  );
};

export default MaintenanceJobForm;
