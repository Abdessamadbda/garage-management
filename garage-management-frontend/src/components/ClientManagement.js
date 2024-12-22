import React, { useEffect, useState } from "react";
import ClientForm from "./ClientForm";
import {
  getClients,
  updateClient,
  deleteClient,
} from "../services/clientService";

function ClientManagement() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editingClient, setEditingClient] = useState(null);
  const [updateSuccess, setUpdateSuccess] = useState(false);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    try {
      setLoading(true);
      const clientsList = await getClients();
      setClients(clientsList);
      setError(null);
    } catch (err) {
      setError("Impossible de charger les clients");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (client) => {
    setEditingClient(client);
    setShowModal(true);
  };

  const handleCancelEdit = () => {
    setEditingClient(null);
    setShowModal(false);
    setError(null);
  };

  const handleUpdateClient = async (updatedClientData) => {
    try {
      setError(null);
      const clientToUpdate = {
        ...updatedClientData,
        id: editingClient.id,
      };

      await updateClient(clientToUpdate);
      setUpdateSuccess(true);
      setShowModal(false);
      await fetchClients();

      setTimeout(() => {
        setUpdateSuccess(false);
      }, 3000);
    } catch (err) {
      setError("Erreur lors de la mise à jour du client");
      console.error("Erreur lors de la mise à jour du client:", err);
    }
  };

  const handleDelete = async (clientId) => {
    const confirmed = window.confirm(
      "Êtes-vous sûr de vouloir supprimer ce client ?"
    );
    if (!confirmed) return;

    try {
      await deleteClient(clientId);
      setClients(clients.filter((client) => client.id !== clientId));
    } catch (err) {
      setError("Erreur lors de la suppression du client");
      console.error("Erreur lors de la suppression du client:", err);
    }
  };

  const EditModal = () => (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-2xl mx-4">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-xl font-semibold">Modifier le Client</h3>
          <button
            onClick={handleCancelEdit}
            className="text-gray-500 hover:text-gray-700"
          >
            ✕
          </button>
        </div>
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}
        <ClientForm
          initialData={editingClient}
          onSubmit={handleUpdateClient}
          onCancel={handleCancelEdit}
          isEditing={true}
        />
      </div>
    </div>
  );

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-4">Gestion des Clients</h2>

      {updateSuccess && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
          Client mis à jour avec succès!
        </div>
      )}

      <ClientForm onAddClient={fetchClients} />

      <div className="mt-8">
        <h3 className="text-xl font-semibold mb-4">Liste des Clients</h3>

        {loading ? (
          <p className="text-gray-500">Chargement des clients...</p>
        ) : clients.length === 0 ? (
          <p className="text-gray-500">Aucun client trouvé</p>
        ) : (
          <ul className="space-y-3">
            {clients.map((client) => (
              <li
                key={client.id}
                className="flex justify-between items-center p-4 bg-white rounded-lg shadow hover:shadow-md transition-shadow"
              >
                <div className="flex flex-col">
                  <span className="font-medium">
                    {client.firstname} {client.lastname}
                  </span>
                  <span className="text-gray-600 text-sm">
                    {client.phone} - {client.email}
                  </span>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEdit(client)}
                    className="px-4 py-2 text-blue-600 hover:text-blue-800 rounded border border-blue-600 hover:border-blue-800 transition-colors"
                  >
                    Modifier
                  </button>
                  <button
                    onClick={() => handleDelete(client.id)}
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

export default ClientManagement;
