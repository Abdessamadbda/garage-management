import React, { useEffect, useState } from "react";
import MaintenanceJobForm from "./MaintenanceForm";
import {
  getMaintenanceJobs,
  updateMaintenanceJob,
} from "../services/maintenanceService";

function MaintenanceJobManagement() {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editingJob, setEditingJob] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    fetchMaintenanceJobs();
  }, []);

  const fetchMaintenanceJobs = async () => {
    try {
      setLoading(true);
      const jobsList = await getMaintenanceJobs();
      setJobs(jobsList);
      setError(null);
    } catch (err) {
      setError("Impossible de charger les travaux de maintenance");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateJob = async (updatedJob) => {
    try {
      await updateMaintenanceJob(updatedJob);
      closeModal();
      fetchMaintenanceJobs();
    } catch (err) {
      console.error(
        "Erreur lors de la mise à jour du travail de maintenance:",
        err
      );
    }
  };

  const openEditModal = (job) => {
    setEditingJob(job);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setEditingJob(null);
    setIsModalOpen(false);
  };

  return (
    <section>
      <h2 className="text-2xl font-bold mb-4">
        Gestion des Travaux de Maintenance
      </h2>

      <MaintenanceJobForm onAddJob={fetchMaintenanceJobs} />

      <h3 className="text-xl font-semibold mt-6">Liste des Travaux</h3>
      {loading ? (
        <p className="text-gray-500">Chargement des travaux...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : jobs.length === 0 ? (
        <p className="text-gray-500">Aucun travail trouvé</p>
      ) : (
        <ul className="list-disc pl-5">
          {jobs.map((job) => (
            <li key={job.id} className="flex justify-between items-center">
              <div>
                {job.description} - {job.date} - {job.status}
              </div>
              <button
                onClick={() => openEditModal(job)}
                className="text-blue-500 hover:underline"
              >
                Modifier
              </button>
            </li>
          ))}
        </ul>
      )}

      {isModalOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
          style={{ zIndex: 9999 }}
        >
          <div className="bg-white p-6 rounded-lg shadow-lg w-3/4 max-w-lg relative">
            <button
              onClick={closeModal}
              className="absolute top-2 right-2 text-gray-500 hover:text-gray-800"
            >
              ×
            </button>
            <h3 className="text-xl font-semibold mb-4">
              Modifier Travail de Maintenance
            </h3>
            <MaintenanceJobForm
              initialData={editingJob}
              onSubmit={handleUpdateJob}
              onCancel={closeModal}
            />
          </div>
        </div>
      )}
    </section>
  );
}

export default MaintenanceJobManagement;
