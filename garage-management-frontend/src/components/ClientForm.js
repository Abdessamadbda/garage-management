import React, { useState, useEffect } from "react";
import { addClient } from "../services/clientService";

const ClientForm = ({
  initialData,
  onAddClient,
  onSubmit,
  onCancel,
  isEditing,
}) => {
  const [formData, setFormData] = useState({
    cin: "",
    firstname: "",
    lastname: "",
    phone: "",
    address: "",
    email: "",
  });

  // Load initial data when editing
  useEffect(() => {
    if (initialData && isEditing) {
      setFormData(initialData);
    }
  }, [initialData, isEditing]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        await onSubmit(formData);
      } else {
        await addClient(formData);
        onAddClient();
        setFormData({
          cin: "",
          firstname: "",
          lastname: "",
          phone: "",
          address: "",
          email: "",
        });
      }
    } catch (error) {
      console.error(
        `Erreur lors de ${
          isEditing ? "la modification" : "l'ajout"
        } du client:`,
        error.response || error
      );
      alert(
        `Erreur lors de ${
          isEditing ? "la modification" : "l'ajout"
        } du client. Veuillez vérifier les informations.`
      );
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <input
        type="text"
        name="cin"
        placeholder="CIN"
        value={formData.cin}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="firstname"
        placeholder="Prénom"
        value={formData.firstname}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="lastname"
        placeholder="Nom de famille"
        value={formData.lastname}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="phone"
        placeholder="Téléphone"
        value={formData.phone}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="text"
        name="address"
        placeholder="Adresse"
        value={formData.address}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <input
        type="email"
        name="email"
        placeholder="Email"
        value={formData.email}
        onChange={handleChange}
        required
        className="w-full p-2 border rounded-md"
      />
      <div className="flex gap-2">
        <button
          type="submit"
          className="flex-1 p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
        >
          {isEditing ? "Modifier Client" : "Ajouter Client"}
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

export default ClientForm;
