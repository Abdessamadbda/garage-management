import React, { useState, useEffect } from "react";
import { addVehicle } from "../services/vehicleService";

const VehicleForm = ({
  initialData,
  onAddVehicle,
  onSubmit,
  onCancel,
  isEditing,
}) => {
  const [formData, setFormData] = useState({
    vin: "",
    registrationNumber: "",
    brand: "",
    model: "",
    year: 0,
    color: "",
    mileage: 0,
    fuelType: "",
    purchaseDate: "",
    ownerId: null,
    vehicleStatus: "",
  });

  // Load initial data when editing
  useEffect(() => {
    if (initialData && isEditing) {
      setFormData(initialData);
    }
  }, [initialData, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Process input values to match backend types
    const processedValue = ["year", "mileage", "ownerId"].includes(name)
      ? value === ""
        ? 0
        : parseInt(value, 10)
      : value;

    setFormData((prev) => ({
      ...prev,
      [name]: processedValue,
    }));
  };

  const formatDateForBackend = (date) => {
    if (!date) return null;
    // Convert to ISO string format: "YYYY-MM-DDTHH:mm:ss"
    const [year, month, day] = date.split("-");
    return `${year}-${month}-${day}T00:00:00`;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const processedData = {
        ...formData,
        purchaseDate: formatDateForBackend(formData.purchaseDate),
      };

      if (isEditing) {
        await onSubmit(processedData);
      } else {
        await addVehicle(processedData);
        onAddVehicle();
        setFormData({
          vin: "",
          registrationNumber: "",
          brand: "",
          model: "",
          year: 0,
          color: "",
          mileage: 0,
          fuelType: "",
          purchaseDate: "",
          ownerId: null,
          vehicleStatus: "",
        });
      }
    } catch (error) {
      console.error(
        `Erreur lors de ${
          isEditing ? "la modification" : "l'ajout"
        } du véhicule:`,
        error.response || error
      );
      alert(
        `Erreur lors de ${
          isEditing ? "la modification" : "l'ajout"
        } du véhicule. Veuillez vérifier les informations.`
      );
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <input
        type="text"
        name="vin"
        placeholder="VIN"
        value={formData.vin}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="registrationNumber"
        placeholder="Numéro d'immatriculation"
        value={formData.registrationNumber}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="brand"
        placeholder="Marque"
        value={formData.brand}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="model"
        placeholder="Modèle"
        value={formData.model}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="number"
        name="year"
        placeholder="Année"
        value={formData.year}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="color"
        placeholder="Couleur"
        value={formData.color}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="number"
        name="mileage"
        placeholder="Kilométrage"
        value={formData.mileage}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="fuelType"
        placeholder="Type de carburant"
        value={formData.fuelType}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="date"
        name="purchaseDate"
        placeholder="Date d'achat"
        value={formData.purchaseDate}
        onChange={handleChange}
        className="w-full p-2 border rounded-md"
      />
      <input
        type="number"
        name="ownerId"
        placeholder="ID du propriétaire"
        value={formData.ownerId || ""}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="vehicleStatus"
        placeholder="État du véhicule"
        value={formData.vehicleStatus}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <div className="flex gap-2">
        <button
          type="submit"
          className="flex-1 p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
        >
          {isEditing ? "Modifier Véhicule" : "Ajouter Véhicule"}
        </button>
        {isEditing && (
          <button
            type="button"
            onClick={onCancel}
            className="flex-1 p-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
          >
            Annuler
          </button>
        )}
      </div>
    </form>
  );
};

export default VehicleForm;
