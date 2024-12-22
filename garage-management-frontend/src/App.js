import React, { useState } from "react";
import ClientManagement from "./components/ClientManagement";
import VehicleManagement from "./components/VehicleManagement";
import MaintenanceJobManagement from "./components/MaintenanceJobManagement";
import "./App.css";

function App() {
  const [activeSection, setActiveSection] = useState("clients");

  const renderSection = () => {
    switch (activeSection) {
      case "clients":
        return <ClientManagement />;
      case "vehicles":
        return <VehicleManagement />;
      case "maintenance-jobs":
        return <MaintenanceJobManagement />;
      default:
        return <ClientManagement />;
    }
  };

  return (
    <div className="App flex min-h-screen bg-gray-100 text-gray-800">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-800 text-white flex flex-col">
        <h1 className="text-2xl font-bold text-center py-6">Gestion Garage</h1>
        <nav className="flex-1">
          <ul className="space-y-2">
            <li>
              <button
                className={`w-full py-3 px-4 text-left hover:bg-gray-700 ${
                  activeSection === "clients" ? "bg-gray-700" : ""
                }`}
                onClick={() => setActiveSection("clients")}
              >
                Clients
              </button>
            </li>
            <li>
              <button
                className={`w-full py-3 px-4 text-left hover:bg-gray-700 ${
                  activeSection === "vehicles" ? "bg-gray-700" : ""
                }`}
                onClick={() => setActiveSection("vehicles")}
              >
                Véhicules
              </button>
            </li>
            <li>
              <button
                className={`w-full py-3 px-4 text-left hover:bg-gray-700 ${
                  activeSection === "maintenance-jobs" ? "bg-gray-700" : ""
                }`}
                onClick={() => setActiveSection("maintenance-jobs")}
              >
                Travaux de Maintenance
              </button>
            </li>
          </ul>
        </nav>
        <footer className="p-4 text-center text-sm text-gray-400">
          © 2024 Gestion Garage
        </footer>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8">{renderSection()}</main>
    </div>
  );
}

export default App;
