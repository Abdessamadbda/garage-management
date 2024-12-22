import React, { useEffect, useState } from "react";
import VehicleForm from "./VehicleForm";
import {
  getVehicles,
  updateVehicle,
  deleteVehicle,
} from "../services/vehicleService";

function VehicleManagement() {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editingVehicle, setEditingVehicle] = useState(null);
  const [updateSuccess, setUpdateSuccess] = useState(false);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    fetchVehicles();
  }, []);

  const fetchVehicles = async () => {
    try {
      setLoading(true);
      const vehiclesList = await getVehicles();
      setVehicles(vehiclesList);
      setError(null);
    } catch (err) {
      setError("Impossible de charger les véhicules");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (vehicle) => {
    setEditingVehicle(vehicle);
    setShowModal(true);
  };

  const handleCancelEdit = () => {
    setEditingVehicle(null);
    setShowModal(false);
    setError(null);
  };

  const handleUpdateVehicle = async (updatedVehicleData) => {
    try {
      setError(null);
      const vehicleToUpdate = {
        ...updatedVehicleData,
        id: editingVehicle.id,
      };

      await updateVehicle(vehicleToUpdate);
      setUpdateSuccess(true);
      setShowModal(false);
      await fetchVehicles();

      setTimeout(() => {
        setUpdateSuccess(false);
      }, 3000);
    } catch (err) {
      setError("Erreur lors de la mise à jour du véhicule");
      console.error("Erreur lors de la mise à jour du véhicule:", err);
    }
  };

  const handleDelete = async (vehicleId) => {
    const confirmed = window.confirm(
      "Êtes-vous sûr de vouloir supprimer ce véhicule ?"
    );
    if (!confirmed) return;

    try {
      await deleteVehicle(vehicleId);
      setVehicles(vehicles.filter((vehicle) => vehicle.id !== vehicleId));
    } catch (err) {
      setError("Erreur lors de la suppression du véhicule");
      console.error("Erreur lors de la suppression du véhicule:", err);
    }
  };

  const EditModal = () => (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg w-full max-w-2xl max-h-[90vh] flex flex-col">
        <div className="p-6 border-b">
          <div className="flex justify-between items-center">
            <h3 className="text-xl font-semibold">Modifier le Véhicule</h3>
            <button
              onClick={handleCancelEdit}
              className="text-gray-500 hover:text-gray-700"
            >
              ✕
            </button>
          </div>
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
              {error}
            </div>
          )}
        </div>
        <div className="p-6 overflow-y-auto flex-1">
          <VehicleForm
            initialData={editingVehicle}
            onSubmit={handleUpdateVehicle}
            onCancel={handleCancelEdit}
            isEditing={true}
          />
        </div>
      </div>
    </div>
  );

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-4">Gestion des Véhicules</h2>

      {updateSuccess && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
          Véhicule mis à jour avec succès!
        </div>
      )}

      <VehicleForm onAddVehicle={fetchVehicles} />

      <div className="mt-8">
        <h3 className="text-xl font-semibold mb-4">Liste des Véhicules</h3>

        {loading ? (
          <p className="text-gray-500">Chargement des véhicules...</p>
        ) : vehicles.length === 0 ? (
          <p className="text-gray-500">Aucun véhicule trouvé</p>
        ) : (
          <ul className="space-y-3">
            {vehicles.map((vehicle) => (
              <li
                key={vehicle.id}
                className="flex justify-between items-center p-4 bg-white rounded-lg shadow hover:shadow-md transition-shadow"
              >
                <div className="flex flex-col">
                  <span className="font-medium">
                    {vehicle.make} {vehicle.model}
                  </span>
                  <span className="text-gray-600 text-sm">
                    {vehicle.plateNumber} - {vehicle.year}
                  </span>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEdit(vehicle)}
                    className="px-4 py-2 text-blue-600 hover:text-blue-800 rounded border border-blue-600 hover:border-blue-800 transition-colors"
                  >
                    Modifier
                  </button>
                  <button
                    onClick={() => handleDelete(vehicle.id)}
                    className="px-4 py-2 text-red-600 hover:text-red-800 rounded border border-red-600 hover:border-red-800 transition-colors"
                  >
                    Supprimer
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>

      {showModal && <EditModal />}
    </div>
  );
}

export default VehicleManagement;
